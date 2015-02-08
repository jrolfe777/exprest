package com.pv.mfl.bootstrap;

import io.dropwizard.Bundle;
import io.dropwizard.Configuration;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.lifecycle.Managed;
import io.dropwizard.servlets.tasks.Task;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.Nullable;
import javax.ws.rs.Path;
import javax.ws.rs.ext.Provider;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.health.HealthCheck;
import com.google.common.base.Function;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Stage;
import com.google.inject.servlet.GuiceFilter;
import com.pv.mfl.framework.module.DynamicModule;
import com.pv.mfl.framework.module.DynamicModuleDescriptor;
import com.pv.mfl.framework.module.DynamicModuleFactory;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.spi.container.servlet.ServletContainer;
import com.sun.jersey.spi.inject.InjectableProvider;

public class DynamicModuleBundle implements ConfiguredBundle<Configuration> {

   private static final Logger LOG = LoggerFactory.getLogger(DynamicModuleBundle.class);
  
   protected Injector _injector;
   protected List<Module> _allModules = new ArrayList<>();
   protected List<DynamicModuleDescriptor> _descriptors = new ArrayList<>();
   protected BootstrapModule _bootstrapModule;
   protected GuiceContainer _container;
   protected String _basePackage;
   protected Bootstrap<?> _bootstrap;
   protected List<DynamicModule> _dynamicModules = new ArrayList<>();
  
   
   public DynamicModuleBundle(Bootstrap<?> bootstrap, String basePackage) {
      _bootstrap = bootstrap;
      _basePackage = basePackage;      
   }
   
   protected void registerModules() {
      
      Reflections r = getReflectionsForPackage(_basePackage);
      Set<Class<? extends DynamicModule>> moduleClasses = r.getSubTypesOf(DynamicModule.class);
      for (Class<? extends DynamicModule> moduleClass : moduleClasses) {
         try {
            DynamicModule module = DynamicModuleFactory.create(moduleClass, _bootstrap, null);
            _allModules.add(module);
            _dynamicModules.add(module);
         } catch (Throwable t) {
            LOG.error("Could not load module: " + moduleClass, t);
         }
      }
   }
      
   protected void loadModules() throws Exception {
      _dynamicModules.stream().forEach(e -> _descriptors.add(loadModuleDescriptor(e)));
   }
   
   protected DynamicModuleDescriptor loadModuleDescriptor(DynamicModule m) {
      
      Reflections r = getReflectionsForPackage(m.getClass().getPackage().getName());
      
      DynamicModuleDescriptor descriptor = new DynamicModuleDescriptor();
      descriptor.setManaged(r.getSubTypesOf(Managed.class));
      descriptor.setTasks(r.getSubTypesOf(Task.class));
      descriptor.setHealthChecks(r.getSubTypesOf(HealthCheck.class));
      descriptor.setInjectableProviders(r.getSubTypesOf(InjectableProvider.class));
      descriptor.setProviders(r.getTypesAnnotatedWith(Provider.class));
      descriptor.setResources(r.getTypesAnnotatedWith(Path.class));
      descriptor.setBundles(r.getSubTypesOf(Bundle.class));
      
      return descriptor;
   }
   
   protected Reflections getReflectionsForPackage(String basePackage) {
      ConfigurationBuilder configBuilder = new ConfigurationBuilder();
      FilterBuilder filterBuilder = new FilterBuilder();
      configBuilder.addUrls(ClasspathHelper.forPackage(basePackage));
      filterBuilder.include(FilterBuilder.prefix(basePackage));      

      configBuilder.filterInputsBy(filterBuilder).setScanners(new SubTypesScanner(), new TypeAnnotationsScanner());
      
      return new Reflections(configBuilder);           
   }

   public void initialize(Bootstrap<?> bootstrap) {
      _container = new GuiceContainer();
      JerseyContainerModule jerseyContainerModule = new JerseyContainerModule(_container);
      _bootstrapModule = new BootstrapModule();
      
      _allModules.add(jerseyContainerModule);
      _allModules.add(_bootstrapModule);
      registerModules();

      initInjector();
   }

   private void initInjector() {
      try {
         _injector = Guice.createInjector(Stage.PRODUCTION, _allModules);
      } catch (Exception ie) {
         LOG.error("Exception occurred when creating Guice Injector - exiting", ie);
         System.exit(1);
      }
   }

   public void run(final Configuration configuration, final Environment environment) throws Exception {
      loadModules();
      _container.setResourceConfig(environment.jersey().getResourceConfig());
      environment.jersey().replace(
            new Function<ResourceConfig, ServletContainer>() {
               @Nullable
               public ServletContainer apply(ResourceConfig resourceConfig) {
                  return _container;
               }
            });
      environment.servlets().addFilter("Guice Filter", GuiceFilter.class)
            .addMappingForUrlPatterns(null, false, environment.getApplicationContext().getContextPath() + "*");      
      
      _bootstrapModule.setEnvironmentData(configuration, environment, _descriptors);
      _descriptors.stream().forEach(e -> register(e, environment));
   }
   
   public void register(final DynamicModuleDescriptor descriptor, final Environment environment) {
      descriptor.getManaged().parallelStream().forEach(e -> environment.lifecycle().manage(_injector.getInstance(e)));
      descriptor.getTasks().parallelStream().forEach(e -> environment.admin().addTask(_injector.getInstance(e)));
      descriptor.getHealthChecks().parallelStream().forEach(e -> environment.healthChecks().register(e.getName(), _injector.getInstance(e)));
      descriptor.getInjectableProviders().parallelStream().forEach(e -> environment.jersey().register(e));
      descriptor.getProviders().parallelStream().forEach(e -> environment.jersey().register(e));
      descriptor.getResources().parallelStream().forEach(e -> environment.jersey().register(e));
   }   
}

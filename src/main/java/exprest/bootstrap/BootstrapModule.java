package exprest.bootstrap;

import io.dropwizard.Configuration;
import io.dropwizard.setup.Environment;

import java.util.List;

import com.codahale.metrics.MetricRegistry;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import exprest.framework.module.DynamicModuleDescriptor;

public class BootstrapModule extends AbstractModule {
   
   private Configuration _configuration;
   private Environment _environment;
   private List<DynamicModuleDescriptor> _descriptors;

   public BootstrapModule() {
   }

   @Override
   protected void configure() {      
      //bind(Configuration.class).toProvider(() -> _configuration);
   }

   public void setEnvironmentData(Configuration configuration, Environment environment, List<DynamicModuleDescriptor> descriptors) {
      _configuration = configuration;
      _environment = environment;
      _descriptors = descriptors;
   }

   @Provides
   public Environment providesEnvironment() {
      return _environment;
   }
   
   @Provides MetricRegistry providesMetrics() {
      return _environment.metrics();
   }
   
   @Provides List<DynamicModuleDescriptor> providesDescriptors() {
      return _descriptors;
   }
}

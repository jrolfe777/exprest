package exprest.framework.module;

import io.dropwizard.Bundle;
import io.dropwizard.lifecycle.Managed;
import io.dropwizard.servlets.tasks.Task;

import java.util.Set;

import com.codahale.metrics.health.HealthCheck;
import com.sun.jersey.spi.inject.InjectableProvider;

public class DynamicModuleDescriptor {

   protected Set<Class<? extends Managed>> _managed;
   protected Set<Class<? extends Task>> _tasks;
   protected Set<Class<? extends HealthCheck>> _healthChecks;   
   protected Set<Class<?>> _providers;
   protected Set<Class<?>> _resources;
   protected Set<Class<? extends Bundle>> _bundles;
   
   @SuppressWarnings("rawtypes") 
   protected Set<Class<? extends InjectableProvider>> _injectableProviders;
   
   public DynamicModuleDescriptor() {

   }
   
   public void setManaged(Set<Class<? extends Managed>> managed) {
      _managed = managed;
   }
   
   public void setTasks(Set<Class<? extends Task>> tasks) {
      _tasks = tasks;
   }
   
   public void setHealthChecks(Set<Class<? extends HealthCheck>> healthChecks) {
      _healthChecks = healthChecks;
   }
   
   public void setInjectableProviders(Set<Class<? extends InjectableProvider>> injectableProviders) {
      _injectableProviders = injectableProviders;
   }
   
   public void setProviders(Set<Class<?>> providers) {
      _providers = providers;
   }
   
   public void setResources(Set<Class<?>> resources) {
      _resources = resources;
   }
      
   public void setBundles(Set<Class<? extends Bundle>> bundles) {
      _bundles = bundles;
   }
   
   public Set<Class<? extends Managed>> getManaged() {
      return _managed;
   }
   
   public Set<Class<? extends Task>> getTasks() {
      return _tasks;
   }
   
   public Set<Class<? extends HealthCheck>> getHealthChecks() {
      return _healthChecks;
   }
   
   public Set<Class<? extends InjectableProvider>> getInjectableProviders() {
      return _injectableProviders;
   }
   
   public Set<Class<?>> getProviders() {
      return _providers;
   }
   
   public Set<Class<?>> getResources() {
      return _resources;
   }
      
   public Set<Class<? extends Bundle>> getBundles() {
      return _bundles;
   }
}
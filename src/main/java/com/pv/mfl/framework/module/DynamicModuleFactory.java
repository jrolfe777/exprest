package com.pv.mfl.framework.module;

import io.dropwizard.configuration.ConfigurationException;
import io.dropwizard.configuration.ConfigurationFactory;
import io.dropwizard.configuration.ConfigurationFactoryFactory;
import io.dropwizard.configuration.DefaultConfigurationFactoryFactory;
import io.dropwizard.setup.Bootstrap;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

public final class DynamicModuleFactory {
   
   private static final Logger LOG = LoggerFactory.getLogger(DynamicModuleFactory.class);   
   
   private DynamicModuleFactory() {};
      
   public static <T extends DynamicModule> T create(Class<T> moduleClass, Bootstrap<?> bootstrap, String name) 
         throws ConfigurationException, IOException, InstantiationException, IllegalAccessException {
      
      ConfigurationFactoryFactory<T> configFactoryFactory = new DefaultConfigurationFactoryFactory<T>();
      
      ConfigurationFactory<T> configFactory = configFactoryFactory.create(moduleClass, 
            bootstrap.getValidatorFactory().getValidator(), 
            bootstrap.getObjectMapper(), 
            "mfl");
      
      T module = null;
      if (moduleClass.isAnnotationPresent(DynamicModuleConfiguration.class)) {
         String configuredBy = moduleClass.getAnnotation(DynamicModuleConfiguration.class).value();               
         URL configuredByResource = ClassLoader.getSystemResource(configuredBy);
         configuredBy = configuredByResource != null ? configuredByResource.getPath() : configuredBy;
         
         File configFile = new File(configuredBy);
         if (!configFile.exists()) {
            LOG.info("The configuration file {} could not be found. {} will be configured with default values.", configuredBy, moduleClass);
            module = moduleClass.newInstance();
         } else {
            InputStream input = bootstrap.getConfigurationSourceProvider().open(configuredBy);
            JsonNode node = bootstrap.getObjectMapper().readTree(new YAMLFactory().createParser(input));
            if (node == null) {
               LOG.info("The configuration file {} is empty. {} will be configured with default values.", configuredBy, moduleClass);
               module = moduleClass.newInstance();               
            } else {            
               module = configFactory.build(bootstrap.getConfigurationSourceProvider(), configuredBy);
            }
         }
      } else {
         module = moduleClass.newInstance();
      }
      
      return module;
   }
    
}

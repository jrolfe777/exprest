package com.pv.mfl.bootstrap;

import io.dropwizard.Configuration;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;

import com.pv.mfl.doc.swagger.SwaggerBundle;

public class Start extends io.dropwizard.Application<Configuration> {
   
   public static void main(String[] args) throws Exception {
      new Start().run(args);
   }

   @Override
   public String getName() {
      return "mediafusion-lite";
   }

   @Override
   public void initialize(Bootstrap<Configuration> bootstrap) {
      
      try {
         
         bootstrap.setConfigurationSourceProvider(path -> {
            final File file = new File(path);
            if (file.exists()) {
               return new FileInputStream(file);
            } else {
               URL configResource = ClassLoader.getSystemResource(path);
               if (configResource != null) {
                  return ClassLoader.getSystemResource(path).openStream();
               } else {
                  throw new RuntimeException("Could not locate the configuration file specified: " + path);
               }
            }
         });

         bootstrap.addBundle(new DynamicModuleBundle(bootstrap, "com.pv.mfl")); 
         bootstrap.addBundle(new StaticAssetsBundle());
         bootstrap.addBundle(new SwaggerBundle());
         bootstrap.addBundle(new ViewBundle());
      } catch (Throwable t) {
         t.printStackTrace();
      }
   }

   @Override
   public void run(Configuration configuration, Environment environment) throws Exception {
   }
}
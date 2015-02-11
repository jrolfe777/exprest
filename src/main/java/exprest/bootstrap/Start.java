package exprest.bootstrap;

import io.dropwizard.Configuration;
import io.dropwizard.jetty.ConnectorFactory;
import io.dropwizard.jetty.HttpConnectorFactory;
import io.dropwizard.server.DefaultServerFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import exprest.doc.swagger.SwaggerBundle;

public class Start extends io.dropwizard.Application<Configuration> {
   
   public static void main(String[] args) throws Exception {
	  
	   Map<String, String> env = System.getenv();
       for (String envName : env.keySet()) {
           System.out.format("%s=%s%n",
                             envName,
                             env.get(envName));
       }	   
	   
	  if (args != null && args.length > 0) {
          new Start().run(args);  
	  } else {
		  new Start().run(new String[] {"server", "config/dev.yml"});
	  }
      
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
	   	   List<ConnectorFactory> fList = ((DefaultServerFactory)configuration.getServerFactory()).getApplicationConnectors();
	   	   
	   	   Integer port = System.getenv().get("VCAP_APP_PORT") != null ? Integer.parseInt(System.getenv().get("VCAP_APP_PORT")) : 80;
	   	   String host = System.getenv().get("VCAP_APP_HOST") != null ? System.getenv().get("VCAP_APP_HOST") : "127.0.0.1";
	   	   fList.stream().forEach(e -> {
	   		   HttpConnectorFactory httpConnectorFactory = (HttpConnectorFactory)e;
	   		   httpConnectorFactory.setPort(port);
	   		   httpConnectorFactory.setBindHost(host);
	   	   });
	   	  
	   	   ((DefaultServerFactory)configuration.getServerFactory()).setAdminConnectors(new ArrayList<ConnectorFactory>());
//	   	   fList.stream().forEach(e -> {
//	   		   HttpConnectorFactory httpConnectorFactory = (HttpConnectorFactory)e;
//	   		   httpConnectorFactory.setPort(443);
//	   		   httpConnectorFactory.setBindHost(host);
//	   	   });
   }
}
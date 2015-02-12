package exprest.module.search;

import io.dropwizard.lifecycle.Managed;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.ClientConfig;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Provides;

public class SearchManager implements Managed {
   
	protected final JestClient _searchClient;
	
	@SuppressWarnings("rawtypes")
	public SearchManager() {
	   
		JestClient client;
		
		try {
			Map result = new ObjectMapper().readValue(System.getenv("VCAP_SERVICES"), HashMap.class);
	
			String connectionUrl = (String) ((Map) ((Map) ((List)
	                   result.get("searchly")).get(0)).get("credentials")).get("uri");
	       // Configuration
	       ClientConfig clientConfig = new ClientConfig.Builder(connectionUrl).multiThreaded(true).build();
	
	       // Construct a new Jest client according to configuration via factory
	       JestClientFactory factory = new JestClientFactory();
	       factory.setClientConfig(clientConfig);
	       factory.getObject();
	       client = factory.getObject();
		} catch (Exception exp) {
			exp.printStackTrace();
			client = null;
		}
		
		_searchClient = client;
   }

   @Override
   public void start() throws Exception {
     
   }

   @Override
   public void stop() throws Exception {
   
   }
   
   @Provides
   public JestClient providesSearchClient() {
      return _searchClient;
   }
   
   @Provides SearchManager providesSearchManager() {
	   return this;
   }
   
}
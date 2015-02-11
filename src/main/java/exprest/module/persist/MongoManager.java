package exprest.module.persist;

import io.dropwizard.lifecycle.Managed;

import java.net.UnknownHostException;

import com.google.inject.Inject;
import com.google.inject.Provides;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;

public class MongoManager implements Managed {
   protected final MongoClient _mongoClient;
   protected final DB _mongoDB;
   
   @Inject
   public MongoManager(MongoModule mongoModule) throws UnknownHostException {
      
      ServerAddress mongoServer = new ServerAddress(mongoModule.hostName, mongoModule.port);
      MongoClientOptions opts = MongoClientOptions.builder().socketKeepAlive(true).description("this is a testyo").build();
      
      _mongoClient = new MongoClient(mongoServer, opts);
      _mongoDB = _mongoClient.getDB(mongoModule.database);
   }

   @Override
   public void start() throws Exception {
     
   }

   @Override
   public void stop() throws Exception {
      _mongoClient.close();
   }
   
   @Provides
   public MongoClient providesMongoClient() {
      return _mongoClient;
   }
   
   @Provides
   public DB providesMongoDB() {
      return _mongoDB;
   }
}
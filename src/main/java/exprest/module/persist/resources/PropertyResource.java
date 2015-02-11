package exprest.module.persist.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

import exprest.module.persist.MongoManager;

@Path("/bags")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api(value = "/bags", description = "Operations for accessing and manipulating client owned property bags.")
public class PropertyResource {
   
   public static final String PROPERTY_COLLECTION_NAME = "property_bag";
   
   protected final DBCollection _propertyCollection;
   
   @Inject
   public PropertyResource(MongoManager mongoManager) {
      _propertyCollection = mongoManager.providesMongoDB().getCollection(PROPERTY_COLLECTION_NAME);
   }
  
   @GET
   @Path("/{key}")
   @SuppressWarnings("unchecked")
   @ApiOperation(value = "Retrieve a property bag using a key.", response = PropertyBag.class)
   public PropertyBag getBag(@ApiParam(value="The key to obtain a property bag for", required=true) @PathParam("key") String key) {
      
      BasicDBObject query = new BasicDBObject("_id", key);
      DBObject result = _propertyCollection.findOne(query);
      
      PropertyBag bag = new PropertyBag();
      bag.key = key;
      bag.data = result != null ? result.toMap() : Maps.newHashMap();
      return bag;    
   }
   
   @PUT
   @Path("/{key}")
   @ApiOperation(value="Create or overwrite a property bag at the given key.")
   public void putBag(
            @ApiParam(value="The identifier of this property bag.", required=true) @PathParam("key") String key,
            @ApiParam(value="A JSON object representing the property bag.") PropertyBag bag) {
      
      System.out.println(bag.data);
      _propertyCollection.insert(new BasicDBObject(key, bag.data));
      
   }
   
   
   @DELETE
   @Path("/{key}")
   @ApiOperation(value="Remove the property bag located at the given key.")
   public void deleteBag(
            @ApiParam(value="The identifier of this property bag.", required=true) @PathParam("key") String key) {
      
      
   }
}

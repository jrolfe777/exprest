package exprest.module.system.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

import exprest.framework.response.SingleValue;
import exprest.module.system.auth.AuthParam;
import exprest.module.system.auth.User;
import exprest.module.system.models.Uptime;

@Path("/system")
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "/system", description = "Provides run-time information about this server, including uptime, deployed services, and more.")
public class SystemResource {

   private static final long START_TIME = System.currentTimeMillis();

   @Inject
   public SystemResource() {}

   @GET
   @Path("/uptime")
   @ApiOperation(value="Return the amount of time that has elapsed since the server was started, in seconds.", response=Uptime.class)
   public Uptime uptime() {
      Uptime uptime = new Uptime();
      uptime.millis = System.currentTimeMillis() - START_TIME;
      return uptime;
   }
   
   @GET
   @Path("/echo")
   @ApiOperation(value="Echo a parameter value.")   
   public SingleValue echo(@ApiParam(value="The value to echo.", required=true) @QueryParam("value") String value) {
      return new SingleValue(value);
   }
   
   @GET
   @Path("/user") 
   @ApiOperation(value="Return the authenticated user.", response=User.class)
   public User user(@AuthParam Optional<User> user) {
      return user.get();
   }
}
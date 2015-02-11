package test.exprest.module.system.resources;

@javax.ws.rs.Path(value="/system")
@javax.ws.rs.Produces(value="application/json")

public interface SystemResource {

	@javax.ws.rs.GET
	@javax.ws.rs.Path(value="/echo")
	public exprest.framework.response.SingleValue echo(@javax.ws.rs.QueryParam(value="value") java.lang.String value);

	@javax.ws.rs.GET
	@javax.ws.rs.Path(value="/user")
	public exprest.module.system.auth.User user();

	@javax.ws.rs.GET
	@javax.ws.rs.Path(value="/uptime")
	public exprest.module.system.models.Uptime uptime();

}
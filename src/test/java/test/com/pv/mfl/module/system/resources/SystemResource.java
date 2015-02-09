package test.com.pv.mfl.module.system.resources;

@javax.ws.rs.Path(value="/system")
@javax.ws.rs.Produces(value="application/json")

public interface SystemResource {

	@javax.ws.rs.GET
	@javax.ws.rs.Path(value="/echo")
	public com.pv.mfl.framework.response.SingleValue echo(@javax.ws.rs.QueryParam(value="value") java.lang.String value);

	@javax.ws.rs.GET
	@javax.ws.rs.Path(value="/user")
	public com.pv.mfl.module.system.auth.User user();

	@javax.ws.rs.GET
	@javax.ws.rs.Path(value="/uptime")
	public com.pv.mfl.module.system.models.Uptime uptime();

}
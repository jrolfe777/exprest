package test.com.pv.mfl.module.persist.resources;

@javax.ws.rs.Path(value="/bags")
@javax.ws.rs.Produces(value="application/json")
@javax.ws.rs.Consumes(value="application/json")

public interface PropertyResource {

	@javax.ws.rs.GET
	@javax.ws.rs.Path(value="/{key}")
	public com.pv.mfl.module.persist.resources.PropertyBag getBag(@javax.ws.rs.PathParam(value="key") java.lang.String key);

	@javax.ws.rs.DELETE
	@javax.ws.rs.Path(value="/{key}")
	public void deleteBag(@javax.ws.rs.PathParam(value="key") java.lang.String key);

	@javax.ws.rs.PUT
	@javax.ws.rs.Path(value="/{key}")
	public void putBag(@javax.ws.rs.PathParam(value="key") java.lang.String key, com.pv.mfl.module.persist.resources.PropertyBag bag);

}
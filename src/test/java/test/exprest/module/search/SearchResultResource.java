package test.exprest.module.search;

@javax.ws.rs.Path(value="/search")
@javax.ws.rs.Produces(value="application/json")
@javax.ws.rs.Consumes(value="application/json")

public interface SearchResultResource {

	@javax.ws.rs.GET
	@javax.ws.rs.Path(value="/createSearchIndex")
	public exprest.framework.response.SingleValue createSearchIndex(@javax.ws.rs.QueryParam(value="indexName") java.lang.String indexName);

	@javax.ws.rs.GET
	@javax.ws.rs.Path(value="indexData")
	public exprest.framework.response.SingleValue indexMusic(@javax.ws.rs.QueryParam(value="title") java.lang.String title, @javax.ws.rs.QueryParam(value="artist") java.lang.String artist, @javax.ws.rs.QueryParam(value="album") java.lang.String album, java.lang.String indexName);

	@javax.ws.rs.GET
	@javax.ws.rs.Path(value="/results")
	public java.util.List getSearchResults(@javax.ws.rs.QueryParam(value="query") java.lang.String query, @javax.ws.rs.QueryParam(value="indexName") java.lang.String indexName);

}
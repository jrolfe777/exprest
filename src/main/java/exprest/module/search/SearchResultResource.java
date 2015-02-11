package exprest.module.search;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.indices.CreateIndex;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.inject.Inject;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

import exprest.framework.response.SingleValue;

@Path("/search")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api(value = "/search", description = "A simple search microservice.")
public class SearchResultResource {
   
   protected final JestClient _searchClient;
   
   @Inject
   public SearchResultResource(JestClient searchClient) {
	   _searchClient = searchClient;
   }
  
   @GET
   @Path("/results")
   @ApiOperation(value = "Look for items based on a simple query.", response = SingleValue.class)
   public List<Music> getSearchResults(
		   @ApiParam(value="The search string.", required=true) @QueryParam("query") String query,
		   @ApiParam(value="The search index.", required=true) @QueryParam("indexName") String indexName) throws Exception {
   
	   String elasticQuery = "{\n" +
			    "    \"query\": {\n" +
			    "        \"filtered\" : {\n" +
			    "            \"query\" : {\n" +
			    "                \"query_string\" : {\n" +
			    "                    \"query\" : \"" + query + "\"\n" +
			    "                }\n" +
			    "            }\n"+
			    "        }\n" +
			    "    }\n" +
			    "}";
	   
	   Search search = (Search) new Search.Builder(elasticQuery).addIndex(indexName).addType("music").build();

	   JestResult searchResult = _searchClient.execute(search);
	   return searchResult.getSourceAsObjectList(Music.class);
   }
   
   @GET   //should be a POST, but this is fakeware.
   @Path("/createSearchIndex")
   @ApiOperation(value = "Create a new index within the search system.", response = SingleValue.class)
   public SingleValue createSearchIndex(
		   @ApiParam(value="The name of the new index.", required=true) @QueryParam("indexName") String indexName) throws Exception {
	   
	   JestResult result = _searchClient.execute(new CreateIndex.Builder(indexName).build());
	   return new SingleValue(result.getJsonString());
	   
   }
   
   @GET
   @Path("indexData")
   @ApiOperation(value="Create a new search document within the search system.", response = SingleValue.class)
   public SingleValue indexMusic(
		   @ApiParam(value="The title of the song to index.", required=true) @QueryParam("title") String title,
		   @ApiParam(value="The name of the artist.", required=true) @QueryParam("artist") String artist,
		   @ApiParam(value="The name of the album.", required=true) @QueryParam("album") String album,
		   @ApiParam(value="The index to insert this music into.", required=true) String indexName) throws Exception {
	   
	   Music m = new Music();
	   m.title = title;
	   m.artist = artist;
	   m.album = album;
	   
	   Index index = new Index.Builder(m).index(indexName).type("music").build();
	   JestResult result = _searchClient.execute(index);
	   
	   return new SingleValue(result.getJsonString());
   }
   
   class Music {
	   
	   @JsonProperty public String title;
	   @JsonProperty public String artist;
	   @JsonProperty public String album;
	   
	   public Music() {
		   
	   }
   } 
}

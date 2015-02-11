package exprest.module.persist.resources;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wordnik.swagger.annotations.ApiModelProperty;

public class PropertyBag {
   
   @JsonProperty @ApiModelProperty(value = "The property bag identifier.  Unique by user.", required=true) public String key;
   @JsonProperty @ApiModelProperty(value = "The property bag data.", required=true) public Map<Object,Object> data;

   public PropertyBag() { /*jackson deserialize*/  }
}

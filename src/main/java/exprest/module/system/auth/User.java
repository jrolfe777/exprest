package exprest.module.system.auth;

import java.util.Set;
import java.util.TreeSet;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class User {

   protected Set<Authority> _authorities = new TreeSet<>();
   protected String _name = "joel";
   protected String _secret = "milkdud";
   
   public User() {
      _authorities.add(Authority.ROLE_USER);
   }
   
   @JsonProperty public String getName() {
      return _name;
   }
   
   @JsonProperty public Set<Authority> getAuthorities() {
      return _authorities;
   }
   
   @JsonIgnore public String getSecret() {
      return _secret;
   }
}

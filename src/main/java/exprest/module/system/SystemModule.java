package exprest.module.system;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.inject.Provides;
import com.google.inject.name.Named;

import exprest.framework.module.DynamicModule;
import exprest.framework.module.DynamicModuleConfiguration;
import exprest.module.system.auth.BaseAuthenticator;
import exprest.module.system.auth.HmacAuthenticator;
import exprest.module.system.auth.User;
import exprest.module.system.auth.UserRegistry;

@DynamicModuleConfiguration("system.yml")
public class SystemModule extends DynamicModule {
 
   @JsonProperty public String realm = "REST";
   
   @Override
   protected void configure() {
      //bind(BaseAuthenticator.class).to(BasicAuthenticator.class);
      bind(BaseAuthenticator.class).to(HmacAuthenticator.class);
   }
   
   public @Provides @Named("realm") String providesRealm() {
      return realm;
   }
   
   public @Provides UserRegistry providesUserRegistry() {
      return (String key) -> new User();
   }   
}

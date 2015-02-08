package com.pv.mfl.module.system;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.pv.mfl.framework.module.DynamicModule;
import com.pv.mfl.framework.module.DynamicModuleConfiguration;
import com.pv.mfl.module.system.auth.BaseAuthenticator;
import com.pv.mfl.module.system.auth.HmacAuthenticator;
import com.pv.mfl.module.system.auth.User;
import com.pv.mfl.module.system.auth.UserRegistry;

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

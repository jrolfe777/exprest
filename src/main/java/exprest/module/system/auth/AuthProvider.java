package exprest.module.system.auth;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.sun.jersey.api.model.Parameter;
import com.sun.jersey.core.spi.component.ComponentContext;
import com.sun.jersey.core.spi.component.ComponentScope;
import com.sun.jersey.spi.inject.Injectable;
import com.sun.jersey.spi.inject.InjectableProvider;

public class AuthProvider implements InjectableProvider<AuthParam, Parameter> {

   private final BaseAuthenticator _authenticator;
   private final String _realm;

   @Inject
   AuthProvider(BaseAuthenticator authenticator, @Named("realm") String realm) {
      _authenticator = authenticator;
      _realm = realm;
   }

   @Override
   public ComponentScope getScope() {
      return ComponentScope.PerRequest;
   }

   @Override
   public Injectable<?> getInjectable(ComponentContext ic, AuthParam a, Parameter c) {
      return new AuthInjectable(_authenticator, _realm, a);
   }
}
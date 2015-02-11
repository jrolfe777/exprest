package exprest.module.system.auth;


import io.dropwizard.auth.AuthenticationException;

import com.google.common.base.Optional;
import com.google.inject.Inject;

import exprest.util.crypto.Hmac;

public class HmacAuthenticator extends BaseAuthenticator {

   protected final UserRegistry _userRegistry;
   
   @Inject
   HmacAuthenticator(UserRegistry userRegistry) {
      _userRegistry = userRegistry;
   }

  @Override
  public Optional<User> authenticate(Credentials credentials) throws AuthenticationException {

     Optional<User> result = Optional.absent();

     if (credentials instanceof HmacCredentials) {
        HmacCredentials hmacCreds = (HmacCredentials) credentials;
        User user = _userRegistry.findUser(hmacCreds.getApiKey());
        if (user != null) {
           String computedSignature = new String(Hmac.computeSignature(hmacCreds.getCanonicalRepresentation(), user.getSecret()));
           System.out.println("computed Signature: \n" + hmacCreds.getCanonicalRepresentation());
           System.out.println("SIGNATURE: " + computedSignature);
           if (isEqual(computedSignature.getBytes(), hmacCreds.getSignature().getBytes())) {
              result = Optional.of(user);
            }
        }
     }

     return result;
  }
  
  @Override
  public CredentialParser getCredentialParser() {
     return new HmacCredentialParser();
  }

}
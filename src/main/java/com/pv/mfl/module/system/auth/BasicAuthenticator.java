package com.pv.mfl.module.system.auth;

import io.dropwizard.auth.AuthenticationException;

import com.google.common.base.Optional;
import com.google.inject.Inject;

public class BasicAuthenticator extends BaseAuthenticator {

   protected final UserRegistry _userRegistry;
   
   @Inject
   BasicAuthenticator(UserRegistry userRegistry) {
      _userRegistry = userRegistry;
   }
   
   @Override
   public Optional<User> authenticate(Credentials credentials) throws AuthenticationException {

      // Get the User referred to by the API key
      Optional<User> result = Optional.absent();

      if (credentials instanceof BasicCredentials) {
         BasicCredentials basicCreds = (BasicCredentials) credentials;
         User user = _userRegistry.findUser(basicCreds.getUserName());
         if (user != null) {
            // Avoid timing attacks by verifying every byte every time
            if (isEqual(basicCreds.getPassword().getBytes(), user.getSecret().getBytes())) {
               result = Optional.of(user);
            }
         }
      }

      return result;

   }

   @Override
   public CredentialParser getCredentialParser() {
      return new BasicCredentialParser();
   }

}
package com.pv.mfl.module.system.auth;

import io.dropwizard.auth.AuthenticationException;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.sun.jersey.api.core.HttpContext;
import com.sun.jersey.server.impl.inject.AbstractHttpContextInjectable;


class AuthInjectable extends AbstractHttpContextInjectable<Optional<User>> {

   private static final Logger LOGGER = LoggerFactory.getLogger(AuthInjectable.class);
   
   private final BaseAuthenticator _authenticator;
   private final String _realm;
   private final Set<Authority> _requiredAuthorities;
   private final boolean _required;

   AuthInjectable(BaseAuthenticator authenticator, String realm, AuthParam p) {

      _authenticator = authenticator;
      _realm = realm;
      _requiredAuthorities = new TreeSet<Authority>(Arrays.asList(p.value()));
      _required = p.required();
   }

   @Override
   public Optional<User> getValue(HttpContext httpContext) {
      
      Optional<User> result = Optional.absent();

      try {
          
         Credentials credentials = _authenticator.getCredentialParser().getCredentials(httpContext);
         if (credentials != null) {
            result = _authenticator.authenticate(credentials);
            if (result.isPresent()) {
               if (!result.get().getAuthorities().containsAll(_requiredAuthorities)) {
                  result = Optional.absent();
               }
            }
         }

      } catch (IllegalArgumentException e) {
         LOGGER.debug("Error decoding credentials", e);
      } catch (AuthenticationException e) {
         LOGGER.warn("Error authenticating credentials", e);
         throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
      }
      
      if (_required && !result.isPresent()) {
         final String challenge = String.format(_authenticator.getCredentialParser().getPrefix() + " realm=\"%s\"", _realm);
         throw new WebApplicationException(Response.status(Response.Status.UNAUTHORIZED)
                                                   .header(HttpHeaders.WWW_AUTHENTICATE, challenge)
                                                   .entity("Credentials are required to access this resource.")
                                                   .type(MediaType.TEXT_PLAIN_TYPE)
                                                   .build());
      }
      
      return result;
   }

}
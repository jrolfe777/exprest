package com.pv.mfl.module.system.auth;

import javax.ws.rs.core.HttpHeaders;

import com.sun.jersey.api.core.HttpContext;

public abstract class CredentialParser {

   public abstract String getPrefix();
   public abstract Credentials parseCredentials(String authHeader, HttpContext c);
   
   public Credentials getCredentials(HttpContext c) {
      
      Credentials result = null;
      String header = c.getRequest().getHeaderValue(HttpHeaders.AUTHORIZATION);

      if (header != null) {
         int space = header.indexOf(' ');
         if (space > 0) {
            String method = header.substring(0, space);
            if (getPrefix().equalsIgnoreCase(method)) {                     
               result = parseCredentials(header, c);
            }
         }
      }
      
      return result;
   }


//      if (required) {
//          final String challenge = String.format(CHALLENGE_FORMAT, realm);
//          throw new WebApplicationException(Response.status(Response.Status.UNAUTHORIZED)
//                                                    .header(HttpHeaders.WWW_AUTHENTICATE,
//                                                            challenge)
//                                                    .entity("Credentials are required to access this resource.")
//                                                    .type(MediaType.TEXT_PLAIN_TYPE)
//                                                    .build());
//      }
//      return null;

   
   
}

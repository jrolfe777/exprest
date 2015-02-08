package com.pv.mfl.module.system.auth;

import org.eclipse.jetty.util.B64Code;
import org.eclipse.jetty.util.StringUtil;

import com.sun.jersey.api.core.HttpContext;

public class BasicCredentialParser extends CredentialParser {

   public String getPrefix() {
      return "Basic";
   }
      
   public Credentials parseCredentials(String authHeader, HttpContext c) {      
      Credentials credentials = null;
      String encoded = authHeader.split(" ")[1];
      String decoded = B64Code.decode(encoded, StringUtil.__ISO_8859_1);
      String[] authParts = decoded.split(":");
      
      if (authParts.length == 2) {
          credentials = new BasicCredentials(authParts[0], authParts[1]);
      }
      
      return credentials;
   }
}

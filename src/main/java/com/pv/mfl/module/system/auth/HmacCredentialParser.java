package com.pv.mfl.module.system.auth;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.stream.Collectors;

import com.pv.mfl.util.crypto.Hmac;
import com.sun.jersey.api.container.ContainerException;
import com.sun.jersey.api.core.HttpContext;
import com.sun.jersey.core.util.ReaderWriter;
import com.sun.jersey.spi.container.ContainerRequest;

public class HmacCredentialParser extends CredentialParser {

   public String getPrefix() {
      return "HMAC";
   }
      
   public Credentials parseCredentials(String authHeader, HttpContext c) {      
       
      Credentials credentials = null;   
      
      // Expect form of "Authorization: <Algorithm> <PublicKey> <Signature>"
      String[] authTokens = authHeader.split(" ");

      if (authTokens.length == 3) {

         String publicKey = authTokens[1];
         String signature = authTokens[2];
         ContainerRequest containerRequest = (ContainerRequest) c.getRequest();
         String canonicalRepresentation = createCanonicalRepresentation(containerRequest);
   
         credentials = new HmacCredentials(publicKey, signature, canonicalRepresentation);
      }
      
      return credentials;
   }
   
   
   public static String createCanonicalRepresentation(ContainerRequest containerRequest) {
      
      byte[] body = null;
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      InputStream in = containerRequest.getEntityInputStream();
      
      try {
         if (in != null && in.available() > 0) {
            ReaderWriter.writeTo(in, out);
            body = out.toByteArray();
         }     
       } catch (IOException ex) {
         throw new ContainerException(ex);
       }
      
      Map<String,String> singleValueQueries = containerRequest.getQueryParameters().entrySet().stream()
               .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().stream().collect(Collectors.joining(","))));
      
      return Hmac.toCanonicalRepresentation(containerRequest.getMethod(), containerRequest.getRequestUri().getPath(),
               containerRequest.getHeaderValue(Hmac.X_HMAC_DATE), singleValueQueries, body);   
   }
   
}

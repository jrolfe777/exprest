package com.pv.mfl.util.crypto;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.google.common.base.Charsets;
import com.sun.jersey.core.util.Base64;

public final class Hmac {
   
   public static final String AUTHORIZATION_HEADER_PREFIX = "HMAC";
   public static final String X_HMAC_NONCE = "X-HMAC-Nonce";
   public static final String X_HMAC_DATE = "X-HMAC-Date";
   
   private static final String HMAC_SHA_ALGORITHM = "HmacSHA256";
   
   /**
    * Compute the HMAC signature for the given data with the shared secret
    *
    * @param algorithm     The algorithm to use (e.g. "HmacSHA512")
    * @param data          The data to sign
    * @param sharedSecret  The shared secret key to use for signing
    *
    * @return A base 64 encoded signature encoded as UTF-8
    */
   public static String computeSignature(String data, String sharedSecret) {

      String signedData = null;  
      
      try {
         SecretKey secretKey = new SecretKeySpec(sharedSecret.getBytes(Charsets.UTF_8), HMAC_SHA_ALGORITHM);
         Mac mac = Mac.getInstance(HMAC_SHA_ALGORITHM);
         mac.init(secretKey);
         mac.update(data.getBytes(Charsets.UTF_8));
         signedData = new String(Base64.encode(mac.doFinal()), Charsets.UTF_8);
      } catch (Exception e) {
         throw new RuntimeException("Invalid System Configuration.", e);
      }
      
      return signedData;
   }
   
   public static final String toCanonicalRepresentation(String verb, String uri, 
            String dateHeader, Map<String,String> queryParams, byte[] body) {
      
      String canonicalVerb = verb.toUpperCase();
      String canonicalUri = (uri != null ? (uri.length() > 0 ? uri : "/") : "/");
      String canonicalQuery = toCanonoicalParams(queryParams);
             
      String contentMD5 = MD5.computeMD5(body);      

      StringBuilder canonicalRepresentationBuilder = new StringBuilder();
      canonicalRepresentationBuilder
         .append(canonicalVerb).append("\n")
         .append(canonicalUri).append("\n")
         .append(dateHeader).append("\n")
         .append(canonicalQuery).append("\n")
         .append(contentMD5);
      
      return canonicalRepresentationBuilder.toString();      
   }
   
   public static final String toCanonoicalParams(Map<String,String> params) {      
      StringBuilder queryParamsBuilder = new StringBuilder();
      
      if (params != null) {
         params.entrySet().stream().sorted((e1, e2) -> e1.getKey().compareTo(e2.getKey())).forEach(e -> {
            if (!e.getKey().equalsIgnoreCase("Authorization")) {
               if (queryParamsBuilder.length() > 0) {
                  queryParamsBuilder.append("&");
               }
               queryParamsBuilder
                  .append(percentEncodeRfc3986(e.getKey()).toLowerCase())
                  .append("=").append(percentEncodeRfc3986(e.getValue()).trim());
            }
         });
      }
      
      return queryParamsBuilder.toString();
   }
   
   
   private static String percentEncodeRfc3986(String s) {
      String out;
      try {
         out = URLEncoder.encode(s, "UTF-8").replace("+", "%20").replace("*", "%2A").replace("%7E", "~");
      } catch (UnsupportedEncodingException e) {
         out = s;
      }
      return out;
   }
}

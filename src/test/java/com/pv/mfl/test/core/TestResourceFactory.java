package com.pv.mfl.test.core;

import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ws.rs.core.HttpHeaders;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.ReadableInstant;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.pv.mfl.util.crypto.Hmac;

import feign.Feign;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.jaxrs.JAXRSModule.JAXRSContract;



public class TestResourceFactory {
   
   public static <T> T getResource(Class<T> resource) {
      return Feign.builder()
               .decoder(new JacksonDecoder())
               .encoder(new JacksonEncoder())
               .contract(new JAXRSContract())
               .requestInterceptor(new HmacAuthInterceptor("joel", "milkdud"))      
               .target(resource, "http://localhost:8080");
   }
   
   static class HmacAuthInterceptor implements RequestInterceptor {
      
      private final String _publicKey;
      private final String _sharedSecret;
      
      public HmacAuthInterceptor(String publicKey, String sharedSecret) {
         _publicKey = publicKey;
         _sharedSecret = sharedSecret;
      }

      @Override
      public void apply(RequestTemplate template) {
         
         String httpNow = formatHttpDateHeader(new DateTime().withZone(DateTimeZone.UTC));         
         template.header(Hmac.X_HMAC_DATE, httpNow);

         //Feign allows for the possibility of multi-valued params; we convert these to comma-delimited where necessary
         Map<String,String> singleValueQueries = template.queries().entrySet().stream()
                  .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().stream().collect(Collectors.joining(","))));
         
         String canonicalRepresentation = Hmac.toCanonicalRepresentation(
                  template.method(), template.url(), httpNow, singleValueQueries, template.body());
         
         // Build the authorization header
         String signature = Hmac.computeSignature(canonicalRepresentation, _sharedSecret);
         String authorization = Hmac.AUTHORIZATION_HEADER_PREFIX + " " + _publicKey + " " + signature;
         
         template.header(HttpHeaders.AUTHORIZATION, authorization);
      }      
   }
   
   private static final DateTimeFormatter rfc1123 = DateTimeFormat
            .forPattern("EEE, dd MMM yyyy HH:mm:ss 'GMT'")
            .withLocale(Locale.US) // For common language
            .withZone(DateTimeZone.UTC); // For GMT
   
   private static String formatHttpDateHeader(ReadableInstant when) {
      return rfc1123.print(when);
    }   
 }

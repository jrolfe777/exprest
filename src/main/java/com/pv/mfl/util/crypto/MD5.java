package com.pv.mfl.util.crypto;

import java.security.MessageDigest;

import com.sun.jersey.core.util.Base64;

public final class MD5 {
   
   public static final String MD5_ALGORITHM = "MD5";
   
   private MD5() {}   
   
   public static String computeMD5(byte[] data) { 
      
      String md5 = "";
      
      if (data != null) {
         try {
            MessageDigest digest = MessageDigest.getInstance(MD5_ALGORITHM);
            digest.update(data);
            md5 = new String(Base64.encode(digest.digest()));
         } catch (Exception e) {
            throw new RuntimeException("Invalid System Configuration.", e);
         }
      }
      
      return md5;
   }
  
}

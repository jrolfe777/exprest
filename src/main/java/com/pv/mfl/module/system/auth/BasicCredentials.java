package com.pv.mfl.module.system.auth;

public class BasicCredentials implements Credentials {

   protected final String _userName;
   protected final String _password;
   
   BasicCredentials(String userName, String password) {
      _userName = userName;
      _password = password;
   }
   
   public String getUserName() {
      return _userName;
   }
   
   public String getPassword() {
      return _password;
   }
   
}

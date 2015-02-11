package exprest.module.system.auth;

import io.dropwizard.auth.Authenticator;

public abstract class BaseAuthenticator implements Authenticator<Credentials, User> {
   
   protected abstract CredentialParser getCredentialParser();
   
  /**
   * Performs a byte array comparison with a constant time
   *
   * @param a A byte array
   * @param b Another byte array
   * @return True if the byte array have equal contents
   */
  public static boolean isEqual(byte[] a, byte[] b) {
    if (a.length != b.length) {
      return false;
    }

    int result = 0;
    for (int i = 0; i < a.length; i++) {
      result |= a[i] ^ b[i];
    }
    return result == 0;
  }
}
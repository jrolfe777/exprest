package exprest.module.system.auth;
import static com.google.common.base.Preconditions.checkNotNull;

import java.security.MessageDigest;

import com.google.common.base.Charsets;
import com.google.common.base.Objects;


public class HmacCredentials implements Credentials {
   
  private final String _publicKey;
  private final String _signature;
  private final String _canonicalRepresentation;

  /**
   * @param algorithm               The algorithm used for computing the digest (e.g. "HmacSHA1", "HmacSHA256", "HmacSHA512")
   * @param publicKey               The API key used for looking up the shared secret key associated with the user
   * @param digest                  The digest of (contents + shared secret key)
   * @param canonicalRepresentation The canonical representation of the request that was signed
   */
  public HmacCredentials(String publicKey, String digest, String canonicalRepresentation) {
    _publicKey = checkNotNull(publicKey);
    _signature = checkNotNull(digest);
    _canonicalRepresentation = checkNotNull(canonicalRepresentation);
  }

  public String getApiKey() {
    return _publicKey;
  }

  public String getSignature() {
    return _signature;
  }

  public String getCanonicalRepresentation() {
    return _canonicalRepresentation;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if ((obj == null) || (getClass() != obj.getClass())) {
      return false;
    }
    final HmacCredentials that = (HmacCredentials) obj;

    final byte[] thisBytes = _signature.getBytes(Charsets.UTF_8);
    final byte[] thatBytes = that._signature.getBytes(Charsets.UTF_8);
    return _publicKey.equals(that._publicKey) && MessageDigest.isEqual(thisBytes, thatBytes);
  }

  @Override
  public int hashCode() {
    return (31 * _publicKey.hashCode()) + _signature.hashCode();
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
      .add("publicKey", _publicKey)
      .add("signature", _signature)
      .add("contents", _canonicalRepresentation)
      .toString();
  }
}
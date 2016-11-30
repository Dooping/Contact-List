package authenticator;

import java.security.*;
import java.util.Base64;

public final class AESencrp {
  	
  	public static String hash(String Data, int nonce) throws Exception {
  		MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(Integer.toString(nonce).getBytes());
        byte[] bytes = md.digest(Data.getBytes("UTF-8"));
        return Base64.getEncoder().encodeToString(bytes);
    }
  	
  	public static String hash(String Data) throws Exception {
  		MessageDigest md = MessageDigest.getInstance("SHA-512");
        byte[] bytes = md.digest(Data.getBytes("UTF-8"));
        return Base64.getEncoder().encodeToString(bytes);
    }

}


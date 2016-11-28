package authenticator;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.util.Base64.*;

public final class AESencrp {
  private static final String ALGO = "AES";
  private static final byte[] keyValue = 
  new byte[] { 'T', 'h', 'e', 'B', 'e', 's', '7', 'S', '$', 'c', 'r','e', 't', 'K', 'e', 'y' };

  	public static String encrypt(String Data) throws Exception {
        Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = c.doFinal(Data.getBytes());
        return java.util.Base64.getEncoder().encodeToString(encVal);
    }
  	
  	public static String hash(String Data, int nonce) throws Exception {
  		MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(Integer.toString(nonce).getBytes());
        byte[] bytes = md.digest(Data.getBytes("UTF-8"));
        return java.util.Base64.getEncoder().encodeToString(bytes);
    }

    public static String decrypt(String encryptedData) throws Exception {
        Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decordedValue = java.util.Base64.getDecoder().decode(encryptedData);
        byte[] decValue = c.doFinal(decordedValue);
        return new String(decValue);
    }

    private static Key generateKey() throws Exception {
        Key key = new SecretKeySpec(keyValue, ALGO);
        return key;
    }
    
    public static SecretKey generateUserKey() throws Exception{
    	return KeyGenerator.getInstance(ALGO).generateKey();
    }

    /*public static void main(String[] args) throws Exception {

        String password = "salt"+"my_passwd_$";
        String passwordEnc = AESencrp.encrypt(password);
        String passwordEnc1 = AESencrp.encrypt(password);
        String passwordDec = AESencrp.decrypt(passwordEnc);

        System.out.println("Plain Text : " + password);
        System.out.println("Encrypted Text : " + passwordEnc);
        System.out.println("Encrypted Text : " + passwordEnc1);
        System.out.println("Decrypted Text : " + passwordDec);
    }*/


}


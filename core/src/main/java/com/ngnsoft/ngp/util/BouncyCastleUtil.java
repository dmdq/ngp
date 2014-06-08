package com.ngnsoft.ngp.util;

import java.security.Key;
import java.security.Security;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 *
 * @author fcy
 */
public class BouncyCastleUtil {
    
    public static final String AES_KEY_PRE = "d37cb651";
    
    static {
        Security.addProvider(new BouncyCastleProvider());
    }
    
    public static byte[] aesEncrypt(String secretKey, String clearText) {
        byte[] cipherByte = null;
        try {
            byte[] keyBytes = secretKey.getBytes();
            Key key = new SecretKeySpec(keyBytes, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
            cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(keyBytes));
            cipherByte = cipher.doFinal(clearText.getBytes("UTF8"));
        } catch (Exception ex) {
        }
        return cipherByte;
    }
    
    public static String aesEncode(String secretKey, String clearText) {
        byte[] cipherByte = aesEncrypt(secretKey, clearText);
        return MiscUtil.byteToBase64(cipherByte);
    }
    
    public static String aesDecrypt(String secretKey, byte[] cipherByte) {
        String clearText = null;
        try {
            byte[] keyBytes = secretKey.getBytes();
            Key key = new SecretKeySpec(keyBytes, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(keyBytes));
            byte[] dec = cipher.doFinal(cipherByte);
            clearText = new String(dec, "UTF8");
        } catch (Exception ex) {
        }
        return clearText;
    }
    
    public static String aesDecode(String secretKey, String cipherText) {
        byte[] cipherByte = MiscUtil.base64ToByte(cipherText);
        return aesDecrypt(secretKey, cipherByte);
    }
    
    public static void main(String[] args) {
        String sk = "abcdefghijklmnop";
        String clearText = "good很好的";
        String cipherText = aesEncode(sk, clearText);
        System.out.println("cipherText:" + cipherText);
        System.out.println(aesDecode("abcdefghijklmnop", cipherText));
    }
    
}

package org.itevents.security;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by ramax on 1/15/16.
 */
@Component
public class AESCryptTokenService implements CryptTokenService{

    private final String initVectorHex = "000102030405060708090A0B0C0D0E0F";

    private final String keyHex = "000102030405060708090A0B0C0D0E0F101112131415161718191A1B1C1D1E1F";

    private ObjectMapper mapper = new ObjectMapper();

    private String encryptAES(String str) throws Exception {
        IvParameterSpec iv = new IvParameterSpec(Hex.decodeHex(initVectorHex.toCharArray()));
        SecretKeySpec skeySpec = new SecretKeySpec(Hex.decodeHex(keyHex.toCharArray()), "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

        byte[] encrypted = cipher.doFinal(str.getBytes());

        return Base64.encodeBase64String(encrypted);
    }

    private String decryptAES(String str) throws Exception {
        IvParameterSpec iv = new IvParameterSpec(Hex.decodeHex(initVectorHex.toCharArray()));
        SecretKeySpec skeySpec = new SecretKeySpec(Hex.decodeHex(keyHex.toCharArray()), "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

        byte[] original = cipher.doFinal(Base64.decodeBase64(str));

        return new String(original);
    }

    @Override
    public String encrypt(Token token) {
        try {
            String strToken = mapper.writeValueAsString(token);
            return encryptAES(strToken);
        } catch (Exception e) {
            //TODO
            throw new RuntimeException(e);
        }
    }

    @Override
    public Token decrypt(String token) {
        try {
            String strToken = decryptAES(token);
            return mapper.readValue(strToken, Token.class);
        } catch (Exception e) {
            //TODO
            throw new RuntimeException(e);
        }
    }
}
package com.finalproject.schoolcalendar.helpers;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Fani on 11/13/13.
 */
public class Sha1Generator {

    public static String getSha1(String username, String password) {
        return generateSha1(username, password);
    }

    private static String generateSha1(String username, String password) {
        String toHash = username + password;
        String hash = null;

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            byte[] bytes = toHash.getBytes("UTF-8");
            digest.update(bytes, 0, bytes.length);
            bytes = digest.digest();

            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }

            hash = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return hash;
    }
}

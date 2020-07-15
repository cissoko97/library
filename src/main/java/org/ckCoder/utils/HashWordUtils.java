package org.ckCoder.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashWordUtils {

    public static String hashWord(String word) {
        StringBuffer buffer = new StringBuffer();
        try {
            MessageDigest md5 = MessageDigest.getInstance("SHA1");
            md5.update(word.getBytes());
            byte[] b = md5.digest();
            for (byte b1 : b) {
                buffer.append((b1 & 0xff));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }
}

package com.jimmy.development.tools;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DigestUtils {

    static MessageDigest getDigest(String algorithm) {
        try {
            return MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static byte[] md5(byte[] data) {
        return getMd5Digest().digest(data);
    }

    private static MessageDigest getMd5Digest() {
        return getDigest("MD5");
    }
}

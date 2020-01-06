package com.jerry.image_watcher.utils;

import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class EncryptionUtils {

    private static final String HMAC_SHA_512 = "HMACSHA512";
    private static final String HMAC_SHA_256 = "HmacSHA256";

    /**
     * 将加密后的字节数组转换成字符串
     *
     * @param b 字节数组
     * @return 字符串
     */
    private static String Hex(byte[] b) {
        StringBuilder hs = new StringBuilder();
        String stmp;
        for (int n = 0; b != null && n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0XFF);
            if (stmp.length() == 1)
                hs.append('0');
            hs.append(stmp);
        }
        return hs.toString().toLowerCase().trim();
    }

    /**
     * SHA512_HMAC加密
     *
     * @param secret  秘钥
     * @param message 消息
     * @return 加密后字符串
     */
    private static byte[] SHA512_HMAC(String secret, String message) {

        byte[] bytes;
        try {
            Mac SHA512_HMAC = Mac.getInstance(HMAC_SHA_512);
            SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), HMAC_SHA_512);
            SHA512_HMAC.init(secret_key);
            bytes = SHA512_HMAC.doFinal(message.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            System.out.println("Error HmacSHA512 ===========" + e.getMessage());
            bytes = new byte[0];
        }

        return bytes;
    }

    /**
     * SHA256_HMAC加密
     *
     * @param secret  秘钥
     * @param message 消息
     * @return 加密后字符串
     */
    private static byte[] SHA256_HMAC(String secret, String message) {

        byte[] bytes;
        try {
            Mac SHA256_HMAC = Mac.getInstance(HMAC_SHA_256);
            SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), HMAC_SHA_256);
            SHA256_HMAC.init(secret_key);
            bytes = SHA256_HMAC.doFinal(message.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            System.out.println("Error HmacSHA256 ===========" + e.getMessage());
            bytes = new byte[0];
        }

        return bytes;
    }

    private static String getBase64String(byte[] hash) {
        return Base64.encodeToString(hash, Base64.DEFAULT).trim();
    }

    /**
     * 使用标准URL Encode编码。注意和JDK默认的不同，空格被编码为%20而不是+。
     *
     * @param s String字符串
     * @return URL编码后的字符串
     */
    public static String urlEncode(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8").replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("UTF-8 encoding not supported!");
        }
    }

    public static String sha256Encrypt(String content) {
        return shaEncrypt("SHA-256", content);
    }

    /**
     * SHA加密
     *
     * @param strSrc 明文
     * @return 加密之后的密文
     */
    public static String shaEncrypt(String type, String strSrc) {
        MessageDigest md;
        String strDes;
        byte[] bt = strSrc.getBytes();
        try {
            md = MessageDigest.getInstance(type);// 将此换成SHA-1、SHA-256、SHA-512、SHA-384等参数
            md.update(bt);
            strDes = Hex(md.digest()); // to HexString
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        return strDes;
    }

}
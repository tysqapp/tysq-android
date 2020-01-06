package com.abc.lib_utils;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * author       : frog
 * time         : 2019-09-24 11:05
 * email        : xxxxx
 * desc         : 加密工具
 * version      : 1.0.0
 */

public class EncryptionUtils {

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
            if (stmp.length() == 1){
                hs.append('0');
            }
            hs.append(stmp);
        }
        return hs.toString().toLowerCase().trim();
    }

    // -----------------------------------------SHA 加密 start---------------------------------------

    /**
     * SHA512_HMAC加密
     */
    public static byte[] SHA512_HMAC(String secret, String message) {
        return SHA_HMAC(secret, message, HMAC_SHA.HMAC_SHA_512);
    }

    /**
     * SHA256_HMAC加密
     */
    public static byte[] SHA256_HMAC(String secret, String message) {
        return SHA_HMAC(secret, message, HMAC_SHA.HMAC_SHA_256);
    }

    /**
     * SHA_HMAC加密
     *
     * @param secret  秘钥
     * @param message 消息
     * @return 加密后字符串
     */
    public static byte[] SHA_HMAC(String secret, String message, String type) {

        byte[] bytes;
        try {
            Mac mac = Mac.getInstance(type);
            SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), type);
            mac.init(secret_key);
            bytes = mac.doFinal(message.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            System.out.println("Error " + type + " ===========" + e.getMessage());
            bytes = new byte[0];
        }

        return bytes;
    }

    public interface HMAC_SHA {
        String HMAC_SHA_512 = "HMACSHA512";
        String HMAC_SHA_256 = "HmacSHA256";
    }
    // -----------------------------------------SHA 加密 end----------------------------------------

    /**
     * base64 编码
     */
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

    // -----------------------------------------SHA 加密 start---------------------------------------
    public static String sha1Encrypt(String content) {
        return shaEncrypt(SHA.SHA_1, content);
    }

    public static String sha256Encrypt(String content) {
        return shaEncrypt(SHA.SHA_256, content);
    }

    public static String sha512Encrypt(String content) {
        return shaEncrypt(SHA.SHA_512, content);
    }

    public static String sha384Encrypt(String content) {
        return shaEncrypt(SHA.SHA_384, content);
    }

    /**
     * SHA加密
     *
     * @param type   加密类型 {@link EncryptionUtils.SHA}
     * @param strSrc 明文
     * @return 加密之后的密文
     */
    public static String shaEncrypt(String type, String strSrc) {
        MessageDigest md;
        String strDes;
        byte[] bt = strSrc.getBytes();
        try {
            // 将此换成SHA-1、SHA-256、SHA-512、SHA-384等参数
            md = MessageDigest.getInstance(type);
            md.update(bt);
            strDes = Hex(md.digest()); // to HexString
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        return strDes;
    }

    /**
     * SHA 的类型
     */
    public interface SHA {
        String SHA_1 = "SHA-1";
        String SHA_256 = "SHA-256";
        String SHA_512 = "SHA-512";
        String SHA_384 = "SHA-384";
    }
    // -----------------------------------------SHA 加密 end-----------------------------------------

    // -----------------------------------------SHA 加密 start---------------------------------------
    public static String md5(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            return (new BigInteger(1, md.digest())).toString(16);
        } catch (Exception var2) {
            var2.printStackTrace();
            return null;
        }
    }
    // -----------------------------------------SHA 加密 end-----------------------------------------
}
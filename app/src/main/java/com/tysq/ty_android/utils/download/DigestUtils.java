package com.tysq.ty_android.utils.download;

import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;

public class DigestUtils {

    /**
     * 比较 文件的md5 与 参数md5Compare 是否一致
     *
     * @param file       文件
     * @param md5Compare 比较的md5
     * @return
     */
    public static boolean checkFileMD5(File file, String md5Compare) {

        String md5 = getMd5ByFile(file);

        if (!TextUtils.isEmpty(md5.toLowerCase()) && md5.equals(md5Compare.toLowerCase()))
            return true;
        else
            return false;
    }

    public static String getMd5ByFile(File file) {
        if (!file.exists())
            return "";

        String value = null;
        try {
            FileInputStream in = new FileInputStream(file);

            try {
                MappedByteBuffer byteBuffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());

                MessageDigest md5 = MessageDigest.getInstance("MD5");
                md5.update(byteBuffer);

                BigInteger bi = new BigInteger(1, md5.digest());

                value = bi.toString(16);

                if (value.length() == 31) {
                    value = "0" + value;
                }

            } catch (Exception e) {
                Log.e(DigestUtils.class.getSimpleName(), e.getMessage());
            } finally {
                if (null != in) {
                    IOUtil.closeIO(in);
                }
            }
        } catch (FileNotFoundException e) {
            Log.e(DigestUtils.class.getSimpleName(), e.getMessage());
        }

        return value;
    }
}

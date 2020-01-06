package com.abc.lib_multi_download.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xxxx
 * @date 创建时间：2018/11/5
 * @description 时间格式类
 * 支持格式：
 * <p>
 * long -> String :{@link #_getDateStringViaTimeStamp}
 */

public class DateUtils {

    private static final String YYYY_MM_DD_HH_MM_SS_SSS = "yyyy-MM-dd HH:mm:ss.SSS";

    private static final Map<String, SimpleDateFormat> dateFormatMap = new HashMap<>();
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 获取 格式化的 format
     *
     * @param pattern 格式
     * @return
     */
    private static SimpleDateFormat _getDateFormat(String pattern) {
        SimpleDateFormat dateFormat = dateFormatMap.get(pattern);
        if (dateFormat == null) {
            dateFormat = new SimpleDateFormat(pattern);
            dateFormatMap.put(pattern, dateFormat);
        }
        return dateFormat;
    }

    /**
     * 获取 格式化时间 通过 时间戳
     * long -> String(指定的dateFormat格式)
     *
     * @param dateFormat 格式
     * @param timeStamp  13位的时间戳（含毫秒）
     * @return
     */
    private static String _getDateStringViaTimeStamp(String dateFormat, long timeStamp) {
        return _getDateFormat(dateFormat).format(timeStamp);
    }

    /**
     * @param timeStamp 13位的时间戳（含毫秒）
     * @return MM_DD_HH_MM
     */
    public static String getCurTime(long timeStamp) {
        return _getDateStringViaTimeStamp(YYYY_MM_DD_HH_MM_SS_SSS, timeStamp);
    }
    public static void main(String[] args) throws ParseException {

        long timeStamp = 1541406716848L;

        String time = getCurTime(timeStamp);

        System.out.println("Data:" + time + "===> String:"+time);

    }
}
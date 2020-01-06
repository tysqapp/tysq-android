package com.abc.lib_log;

import android.util.Log;

/**
 * author       : frog
 * time         : 2019-09-10 16:00
 * desc         : 日志工具
 * version      : 1.0.0
 */
public class JLogShowUtils {

    /**
     * 打印日志
     *
     * @param TAG     标记
     * @param content 内容
     * @param type    类型
     */
    public static void show(String TAG,
                            String content,
                            JLogShowType type) {

        int length = (JLogConfig.getInstance().getMaxLength() + 4)
                * JLogConfig.getInstance().getShowLine();

        int loopSize = content.length() / length;

        loopSize = content.length() % length > 0 ? loopSize + 1 : loopSize;

        for (int i = 0; i < loopSize; ++i) {

            int end = Math.min((i + 1) * length, content.length());

            String log = content.substring(i * length, end);

            _show(type, TAG, log);
        }

    }

    /**
     * 同步打印日志
     *
     * @param TAG     标记
     * @param content 日志内容
     * @param type    日志类型
     */
    public static synchronized void showSync(String TAG,
                                             String content,
                                             JLogShowType type) {
        int length = (JLogConfig.getInstance().getMaxLength() + 4)
                * JLogConfig.getInstance().getShowLine();

        int loopSize = content.length() / length;

        loopSize = content.length() % length > 0 ? loopSize + 1 : loopSize;

        for (int i = 0; i < loopSize; ++i) {

            int end = Math.min((i + 1) * length, content.length());

            String log = content.substring(i * length, end);

            _show(type, TAG, log);
        }

    }

    /**
     * 显示数据
     *
     * @param type    类型
     * @param tag     标记
     * @param content 内容
     */
    private static void _show(JLogShowType type,
                              String tag,
                              String content) {
        switch (type) {
            case INFO:
                Log.i(tag, tag + "\n" + content);
                break;
            case DEBUG:
                Log.d(tag, tag + "\n" + content);
                break;
            case VERBOSE:
                Log.v(tag, tag + "\n" + content);
                break;
            case WARN:
                Log.w(tag, tag + "\n" + content);
                break;
            case ERROR:
                Log.e(tag, tag + "\n" + content);
                break;
        }
    }

}

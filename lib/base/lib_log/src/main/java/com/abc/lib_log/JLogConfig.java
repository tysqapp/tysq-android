package com.abc.lib_log;

/**
 * author       : frog
 * time         : 2019-09-10 16:49
 * desc         : 日志配置
 * version      : 1.0.0
 */
public class JLogConfig {

    // 每行的字数
    private int maxLength;
    // 显示的行数
    private int showLine;
    // 调试
    private boolean debug;

    private static volatile JLogConfig instance = null;

    private JLogConfig() {
        maxLength = 130;
        showLine = 20;
        debug = true;
    }

    public static JLogConfig getInstance() {

        if (instance == null) {
            synchronized (JLogConfig.class) {
                if (instance == null) {
                    instance = new JLogConfig();
                }
            }
        }

        return instance;

    }

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public int getShowLine() {
        return showLine;
    }

    public void setShowLine(int showLine) {
        this.showLine = showLine;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }
}

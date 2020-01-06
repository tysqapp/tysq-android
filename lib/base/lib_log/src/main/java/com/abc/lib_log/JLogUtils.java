package com.abc.lib_log;


import java.util.ArrayList;
import java.util.List;

/**
 * author       : frog
 * time         : 2018-07-26 15:11
 * email        : xxxxx
 * desc         : 日志
 * version      : 1.0.0
 */

public class JLogUtils {

    private static final String TAG = JLogUtils.class.getSimpleName();

    private static final JLogUtils DEFAULT = new JLogUtils();

    //换行符
    private final static String CRLF = "\r\n";
    private final static String VERTICAL_LINE = "│";
    private final static String HORIZONTAL_LINE = "─";
    private final static String TOP_LEFT_LINE = "┌";
    private final static String BOTTOM_LEFT_LINE = "└";

    private final static String BOTTOM_RIGHT_LINE = "┘";
    private final static String TOP_RIGHT_LINE = "┓";

    private final static String MIDDLE_LINE = "├";
    private final static String SPACE_LINE = " ";

    //日志内容
    private List<String> logContent = new ArrayList<>();
    //日志行的类型
    private List<JLogType> logTypeList = new ArrayList<>();
    //当前内容
    private StringBuilder curString = new StringBuilder();

    private JLogUtils() {
    }

    public static JLogUtils getDefault() {
        if (!JLogConfig.getInstance().isDebug()) {
            return DEFAULT;
        }
        return new JLogUtils();
    }

    /**
     * 快速显示
     *
     * @param content 内容
     */
    public static void showInfoQuickly(String content) {

        if (!JLogConfig.getInstance().isDebug()) {
            return;
        }

        showInfoQuickly(TAG, content);
    }

    /**
     * 快速显示
     *
     * @param tag     标记
     * @param content 内容
     */
    public static void showInfoQuickly(String tag, String content) {

        if (!JLogConfig.getInstance().isDebug()) {
            return;
        }

        new JLogUtils()
                .add(content)
                .enterContent()
                .showInfo(tag);
    }

    /**
     * 快速显示
     *
     * @param content 内容
     */
    public static void showErrorQuickly(String content) {
        showErrorQuickly(TAG, content);
    }

    /**
     * 快速显示
     *
     * @param tag     标记
     * @param content 内容
     */
    public static void showErrorQuickly(String tag, String content) {

        if (!JLogConfig.getInstance().isDebug()) {
            return;
        }

        new JLogUtils()
                .add(content)
                .enterContent()
                .showError(tag);
    }

    /**
     * 添加内容（类型为内容，同时会追加上之前缓冲区的内容）
     *
     * @param content 内容
     */
    public JLogUtils content(String content) {
        if (!JLogConfig.getInstance().isDebug()) {
            return this;
        }

        curString.append(content);
        logContent.add(curString.toString());
        logTypeList.add(JLogType.CONTENT);

        curString = new StringBuilder();

        return this;
    }

    /**
     * 添加标题（类型为标题）
     *
     * @param title 标题
     */
    public JLogUtils title(String title) {
        if (!JLogConfig.getInstance().isDebug()) {
            return this;
        }

        logContent.add(title);
        logTypeList.add(JLogType.TITLE);

        return this;
    }

    /**
     * 添加参数（类型为参数，同时会追加上之前缓冲区的内容）
     *
     * @param param 参数
     */
    public JLogUtils param(String param) {
        if (!JLogConfig.getInstance().isDebug()) {
            return this;
        }

        curString.append(param);
        logContent.add(curString.toString());
        logTypeList.add(JLogType.PARAM);

        curString = new StringBuilder();

        return this;
    }

    /**
     * 将缓冲区的字符串置为 参数类型
     */
    public JLogUtils enterParam() {
        if (!JLogConfig.getInstance().isDebug()) {
            return this;
        }

        return enter(JLogType.PARAM);
    }

    /**
     * 将缓冲区的字符串置为 内容类型
     */
    public JLogUtils enterContent() {
        if (!JLogConfig.getInstance().isDebug()) {
            return this;
        }

        return enter(JLogType.CONTENT);
    }

    /**
     * 将缓冲区的字符串置为 对应的类型
     *
     * @param type 类型
     */
    private JLogUtils enter(JLogType type) {
        if (!JLogConfig.getInstance().isDebug()) {
            return this;
        }

        logContent.add(curString.toString());
        logTypeList.add(type);

        // 清空之前的日志
        curString = new StringBuilder();
        return this;
    }

    /**
     * 添加内容
     *
     * @param content 内容
     */
    public JLogUtils add(String content) {
        if (!JLogConfig.getInstance().isDebug()) {
            return this;
        }

        curString.append(content);
        return this;
    }

    /**
     * 添加内容
     *
     * @param content 内容
     */
    public JLogUtils add(int content) {
        if (!JLogConfig.getInstance().isDebug()) {
            return this;
        }

        curString.append(content);
        return this;
    }

    /**
     * 添加": "
     */
    public JLogUtils colon() {
        if (!JLogConfig.getInstance().isDebug()) {
            return this;
        }

        curString.append(": ");
        return this;
    }

    /**
     * 清空日志
     */
    public void clear() {
        if (!JLogConfig.getInstance().isDebug()) {
            return;
        }

        logContent.clear();
        logTypeList.clear();
        curString = new StringBuilder();
    }

    /**
     * 显示日志
     */
    public void showInfo() {
        if (!JLogConfig.getInstance().isDebug()) {
            return;
        }

        this.showInfo(TAG);
    }

    /**
     * 带标记的日志
     *
     * @param tag 标记
     */
    public void showInfo(String tag) {
        if (!JLogConfig.getInstance().isDebug()) {
            return;
        }

        JLogShowUtils.show(tag, getLog(), JLogShowType.INFO);
    }

    /**
     * 显示日志（错误）
     */
    public void showError() {
        if (!JLogConfig.getInstance().isDebug()) {
            return;
        }

        this.showError(TAG);
    }

    /**
     * 显示日志（错误）
     *
     * @param tag
     */
    public void showError(String tag) {
        if (!JLogConfig.getInstance().isDebug()) {
            return;
        }

        JLogShowUtils.show(tag, getLog(), JLogShowType.ERROR);
    }

    /**
     * 显示日志
     */
    public void showVerbose() {

        if (!JLogConfig.getInstance().isDebug()) {
            return;
        }

        this.showVerbose(TAG);
    }

    /**
     * 显示日志
     *
     * @param tag
     */
    public void showVerbose(String tag) {
        if (!JLogConfig.getInstance().isDebug()) {
            return;
        }

        JLogShowUtils.show(tag, getLog(), JLogShowType.VERBOSE);
    }

    /**
     * 显示日志
     */
    public void showDebug() {

        if (!JLogConfig.getInstance().isDebug()) {
            return;
        }

        this.showDebug(TAG);
    }

    /**
     * 显示日志
     *
     * @param tag
     */
    public void showDebug(String tag) {
        if (!JLogConfig.getInstance().isDebug()) {
            return;
        }

        JLogShowUtils.show(tag, getLog(), JLogShowType.ERROR);
    }

    /**
     * 显示日志
     */
    public void showWarn() {

        if (!JLogConfig.getInstance().isDebug()) {
            return;
        }

        this.showWarn(TAG);
    }

    /**
     * 显示日志
     *
     * @param tag
     */
    public void showWarn(String tag) {
        if (!JLogConfig.getInstance().isDebug()) {
            return;
        }

        JLogShowUtils.show(tag, getLog(), JLogShowType.WARN);
    }

    /**
     * 获取日志
     *
     * @return 日志内容
     */
    private String getLog() {

        if (!JLogConfig.getInstance().isDebug()) {
            return "";
        }

        StringBuilder result = new StringBuilder();
        addEndLine(result, true);

        int maxLength = JLogConfig.getInstance().getMaxLength();

        for (int i = 0; i < logContent.size(); ++i) {

            String tempLog = logContent.get(i);
            int logLen = tempLog.length();

            switch (logTypeList.get(i)) {
                case TITLE:

                    int titleMaxLength = maxLength - 8;

                    if (logLen > titleMaxLength) {
                        return "日志的标题长度过长！！！！";
                    }

                    result.append(VERTICAL_LINE);

                    int placeLength = maxLength - logLen;

                    //画左侧"======="
                    for (int j = 0; j < placeLength / 2 - 4; ++j) {
                        result.append("=");
                    }

                    //画左侧四个" "
                    result.append("    ");

                    //画内容
                    result.append(tempLog);

                    //画右侧四个" "
                    result.append("    ");

                    //画右侧"======="
                    for (int j = 0; j < (placeLength - placeLength / 2 - 4); ++j) {
                        result.append("=");
                    }

                    result.append(VERTICAL_LINE);
                    result.append(CRLF);

                    break;

                case PARAM:
                    int paramMaxLength = maxLength - 3;
                    boolean isTheLast = false;

                    for (int j = 0; j < logLen / paramMaxLength + 1; ++j) {
                        result.append(VERTICAL_LINE);

                        //增加连接
                        if (j == 0) {

                            //第一个元素 或 前一个元素不是LogType.PARAM 则使用 TOP_LEFT_LINE
                            if (i == 0 || !logTypeList.get(i - 1).equals(JLogType.PARAM)) {
                                result.append(SPACE_LINE).append(TOP_LEFT_LINE).append(SPACE_LINE);
                                //最后一个元素 或 下一个不是LogType.PARAM 则使用 BOTTOM_LEFT_LINE

                            } else if ((i + 1) == logContent.size()
                                    || !logTypeList.get(i + 1).equals(JLogType.PARAM)) {
                                result.append(SPACE_LINE).append(BOTTOM_LEFT_LINE).append(SPACE_LINE);
                                isTheLast = true;

                            } else {
                                result.append(SPACE_LINE).append(MIDDLE_LINE).append(SPACE_LINE);
                            }

                        } else {
                            if (!isTheLast) {
                                result.append(SPACE_LINE).append(VERTICAL_LINE).append(SPACE_LINE);
                            } else {
                                result.append(SPACE_LINE).append(SPACE_LINE).append(SPACE_LINE);
                            }
                        }

                        int start = j * paramMaxLength;

                        int tempEnd = (j + 1) * paramMaxLength;
                        int end = Math.min(tempLog.length(), tempEnd);

                        result.append(tempLog, start, end);

                        if ((end < tempEnd) && j == logLen / paramMaxLength) {
                            for (int e = 0; e < tempEnd - end; ++e) {
                                result.append(" ");
                            }
                        }

                        result.append(VERTICAL_LINE);
                        result.append(CRLF);
                    }
                    break;

                case CONTENT:

                    for (int j = 0; j < logLen / JLogConfig.getInstance().getMaxLength() + 1; ++j) {
                        result.append(VERTICAL_LINE);

                        int start = j * JLogConfig.getInstance().getMaxLength();

                        int tempEnd = (j + 1) * JLogConfig.getInstance().getMaxLength();
                        int end = Math.min(tempLog.length(), tempEnd);

                        result.append(tempLog, start, end);

                        if ((end < tempEnd) && j == logLen / JLogConfig.getInstance().getMaxLength()) {
                            for (int e = 0; e < tempEnd - end; ++e) {
                                result.append(" ");
                            }
                        }

                        result.append(VERTICAL_LINE);
                        result.append(CRLF);
                    }

                    break;
            }

        }

        addEndLine(result, false);


        return result.toString();
    }

    private void addEndLine(StringBuilder result, boolean isTop) {

        if (!JLogConfig.getInstance().isDebug()) {
            return;
        }

        result.append(isTop ? TOP_LEFT_LINE : BOTTOM_LEFT_LINE);

        for (int i = 0; i < JLogConfig.getInstance().getMaxLength(); ++i) {
            result.append(HORIZONTAL_LINE);
        }

        result.append(isTop ? TOP_RIGHT_LINE : BOTTOM_RIGHT_LINE);

        result.append(CRLF);
    }

}

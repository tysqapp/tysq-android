package utils;

/**
 * author       : frog
 * time         : 2019/6/13 上午10:57
 * desc         : 错误码常量
 * version      : 1.3.0
 */
public class ErrorConstant {

    // 没找到文件
    public static final int NOT_FIND_FILE = 1201;
    // hash 出错
    public static final int HASH_FAILURE = 1202;
    // 查询文件是否存在 请求失败
    public static final int FILE_INFO_CHECK_FAILURE = 1203;
    // 解析成功分片下标 出错
    public static final int PARSER_SUC_INDEX_FAILURE = 1204;
    // 文件合并失败
    public static final int FILE_MERGE_FAILURE = 1205;

    // 文件创建失败
    public static final int FILE_CREATE_ERROR = 1501;
    // 文件复制失败
    public static final int FILE_COPY_ERROR = 1502;
    // 文件请求失败
    public static final int FILE_REQUEST_ERROR = 1503;

}

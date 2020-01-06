package com.tysq.ty_android.config;

import com.tysq.ty_android.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author       : frog
 * time         : 2019-07-17 14:10
 * desc         : 常量
 * version      : 1.3.0
 */
public class Constant {

    /**
     * 发布文章：create_article ,
     * 阅读文章：read_article，
     * 评论文章：comment_article
     */
    public interface JudgementType {
        String CREATE = "create_article";
        String READ = "read_article";
        String COMMENT = "comment_article";
        String DOWNLOAD_VIDEO = "download_video";
        String GRADE = "grade";
        String GRADE_TO_RANK = "grade_to_rank";
    }

    /**
     * -1: 未转码
     * 1:  已转码
     * 0: mp4 审核
     */
    public interface VideoStatus {
        int ENCODE_OVER = 1;
        int ENCODE_WAIT = -1;
        int EXAM_MP4 = 0;
    }

    /**
     * 邮箱验证状态码
     * -1 为未验证，1 为验证
     */
    public interface EmailValidateStatus {
        // 邮箱验证成功
        int EMAIL_VERIFY_SUC = 1;
        // 邮箱未验证
        int EMAIL_VERIFY_NONE = -1;
    }

    // 等级
    public static final HashMap<Integer, Integer> LV_MAP = new HashMap<>();
    public static final HashMap<Integer, Integer> LV_MAP_STRING = new HashMap<>();
    public static final int DEFAULT_GRADE = 1;

    public static final float MULTIPLE = 2.6f;
    public static final int ARTICLE_LIST_SIZE = 20;
    public static final int ARTICLE_ADVERTISEMENT_POS = 6;

    //一篇置顶文章高度
    public static final int TOP_ARTICLE_HEIGHT = 30;
    //打赏列表item的个数
    public static final int REWARD_ITEM_NUM = 5;

    static {
        LV_MAP.put(1, R.drawable.ic_lv_1);
        LV_MAP.put(2, R.drawable.ic_lv_2);
        LV_MAP.put(3, R.drawable.ic_lv_3);
        LV_MAP.put(4, R.drawable.ic_lv_4);
        LV_MAP.put(5, R.drawable.ic_lv_5);
        LV_MAP.put(6, R.drawable.ic_lv_6);
        LV_MAP.put(7, R.drawable.ic_lv_7);
        LV_MAP.put(8, R.drawable.ic_lv_8);
        LV_MAP.put(9, R.drawable.ic_lv_9);
        LV_MAP.put(10, R.drawable.ic_lv_10);
        LV_MAP.put(11, R.drawable.ic_lv_11);
        LV_MAP.put(12, R.drawable.ic_lv_12);
        LV_MAP.put(13, R.drawable.ic_lv_13);
        LV_MAP.put(14, R.drawable.ic_lv_14);
        LV_MAP.put(15, R.drawable.ic_lv_15);
        LV_MAP.put(16, R.drawable.ic_lv_16);
        LV_MAP.put(17, R.drawable.ic_lv_17);
        LV_MAP.put(18, R.drawable.ic_lv_18);
    }

    static {
        LV_MAP_STRING.put(1, R.string.lv_1);
        LV_MAP_STRING.put(2, R.string.lv_2);
        LV_MAP_STRING.put(3, R.string.lv_3);
        LV_MAP_STRING.put(4, R.string.lv_4);
        LV_MAP_STRING.put(5, R.string.lv_5);
        LV_MAP_STRING.put(6, R.string.lv_6);
        LV_MAP_STRING.put(7, R.string.lv_7);
        LV_MAP_STRING.put(8, R.string.lv_8);
        LV_MAP_STRING.put(9, R.string.lv_9);
        LV_MAP_STRING.put(10, R.string.lv_10);
        LV_MAP_STRING.put(11, R.string.lv_11);
        LV_MAP_STRING.put(12, R.string.lv_12);
        LV_MAP_STRING.put(13, R.string.lv_13);
        LV_MAP_STRING.put(14, R.string.lv_14);
        LV_MAP_STRING.put(15, R.string.lv_15);
        LV_MAP_STRING.put(16, R.string.lv_16);
        LV_MAP_STRING.put(17, R.string.lv_17);
        LV_MAP_STRING.put(18, R.string.lv_18);
    }

    // 订单状态
    public static final HashMap<Integer, Integer> ORDER_STATUS_TEXT = new HashMap<>();
    public static final HashMap<Integer, Integer> ORDER_STATUS_COLOR = new HashMap<>();

    static {
        ORDER_STATUS_TEXT.put(1, R.string.order_wait_for_pay);
        ORDER_STATUS_TEXT.put(3, R.string.order_paid_already_accounted);


        ORDER_STATUS_COLOR.put(1, R.color.main_blue_color);
        ORDER_STATUS_COLOR.put(3, R.color.green_color);

    }

    /**
     * html 回调原生的数据类型
     */
    public interface HtmlType {
        // 更换标题
        String TITLE = "title";
        // 关闭页面
        String CLOSE_PAGE = "close_action";
        // 开新页面
        String OPEN_NEW_LINK = "linkAction";
    }

    public static class HtmlUrl {
        // 首页广告
        private static final String HOME_ADVERTISEMENT = "html5/activityPage/app_home.html";
        // 搜索页广告
        private static final String SEARCH_ADVERTISEMENT = "html5/activityPage/app_search.html";
        // 文章详情页面广告
        private static final String DETAIL_ADVERTISEMENT = "html5/activityPage/app_articleInfo.html";

        //首页广告
        public static String HOME_AD;
        //搜索页广告
        public static String SEARCH_AD;
        //文章详情广告
        public static String DETAIL_AD;

        public static void initHtml(String domain) {
            HOME_AD = domain + HOME_ADVERTISEMENT;
            SEARCH_AD = domain + SEARCH_ADVERTISEMENT;
            DETAIL_AD = domain + DETAIL_ADVERTISEMENT;
        }
    }

    /**
     * HTML 中使用的 api
     */
    public static class HtmlAPI {
        // 用户协议
        private static final String USER_PROTOCOL_SUFFIX = "api/pages/agreement";

        // 购买积分
        private static final String EXCHANGE_URL_SUFFIX = "api/pages/buy_score";
        // 购买金币
        private static final String COIN_URL_SUFFIX = "api/pages/buy_coin";

        // 下载App页
        private static final String DOWNLOAD_SUFFIX = "api/pages/download_app";

        // 积分说明
        private static final String RANK_DEFAULT_SUFFIX = "api/pages/score_coin_guide/0";
        // 等级说明
        private static final String GRAND_SUFFIX = "api/pages/score_coin_guide/1";
        // 金币说明
        private static final String COIN_SUFFIX = "api/pages/score_coin_guide/2";

        // 推广挣钱
        private static final String PROMOTE_EARNING_SUFFIX = "api/pages/promotion";

        // 邀请好友页
        private static final String INVITE_URL_SUFFIX = "api/pages/invite_friend";

        // 购买积分
        public static String EXCHANGE_URL;
        // 购买金币
        public static String COIN_URL;
        // 用户协议
        public static String USER_PROTOCOL;
        // 下载App
        public static String DOWNLOAD;
        // 积分说明
        public static String RANK_DETAIL;
        // 等级说明
        public static String GRAND;
        // 金币说明
        public static String COIN;
        // 推广挣钱
        public static String PROMOTE_EARNING;
        // 邀请好友页
        public static String INVITE_URL;

        public static void initHtml(String domain) {
            EXCHANGE_URL = domain + EXCHANGE_URL_SUFFIX;
            COIN_URL = domain + COIN_URL_SUFFIX;
            USER_PROTOCOL = domain + USER_PROTOCOL_SUFFIX;
            DOWNLOAD = domain + DOWNLOAD_SUFFIX;
            RANK_DETAIL = domain + RANK_DEFAULT_SUFFIX;
            GRAND = domain + GRAND_SUFFIX;
            COIN = domain + COIN_SUFFIX;
            PROMOTE_EARNING = domain + PROMOTE_EARNING_SUFFIX;
            INVITE_URL = domain + INVITE_URL_SUFFIX;
        }
    }

    public interface RegisterType {
        // 自主注册
        int DIY = 0;
        // 邀请码注册
        int CODE = 1;
        // 邀请链接注册
        int URL = 2;
        // 扫描二维码注册
        int SCAN = 3;
    }

    /**
     * 邀请类型
     */
    public interface ClientType {
        int PC = 1;
        int IOS = 2;
        int ANDROID = 3;
    }

    /**
     * 收藏类型
     */
    public interface CollectType {
        // 取消
        int CANCEL = 1;
        // 收藏
        int COLLECT = 2;
    }

    /**
     * 验证码的类型
     */
    public interface CaptchaType {
        // 登陆
        String LOGIN = "login";
        // 文章
        String ARTICLE = "article";
        // 注册
        String REGISTER = "register";
        // 修改密码
        String PASSWORD = "password";
        // 绑定邮箱
        String BIND_EMAIL = "bindEmail";
        // 重置密码
        String RESET_PASSWORD = "resetPassword";
    }

    /**
     * 1:审核中
     * 2:平台审核驳回
     * 3:钱包审核中
     * 4：钱包审核失败
     * 5.钱包审核通过，未转账
     * 6.钱包已转账
     */
    public static final HashMap<Integer, Integer> WITHDRAW_STATUS_TEXT = new HashMap<>();
    public static final HashMap<Integer, Integer> WITHDRAW_STATUS_COLOR = new HashMap<>();

    static {
        WITHDRAW_STATUS_TEXT.put(1, R.string.withdraw_under_review);
        WITHDRAW_STATUS_TEXT.put(2, R.string.withdraw_refuse);
        WITHDRAW_STATUS_TEXT.put(3, R.string.withdraw_under_review);
        WITHDRAW_STATUS_TEXT.put(4, R.string.withdraw_refuse);
        WITHDRAW_STATUS_TEXT.put(5, R.string.withdraw_wait_for_arrived);
        WITHDRAW_STATUS_TEXT.put(6, R.string.withdraw_over);

        WITHDRAW_STATUS_COLOR.put(1, R.color.main_blue_color);
        WITHDRAW_STATUS_COLOR.put(2, R.color.red);
        WITHDRAW_STATUS_COLOR.put(3, R.color.main_blue_color);
        WITHDRAW_STATUS_COLOR.put(4, R.color.red);
        WITHDRAW_STATUS_COLOR.put(5, R.color.main_blue_color);
        WITHDRAW_STATUS_COLOR.put(6, R.color.green_color);
    }

    /**
     * 绑定邮箱：bindEmail
     * 找回密码：resetPassword
     * 用户提币：drawCurrency
     */
    public interface EmailCaptchaType {
        String BIND_EMAIL = "bindEmail";
        String RESET_PASSWORD = "resetPassword";
        String DRAW_CURRENCY = "drawCurrency";
    }

    /**
     * 最热: 2
     * 最新: 1
     * 综合: 0
     */
    public interface SortType {
        int COMPOSITE = 0;
        int NEW = 1;
        int HOT = 2;
    }

    public static final List<Integer> BANNER_BG_RES = new ArrayList<>();

    static {
        BANNER_BG_RES.add(R.drawable.img_banner_blue_bg);
        BANNER_BG_RES.add(R.drawable.img_banner_green_bg);
        BANNER_BG_RES.add(R.drawable.img_banner_orange_bg);
    }

    /**
     * 更新类型
     */
    public interface UpdateType {
        // 不需操作
        int NONE = 0;
        // 普通更新
        int ORDINARY = 1;
        // 强制更新
        int FORCE = 2;
    }

    /**
     * 公告类别:
     * 1：公告栏显示
     * 2：弹框显示
     * 3：都显示
     */
    public interface AnnouncementType {
        int BANNER = 1;
        int DIALOG = 2;
        int ALL = 3;
    }

    /**
     * 我的文章请求类型
     * 文章状态：
     * 1：已发布
     * 2：草稿
     * -1：待审核
     * -2：审核未通过
     * -3,已删除
     * -4,已隐藏
     */
    public interface MyArticleType {
        int ALL = 0;
        int PUBLISH = 1;
        int DRAFT = 2;
        int EXAM = -1;
        int REFUSE = -2;
        int DELETE = -3;
        int HIDE = -4;
    }

    public static final Map<Integer, Integer> MY_ARTICLE_TYPE_NAME = new HashMap<>();

    static {
        MY_ARTICLE_TYPE_NAME.put(MyArticleType.ALL, R.string.my_article_all);
        MY_ARTICLE_TYPE_NAME.put(MyArticleType.DRAFT, R.string.my_article_draft);
        MY_ARTICLE_TYPE_NAME.put(MyArticleType.EXAM, R.string.my_article_exam);
        MY_ARTICLE_TYPE_NAME.put(MyArticleType.PUBLISH, R.string.my_article_publish);
        MY_ARTICLE_TYPE_NAME.put(MyArticleType.REFUSE, R.string.my_article_refuse);
        MY_ARTICLE_TYPE_NAME.put(MyArticleType.HIDE, R.string.my_article_hide);
    }

    public interface ReviewStatus {
        int PASS = 1;
        int REFUSE = -2;
    }

    /**
     * http类型
     */
    public interface HttpType {
        String HTTP = "http://";
        String HTTPS = "https://";
    }

    /**
     * 文章类型
     * <p>
     * image：图片
     * video：视频
     * 为空：文本
     */
    public interface ArticleType {
        String IMAGE = "image";
        String VIDEO = "video";
    }

    /**
     * 通知类型
     */
    public interface NotificationType {
        String REVIEW_PASS = "review_pass";
        String REVIEW_UNPASS = "review_unpass";
        String ARTICLE_REVIEW = "article_review";
        String ARTICLE_NEW_COMMENT = "article_new_comment";
        String COMMENT_NEW_REPLY = "comment_new_reply";
        String NEW_REPORT_HANDLER = "new_report_handler";
        String REPORT_HANDLER = "report_handler";
        String INVALID_REPORT_HANDLER = "invalid_report_handler";
        String EFFECTIVE_REPORT_HANDLER = "effective_report_handler";
        String DELETE_CATEGORY = "delete_category";
    }

    public interface HomePageSearchLabel {
        String LABEL_ARTICLE = "文章";
        String LABEL_TAG = "标签";
        String LABEL_ADMIN = "用户";
    }

    public interface HomePageSearchType {
        String TYPE_ARTICLE = "title";
        String TYPE_TAG = "label";
        String TYPE_ADMIN = "user";
    }

    /**
     * 本地数据库存储条数
     */
    public static final int DATABASE_NUM = 20;
    public static final int LOCAL_LABEL_NUM = 10;

    /**
     * 判断文章状态是否为隐藏
     */
    public interface JudgeHideArticle {
        String HIDE_ARTICLE = "已隐藏";
    }

    /**
     * 1:本地存储;
     * 2:阿里云存储;
     * 3:aws存储;
     */
    public interface STORAGE {
        int LOCAL = 1;
        int ALI = 2;
        int AWS = 3;
    }

    /**
     * 广告位的数量
     */
    public interface AdvertisementNum {
        int HOME_AD = 3;
        int DETAIL_AD = 1;
    }

    /**
     * 打赏文章金额
     */
    public interface ArticleRewardNum {
        int REWARD_100 = 100;
        int REWARD_200 = 200;
        int REWARD_500 = 500;
        int REWARD_1000 = 1000;
        int REWARD_2000 = 2000;
    }
}

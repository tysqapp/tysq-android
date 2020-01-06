package com.tysq.ty_android.utils;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bit.view.activity.BitBaseActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.luck.picture.lib.tools.ScreenUtils;
import com.tysq.ty_android.BuildConfig;
import com.tysq.ty_android.R;
import com.tysq.ty_android.app.TyApplication;
import com.tysq.ty_android.config.Constant;
import com.tysq.ty_android.config.TyConfig;
import com.tysq.ty_android.feature.articleDetail.activity.ArticleDetailActivity;
import com.tysq.ty_android.feature.invite.InviteActivity;
import com.tysq.ty_android.feature.web.RouterMap;
import com.tysq.ty_android.feature.web.TyWebViewActivity;
import com.tysq.ty_android.local.sp.UserCache;
import com.tysq.ty_android.net.config.NetConfig;
import com.tysq.ty_android.net.cookie.PersistentCookieStore;
import com.tysq.ty_android.widget.IconBorderTextSpan;
import com.tysq.ty_android.widget.IconTextSpan;
import com.zinc.lib_jerry_editor.config.glide.GlideOptions;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import exception.ParserUrlException;
import okhttp3.Cookie;

/**
 * author       : frog
 * time         : 2019/5/20 下午3:03
 * desc         :
 * version      : 1.3.0
 */
public class TyUtils {

    private final static String DOT = "\\.";
    private final static String NONE = null;

    private static final String JUST = "刚刚";
    private static final String MIN = "%1$s分钟前";
    private static final String HOUR = "%1$s小时前";
    private static final String DAY = "%1$s天前";
    private static final String MONTH = "%1$s个月前";
    private static final String YEAR = "%1$s年前";

    // 10分钟
    private static final long TIME_MIN = 10 * 60;
    // 1小时
    private static final long ONE_HOUR = 60 * 60;
    // 1天
    private static final long ONE_DAY = 24 * 60 * 60;
    // 1个月
    private static final long ONE_MONTH = 30 * 24 * 60 * 60;
    // 1年
    private static final long ONE_YEAR = 365 * 24 * 60 * 60;

    private static final long EIGHT_HOURS = 8 * 60 * 60;

    public static String getPublishTime(long curTime, long createTime) {
        String result;
        long time = curTime - createTime;

        if (time <= TIME_MIN) {
            result = JUST;
        } else if (time <= ONE_HOUR) {
            result = String.format(MIN, time / 60);
        } else if (time <= ONE_DAY) {
            result = String.format(HOUR, time / ONE_HOUR);
        } else if (time <= ONE_MONTH) {
            result = String.format(DAY, time / ONE_DAY);
        } else if (time <= ONE_YEAR) {
            result = String.format(MONTH, time / ONE_MONTH);
        } else {
            result = String.format(YEAR, time / ONE_YEAR);
        }

        return result;
    }

    /**
     * 获取发布时间
     * <p>
     * 1、10分钟内的显示：刚刚
     * 2、超过10分钟小于1小时的显示：X分钟前
     * 3、超过1小时小于1天的显示：X小时前
     * 4、超过1天小于一个月的显示：X天前
     * 5、超过1个月小于一年的显示：X个月前
     * 6、超过1年的显示：X年前
     */
    public static String getPublishTime(long createTime) {
        return getPublishTime(System.currentTimeMillis() / 1000, createTime);
    }

    /**
     * 填充用户头像
     */
    public static void initUserPhoto(Fragment fragment,
                                     Context context,
                                     String url,
                                     ImageView imageView) {
        GlideOptions options = GlideOptions
                .bitmapTransform(new CircleCrop())
                .override(imageView.getWidth())
                .error(R.drawable.placeholder_user_photo)
                .placeholder(R.drawable.placeholder_user_photo);

        TyUtils.getGlideRequest(fragment, context, url, options, imageView);
    }

    /**
     * 填充用户头像
     */
    public static void initUserPhoto(android.support.v4.app.Fragment fragment,
                                     Context context,
                                     String url,
                                     ImageView imageView) {
        GlideOptions options = GlideOptions
                .bitmapTransform(new CircleCrop())
                .override(imageView.getWidth())
                .error(R.drawable.placeholder_user_photo)
                .placeholder(R.drawable.placeholder_user_photo);

        TyUtils.getGlideRequest(fragment, context, url, options, imageView);
    }

    public static void initUserPhoto(BitBaseActivity activity,
                                     Context context,
                                     String url,
                                     ImageView imageView) {
        GlideOptions options = GlideOptions
                .bitmapTransform(new CircleCrop())
                .override(imageView.getWidth())
                .error(R.drawable.placeholder_user_photo)
                .placeholder(R.drawable.placeholder_user_photo);

        TyUtils.getGlideRequest(activity, context, url, options, imageView);
    }

    /**
     * 格式化数字
     *
     * @param count
     * @return
     */
    public static String formatNum(long count) {
        if (count <= 0) {
            return "0";
        }

        if (count < 10000) {
            return count + "";
        }

        // 取万以上整数
        long i = count / 10000;
        // 取千位
        long j = count / 1000 - i * 10;

        return i + "." + j + "w+";

    }

    /**
     * 判断用户id 是否有可控权限
     *
     * @param userId 用户id
     * @return true 可以控制
     * false 不可控
     */
    public static boolean isCanControl(int userId) {
        if (UserCache.isEmpty()) {
            return false;
        }

        if (UserCache.getDefault().getAccountId() == userId) {
            return true;
        }

        return false;
    }

    public final static String IMAGE = "image";
    public final static String VIDEO = "video";
    public final static String AUDIO = "audio";
    public final static String OTHER = "other";

    public static final Map<String, Integer> CLOUD_TYPE_IMG = new HashMap<>();

    private static final String[] VIDEO_ARRAY = new String[]{
            "AVI", "ASF", "WMV", "AVS", "FLV",
            "MKV", "MOV", "3GP", "MP4", "MPG",
            "MPEG", "DAT", "OGM", "VOB", "RM",
            "RMVB", "TS", "TP", "IFO", "NSV"
    };

    private static final String[] AUDIO_ARRAY = new String[]{
            "MP3", "AAC", "WAV", "WMA", "CDA",
            "FLAC", "M4A", "MID", "MKA", "MP2",
            "MPA", "MPC", "APE", "OFR", "OGG",
            "RA", "WV", "TTA", "AC3", "DTS"
    };

    private static final String[] IMAGE_ARRAY = new String[]{
            "jpg", "bmp", "eps", "gif", "mif",
            "miff", "png", "tif", "tiff", "svg",
            "wmf", "jpe", "jpeg", "dib", "ico",
            "tga", "cut", "pic"
    };

    static {
        CLOUD_TYPE_IMG.put("doc", R.drawable.ic_file_word);
        CLOUD_TYPE_IMG.put("docx", R.drawable.ic_file_word);

        CLOUD_TYPE_IMG.put("xls", R.drawable.ic_file_excel);
        CLOUD_TYPE_IMG.put("xlsx", R.drawable.ic_file_excel);

        CLOUD_TYPE_IMG.put("ppt", R.drawable.ic_file_ppt);
        CLOUD_TYPE_IMG.put("pptx", R.drawable.ic_file_ppt);

        CLOUD_TYPE_IMG.put("zip", R.drawable.ic_file_zip);
        CLOUD_TYPE_IMG.put("rar", R.drawable.ic_file_zip);

        CLOUD_TYPE_IMG.put(OTHER, R.drawable.ic_file_other);

        CLOUD_TYPE_IMG.put(IMAGE, R.drawable.ic_file_img);
        for (String item : IMAGE_ARRAY) {
            CLOUD_TYPE_IMG.put(item.toUpperCase(), R.drawable.ic_file_img);
            CLOUD_TYPE_IMG.put(item.toLowerCase(), R.drawable.ic_file_img);
        }

        CLOUD_TYPE_IMG.put(AUDIO, R.drawable.ic_file_audio);
        for (String item : AUDIO_ARRAY) {
            CLOUD_TYPE_IMG.put(item.toUpperCase(), R.drawable.ic_file_audio);
            CLOUD_TYPE_IMG.put(item.toLowerCase(), R.drawable.ic_file_audio);
        }

        CLOUD_TYPE_IMG.put(VIDEO, R.drawable.ic_file_video);
        for (String item : VIDEO_ARRAY) {
            CLOUD_TYPE_IMG.put(item.toUpperCase(), R.drawable.ic_file_video);
            CLOUD_TYPE_IMG.put(item.toLowerCase(), R.drawable.ic_file_video);
        }

    }

    public static final Map<String, Integer> CLOUD_TYPE = new HashMap<>();

    public static final int IMAGE_TYPE = 0;
    public static final int VIDEO_TYPE = 1;
    public static final int AUDIO_TYPE = 2;
    public static final int OTHER_TYPE = 3;

    static {
        CLOUD_TYPE.put(IMAGE, IMAGE_TYPE);
        CLOUD_TYPE.put(VIDEO, VIDEO_TYPE);
        CLOUD_TYPE.put(AUDIO, AUDIO_TYPE);
        CLOUD_TYPE.put(OTHER, OTHER_TYPE);
    }

    public static String formatFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileS <= 0) {
            return wrongSize;
        }
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }

    public static String formatProcessTime(long time) {
        time = time / 1000;

        long sec = time % 60;
        long min = time / 60;

        String secString;
        if (sec < 10) {
            secString = "0" + sec;
        } else {
            secString = "" + sec;
        }

        String minString;
        if (min < 10) {
            minString = "0" + min;
        } else {
            minString = "" + min;
        }

        return minString + ":" + secString;

    }

    /**
     * 获取后缀
     *
     * @param fileName 文件名称
     */
    public static String clipTheType(String fileName) {
        String[] split = fileName.split(DOT);
        if (split.length <= 0) {
            return NONE;
        }

        return split[split.length - 1];
    }

    public static void goToOuterBrowser(String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }

        Uri content_url = Uri.parse(url);

        Intent intent = new Intent();

        intent.setAction("android.intent.action.VIEW");
        intent.setData(content_url);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        TyApplication.getContext().startActivity(intent);
    }

    private static final DecimalFormat df = new DecimalFormat("#,###");

    /**
     * 格式化数字：xxx,xxx,xxx
     */
    public static String formatDotNum(long data) {
        return df.format(data);
    }

    /**
     * 获取宽度
     *
     * @param realWidth 图片的宽
     * @return 真正可用的宽
     */
    public static float getWidth(float realWidth, float padding) {
        float screenWidth = TyConfig.getScreenWidth() - padding * 2;
        if (realWidth > screenWidth) {
            return screenWidth;
        } else {
            return realWidth;
        }
    }

    /**
     * @param picWidth    图片的宽
     * @param picHeight   图片的高
     * @param resultWidth 真实的宽
     * @return
     */
    public static float getHeight(float picWidth,
                                  float picHeight,
                                  float resultWidth) {

        if (resultWidth == picWidth) {
            return picHeight;
        }

        return resultWidth * picHeight / picWidth;

    }

    private static final String BASE64_SPLIT = ";base64,";

    /**
     * @param base64
     * @return
     */
    public static String clipBase64(String base64) {
        int index = base64.indexOf(BASE64_SPLIT);
        return index <= 0 ? base64 : base64.substring(index + BASE64_SPLIT.length());
    }


    /**
     * 处理 webview 传来的链接
     */
    public static void handleWebViewLink(Context context, String url) {

        // 邀请页面
        if (RouterMap.INVITE.equals(url)) {
            InviteActivity.startActivity(context);
            return;
        }

        // 是否为文章链接
        String articleUrl = NetConfig.ARTICLE_URL;
        if (url.startsWith(articleUrl)) {
            String articleId = url.replace(articleUrl, "");

            if (TextUtils.isEmpty(articleId)) {
                return;
            }

            ArticleDetailActivity.startActivity(context, articleId);
            return;
        }

        if (url.startsWith("/api/")) {
            url = url.replaceFirst("/", "");

            url = NetConfig.getBaseUrlWithoutVersion() + url;

        }

        TyWebViewActivity.startActivity(context, url);

    }

    public static void getGlideRequest(Activity activity,
                                       Context context,
                                       String url,
                                       RequestOptions options,
                                       ImageView imageView) {
        if (activity == null || activity.isFinishing()) {
            Log.e("Glide destroy", "getGlideRequest");
            return;
        }
        getGlideRequest(context, url, options, imageView);
    }

    public static void getGlideRequest(Fragment fragment,
                                       Context context,
                                       String url,
                                       RequestOptions options,
                                       ImageView imageView) {
        Activity activity = fragment.getActivity();

        if (activity == null || activity.isFinishing()) {
            Log.e("Glide destroy", "getGlideRequest");
            return;
        }

        getGlideRequest(context, url, options, imageView);
    }

    public static void getGlideRequest(android.support.v4.app.Fragment fragment,
                                       Context context,
                                       String url,
                                       RequestOptions options,
                                       ImageView imageView) {
        FragmentActivity activity = fragment.getActivity();

        if (activity == null || activity.isFinishing()) {
            Log.e("Glide destroy", "getGlideRequest");
            return;
        }

        getGlideRequest(context, url, options, imageView);
    }

    private static void getGlideRequest(Context context,
                                        String url,
                                        RequestOptions options,
                                        ImageView imageView) {

        RequestBuilder<Drawable> requestBuilder;

        List<Cookie> cookies = PersistentCookieStore.getInstance().getCookies();
        if (TextUtils.isEmpty(url)) {
            requestBuilder = Glide.with(context).load(url);
        } else if (cookies == null || cookies.size() <= 0) {
            requestBuilder = Glide.with(context).load(url);
        } else {
            LazyHeaders.Builder builder = new LazyHeaders.Builder();
            for (int i = 0; i < cookies.size(); i++) {
                Cookie cookie = cookies.get(i);
                String value = cookie.name() + "=" + cookie.value();
                builder.setHeader("Cookie", value);
            }

            GlideUrl glideUrl = new GlideUrl(url, builder.build());

            requestBuilder = Glide.with(context).load(glideUrl);
        }

        if (options != null) {
            requestBuilder.apply(options);
        }

        requestBuilder.into(imageView);

    }

    /**
     * 检测是否需要升级
     *
     * @param minVersion  最小支持版本
     * @param lastVersion 最后支持版本
     * @return {@link Constant.UpdateType}
     */
    public static int checkUpdateInfo(String minVersion,
                                      String lastVersion) {

        if (TextUtils.isEmpty(minVersion)) {
            Log.i("TYSQ",
                    "checkUpdateInfo: min is null.");
            return Constant.UpdateType.NONE;
        }

        if (TextUtils.isEmpty(lastVersion)) {
            Log.i("TYSQ",
                    "checkUpdateInfo: last is null.");
            return Constant.UpdateType.NONE;
        }

        String versionName = BuildConfig.VERSION_NAME;

        // 如果最新的版本和当前的版本一致，则不需要更新
        if (lastVersion.equals(versionName)) {
            return Constant.UpdateType.NONE;
        }

        // 如果最小支持版本和当前的版本一致，则需要提示更新
        if (minVersion.equals(versionName)) {
            return Constant.UpdateType.ORDINARY;
        }

        String[] minVersionArray = minVersion.split(DOT);
        String[] lastVersionArray = lastVersion.split(DOT);

        if (minVersionArray.length < 3) {
            Log.i("TYSQ",
                    "checkUpdateInfo: min length not enough:" + minVersionArray.length);
            return Constant.UpdateType.NONE;
        }

        if (lastVersionArray.length < 3) {
            Log.i("TYSQ",
                    "checkUpdateInfo: last length not enough:" + lastVersionArray.length);
            return Constant.UpdateType.NONE;
        }


        String[] curVersionArray = versionName.split(DOT);

        if (curVersionArray.length < 3) {
            Log.i("TYSQ",
                    "checkUpdateInfo: cur length not enough:" + curVersionArray.length);
            return Constant.UpdateType.NONE;
        }

        try {
            for (int i = 0; i < 3; i++) {
                String minVer = minVersionArray[i];
                String curVer = curVersionArray[i];

                int minVerNum = Integer.parseInt(minVer);
                int curVerNum = Integer.parseInt(curVer);

                // 当前版本 大于 最小支持版本，可以立马终止循环，
                // 当前版本 小于 最小支持版本，则强制更新
                if (curVerNum > minVerNum) {
                    break;
                } else if (curVerNum < minVerNum) {
                    return Constant.UpdateType.FORCE;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            for (int i = 0; i < 3; i++) {
                String lastVer = lastVersionArray[i];
                String curVer = curVersionArray[i];

                int lastVerNum = Integer.parseInt(lastVer);
                int curVerNum = Integer.parseInt(curVer);

                // 当前版本 大于 最小支持版本，可以立马终止循环，
                // 当前版本 小于 最小支持版本，则提示更新
                if (curVerNum > lastVerNum) {
                    break;
                } else if (curVerNum < lastVerNum) {
                    return Constant.UpdateType.ORDINARY;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Constant.UpdateType.NONE;
        }

        return Constant.UpdateType.NONE;
    }

    public static String getRealDataSource(String dataSource) throws ParserUrlException {
        if (!BuildConfig.FLAVOR.contains("tysq")) {
            return dataSource;
        }

        try {
            Uri uri = Uri.parse(dataSource);
            String scheme = uri.getScheme();

            // 如果协议为空，则不上协议
            if (scheme == null || scheme.equals("")) {
                dataSource = Constant.HttpType.HTTPS + dataSource;
                uri = Uri.parse(dataSource);
            }

            String host = uri.getHost();

            if (!TextUtils.isEmpty(host)) {
                return Constant.HttpType.HTTPS + host;
            }

        } catch (Exception e) {
            throw new ParserUrlException(e.getMessage());
        }

        throw new ParserUrlException("Host is invalidation.");

//        String lowDataSource = dataSource.toLowerCase();

//        if (lowDataSource.startsWith(Constant.HttpType.HTTPS)) {
//            return dataSource;
//        }

//        if (lowDataSource.startsWith(Constant.HttpType.HTTP)) {
//            return Constant.HttpType.HTTPS
//                    + dataSource.subSequence(Constant.HttpType.HTTP.length(), dataSource.length());
//        }

//        return Constant.HttpType.HTTPS + dataSource;
    }

    /**
     * 添加草稿标记
     */
    public static void addDraftTag(Context context,
                                   TextView textView,
                                   String content) {

        String draft = context.getString(R.string.article_detail_draft);
        content = draft + content;

        IconTextSpan hotSpan = new IconTextSpan(context, R.color.draft_bg, draft);
        hotSpan.setRightMarginDpValue(5);

        SpannableString spannableString = new SpannableString(content);
        spannableString.setSpan(hotSpan, 0, draft.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        textView.setText(spannableString);

    }

    /**
     * 添加隐藏标签
     */
    public static void addHideTag(Context context,
                                  TextView textView,
                                  String content) {
        String hide = context.getString(R.string.article_detail_hide);
        content = hide + content;

        IconTextSpan hotSpan = new IconTextSpan(context, R.color.hide_bg, hide);
        hotSpan.setRightMarginDpValue(5);

        SpannableString spannableString = new SpannableString(content);
        spannableString.setSpan(hotSpan, 0, hide.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        textView.setText(spannableString);
    }

    /**
     * 添加置顶标记
     */
    public static void addTopTag(Context context,
                                 TextView textView,
                                 String content) {

        String tag = context.getString(R.string.article_list_top_tag);
        content = tag + " " + content;

        IconBorderTextSpan topSpan = new IconBorderTextSpan(context, R.color.article_top, tag);

        topSpan.setRightMarginDpValue((int) 12.5);

        SpannableString spannableString = new SpannableString(content);
        spannableString.setSpan(topSpan,
                0,
                tag.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        textView.setText(spannableString);

    }

    /**
     * 获取用户等级的图标
     *
     * @param grade 等级
     * @return 等级的资源图标
     */
    public static Integer getLvIcon(int grade) {
        Integer gradeResource = Constant.LV_MAP.get(grade);
        if (gradeResource == null) {
            gradeResource = Constant.LV_MAP.get(Constant.DEFAULT_GRADE);
        }

        return gradeResource;
    }

    /**
     * 隐藏软键盘
     *
     * @param ev       事件类型
     * @param activity 当前所在的activity
     */
    public static void hideKeyBoard(MotionEvent ev, Activity activity) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = activity.getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                hideKeyboard(v.getWindowToken(), activity);
            }
        }
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private static boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 获取InputMethodManager，隐藏软键盘
     *
     * @param token
     */
    public static void hideKeyboard(IBinder token, Activity activity) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static final String[] REPORT_TYPE = new String[]{
            "广告或垃圾信息", "涉嫌侵权", "低质问题", "其他"
    };


    /**
     * 换算金币为人民币
     *
     * @param data 金币
     * @return
     */
    public static String formatCoin(long data) {
        int coin = (int) data;
        float num = (float) coin / 1000;

        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(num);
    }

    /**
     * 获取屏幕上均分的宽度
     *
     * @param context 上下文
     * @param num     数量
     * @return
     */
    public static int getAverageWidth(Context context, int num) {
        return ScreenUtils.getScreenWidth(context) / num;
    }
}

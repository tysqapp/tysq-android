package com.tysq.ty_android.feature.web;

import android.Manifest;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.abc.lib_utils.toast.ToastUtils;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bit.web.BitWebViewFragment;
import com.google.gson.Gson;
import com.tysq.ty_android.R;
import com.tysq.ty_android.config.Constant;
import com.tysq.ty_android.feature.login.LoginActivity;
import com.tysq.ty_android.local.sp.UserCache;
import com.tysq.ty_android.net.cookie.PersistentCookieStore;
import com.tysq.ty_android.utils.TyFileUtils;
import com.tysq.ty_android.utils.TyUtils;
import com.tysq.ty_android.utils.TyWebViewCookie;
import com.tysq.ty_android.utils.download.DownloadHelper;
import com.zinc.libpermission.JPermissionActivity;
import com.zinc.libpermission.annotation.Permission;
import com.zinc.libpermission.annotation.PermissionCanceled;
import com.zinc.libpermission.annotation.PermissionDenied;
import com.zinc.libpermission.bean.CancelInfo;
import com.zinc.libpermission.bean.DenyInfo;
import com.zinc.libpermission.callback.IPermission;
import com.zinc.libpermission.utils.JPermissionUtil;

import java.io.File;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cookie;
import webview.OpenNewLink;
import webview.Title;

/**
 * author       : frog
 * time         : 2019-07-22 16:33
 * desc         :
 * version      : 1.3.0
 */
public class TyWebViewFragment
        extends BitWebViewFragment
        implements View.OnLongClickListener {

    private static String TAG = TyWebViewFragment.class.getSimpleName();

    private TyWebViewListener mListener;

    private final Gson mGson = new Gson();

    private final Canvas mCanvas = new Canvas();

    public static TyWebViewFragment newInstance(String url) {
        TyWebViewFragment fragment = new TyWebViewFragment();
        Bundle bundle = new Bundle();
        bundle.putString("LOAD_URL", url);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);

        syncCookie(mUrl);

        mWebView.setOnLongClickListener(this);
    }

    private void syncCookie(String url) {

        CookieManager cookieManager = TyWebViewCookie.getInstance();

        if (cookieManager == null) {
            return;
        }

        List<Cookie> cookies = PersistentCookieStore.getInstance().getCookies();
        for (int i = 0; i < cookies.size(); i++) {
            Cookie cookie = cookies.get(i);
            String value = cookie.name() + "=" + cookie.value();
            cookieManager.setCookie(url, value);
        }
        TyWebViewCookie.sync();

    }

    public void setListener(TyWebViewListener listener) {
        this.mListener = listener;
    }

    @SuppressLint("AddJavascriptInterface")
    protected void addJavascriptInterface() {
        mWebView.addJavascriptInterface(new JavaScriptInterface(), "android");
    }

    private class JavaScriptInterface {
        /**
         * 与js交互时用到的方法，在js里直接调用的
         */
        @JavascriptInterface
        public void fromHtml(final String type, final String json) {
            Log.i(TAG, "fromHtml: [type:" + type + "; json:" + json + "]");

            switch (type) {
                // 标题
                case Constant.HtmlType.TITLE:
                    if (mListener == null) {
                        return;
                    }
                    Title title = mGson.fromJson(json, Title.class);
                    mListener.setTitle(title.getNavigationTitle());
                    break;

                // 关闭
                case Constant.HtmlType.CLOSE_PAGE:
                    if (mListener == null) {
                        return;
                    }
                    mListener.close();
                    break;

                // 开新链接
                case Constant.HtmlType.OPEN_NEW_LINK:
                    OpenNewLink openNewLink = mGson.fromJson(json, OpenNewLink.class);

                    if (openNewLink.isNeedLogin()) {
                        if (UserCache.isEmpty()) {
                            LoginActivity.startActivity(getContext(), null);
                            return;
                        }
                    }

                    String navigationLink = openNewLink.getNavigationLink();

                    TyUtils.handleWebViewLink(getContext(), navigationLink);

                    break;
            }

        }

        @JavascriptInterface
        public boolean openInAndroid() {
            return true;
        }

    }

    @Override
    public boolean onLongClick(View v) {
        return savePic(v);
    }

    public boolean savePic(View v) {

        final WebView.HitTestResult result = ((WebView) v).getHitTestResult();
        if (result != null) {
            int type = result.getType();
            //图片保存
            if (type == WebView.HitTestResult.IMAGE_TYPE
                    || type == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {

                JPermissionActivity.permissionRequest(getContext(),
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        100,
                        new IPermission() {
                            @Override
                            public void ganted() {
                                new MaterialDialog
                                        .Builder(getContext())
                                        .title(getString(R.string.web_view_save_suc_tip))
                                        .content(getString(R.string.web_view_save_img))
                                        .cancelable(false)
                                        .negativeText(getString(R.string.web_view_save_cancel))
                                        .positiveText(getString(R.string.web_view_save_confirm))
                                        .onPositive((dialog, which) -> {
                                            handleImg(result);
                                        })
                                        .show();
                            }

                            @Override
                            public void denied(int requestCode, List<String> denyList) {
                                ToastUtils.show(getString(R.string.permission_need_save));
                            }

                            @Override
                            public void canceled(int requestCode) {
                                ToastUtils.show(getContext().getString(R.string.permission_need_save));
                            }
                        });

                return true;
            }
        }

        return false;
    }

    public void handleImg(WebView.HitTestResult result) {
        String imgUrl = result.getExtra();

        if (TextUtils.isEmpty(imgUrl)) {
            return;
        }

        final String fileName = System.currentTimeMillis() / 1000 + (imgUrl.endsWith(".png") ? ".png" : ".jpg");
        final File folderName = TyFileUtils.getQrFolder();
        if (folderName == null) {
            ToastUtils.show(getString(R.string.web_view_save_failure));
        }

        File file = TyFileUtils.createFile(folderName, fileName);
        if (file == null) {
            ToastUtils.show(getString(R.string.web_view_save_failure));
        }

        Observable<DownloadHelper.Progress> observable;

        if (imgUrl.startsWith("data:image/")) {
            observable = DownloadHelper.downloadBase64(imgUrl, file);
        } else {
            observable = DownloadHelper.download(imgUrl, file, false);
        }

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DownloadHelper.Progress>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(DownloadHelper.Progress o) {
                        ToastUtils.show(String.format(getString(R.string.web_view_save_suc),
                                file.getAbsolutePath()));
                        TyFileUtils.updateImageInSystem(getContext(), file);
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.show(String.format(getString(R.string.web_view_save_failure_with_message), e.getMessage()));
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public String getUrl() {
        if (mWebView == null) {
            return mUrl;
        }
        return mWebView.getUrl();
    }

    @Permission(value = {Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCode = 200)
    public void crop() {

        if (mWebView == null) {
            return;
        }

        View decorView = getActivity().getWindow().getDecorView();

        Bitmap bitmap = Bitmap.createBitmap(
                decorView.getWidth(),
                decorView.getHeight(),
                Bitmap.Config.ARGB_8888);
        mCanvas.setBitmap(bitmap);

        decorView.draw(mCanvas);

        String path = TyFileUtils.saveBitmap(bitmap);

        TyFileUtils.updateImageInSystem(getContext(), new File(path));

        bitmap.recycle();

        if (TextUtils.isEmpty(path)) {
            ToastUtils.show(getString(R.string.invite_qr_failure));
        } else {
            ToastUtils.show(String.format(getString(R.string.invite_qr_suc), path));
        }
    }

    @PermissionCanceled(requestCode = 200)
    private void cancel(CancelInfo cancelInfo) {
        ToastUtils.show(getString(R.string.invite_need_save));
    }

    @PermissionDenied(requestCode = 200)
    private void deny(DenyInfo denyInfo) {
        ToastUtils.show(getString(R.string.invite_need_save));
        JPermissionUtil.goToMenu(getContext());
    }

    public void reload() {
        if (!TextUtils.isEmpty(mUrl)) {
            syncCookie(mUrl);
        }

        mWebView.reload();
    }

}

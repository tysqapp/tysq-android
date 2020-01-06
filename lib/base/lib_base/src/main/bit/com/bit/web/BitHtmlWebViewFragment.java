package com.bit.web;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebBackForwardList;

import com.bit.view.fragment.BitBaseFragment;
import com.bit.widget.HTMLWebView;

/**
 * author       : frog
 * time         : 2019/3/26 下午3:29
 * desc         : WebView
 * version      : 1.3.0
 */
public class BitHtmlWebViewFragment extends BitBaseFragment {

    private static String TAG = BitHtmlWebViewFragment.class.getSimpleName();

    public static final String CONTENT = "CONTENT";

    private HTMLWebView mWebView;
    private String mContent;

    public static BitHtmlWebViewFragment newInstance(String content) {
        BitHtmlWebViewFragment fragment = new BitHtmlWebViewFragment();
        Bundle bundle = new Bundle();
        bundle.putString(CONTENT, content);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initArgs(Bundle arguments) {
        super.initArgs(arguments);

        this.mContent = arguments.getString(CONTENT);
    }

    @Override
    protected View onCreateFragmentView(LayoutInflater inflater,
                                        @Nullable ViewGroup container,
                                        @Nullable Bundle savedInstanceState) {

        if (container == null || container.getContext() == null) {
            return null;
        }

        mWebView = new HTMLWebView(container.getContext());

        return mWebView;

    }

    @Override
    protected void initView(View view) {
        mWebView.setHtml(mContent);
    }

    //处理WebView中回退的事件
    @Override
    public boolean onConsumeBackEvent(FragmentManager fragmentManager) {
        WebBackForwardList webBackForwardList = this.mWebView.copyBackForwardList();

        //如果有回退历史，而且可回退，就消费该事件
        if (webBackForwardList.getSize() > 0 && this.mWebView.canGoBack()) {
            this.mWebView.goBack();
            return true;
        }

        return super.onConsumeBackEvent(fragmentManager);
    }

    @Override
    public void onResume() {
        mWebView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mWebView.onPause();
    }

    @Override
    public void onDestroy() {
        if (mWebView != null) {
            mWebView.destroy();
            mWebView = null;
        }

        super.onDestroy();
    }

}

package com.bit.web;

import java.lang.ref.WeakReference;

/**
 * author       : frog
 * time         : 2019/3/26 下午3:36
 * desc         : 
 * version      : 1.3.0
 */

public class IWebViewJavaScriptInterface {

    protected WeakReference<BitWebViewFragment> mFragment;

    public BitWebViewFragment getWebViewFragment() {
        return this.mFragment.get();
    }

    public void setWebViewFragment(BitWebViewFragment fragment) {
        this.mFragment = new WeakReference<BitWebViewFragment>(fragment);
    }

}

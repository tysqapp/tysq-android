package com.tysq.ty_android.feature.myArticle;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.bit.utils.UIUtils;
import com.tysq.ty_android.R;
import com.tysq.ty_android.app.TyApplication;
import com.tysq.ty_android.base.SimpleLoadMoreFragment;
import com.tysq.ty_android.base.activity.CommonToolbarActivity;
import com.tysq.ty_android.config.Constant;
import com.tysq.ty_android.feature.myArticle.adapter.MyArticleAdapter;
import com.tysq.ty_android.feature.myArticle.di.DaggerMyArticleComponent;
import com.tysq.ty_android.feature.myArticle.di.MyArticleModule;
import com.tysq.ty_android.local.sp.UserCache;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import eventbus.DeleteArticleEvent;
import eventbus.HideArticleEvent;
import response.article.MyArticleResp;
import vo.article.MyArticleTitleVO;

/**
 * author       : frog
 * time         : 2019/5/27 下午12:00
 * desc         : 我的文章
 * version      : 1.3.0
 */

public final class MyArticleFragment
        extends SimpleLoadMoreFragment<MyArticlePresenter, MyArticleResp.ArticlesInfoBean>
        implements MyArticleView,
        View.OnClickListener,
        MyArticleAdapter.MyArticleListener,
        CommonToolbarActivity.ICommonFragment {

    public static final String TAG = "MyArticleFragment";
    public static final String TYPE = "type";

    public static final String LOAD_TYPE = "LOAD_TYPE";

    private final MyArticleTitleVO mMyArticleTitleVO
            = new MyArticleTitleVO(0, Constant.MyArticleType.ALL);
    private PopupWindow mTypeWindow;

    private int yOffset;

    public static MyArticleFragment newInstance() {
        return newInstance(UserCache.getDefault().getAccountId(),
                true,
                true,
                true,
                Constant.MyArticleType.ALL);
    }

    public static MyArticleFragment newInstance(int userId,
                                                boolean isNeedHeader,
                                                boolean isNeedRefresh,
                                                boolean isNeedSwipe,
                                                int type) {

        Bundle args = new Bundle();
        args.putInt(USER_ID, userId);
        args.putBoolean(IS_NEED_HEADER, isNeedHeader);
        args.putBoolean(IS_NEED_REFRESH, isNeedRefresh);
        args.putBoolean(IS_NEED_SWIPE, isNeedSwipe);
        args.putInt(TYPE, type);

        MyArticleFragment fragment = new MyArticleFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void registerDagger() {
        DaggerMyArticleComponent.builder()
                .appComponent(TyApplication.getAppComponent())
                .myArticleModule(new MyArticleModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        yOffset = UIUtils.dip2px(getContext(), 3.5f);

        initWindow();
    }

    @Override
    protected void loadData(int start, int pageSize, boolean isFirst) {
        Log.d(TAG, String.valueOf(mMyArticleTitleVO.getType()));
        mPresenter.loadArticle(userId, mMyArticleTitleVO.getType(), start, pageSize, isFirst);
    }

    @Override
    protected void initArgs(Bundle arguments) {
        super.initArgs(arguments);
        int type = arguments.getInt(TYPE, Constant.MyArticleType.ALL);
        mMyArticleTitleVO.setType(type);
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return new MyArticleAdapter(getContext(),
                mData,
                mMyArticleTitleVO,
                this,
                isNeedHeader);
    }

    @Override
    public int getTitleId() {
        return R.string.my_article_title;
    }

    @Override
    public void onLoadArticleError(boolean isFirst) {
        onHandleError(isFirst);
    }

    @Override
    public void onLoadArticle(boolean isFirst,
                              List<MyArticleResp.ArticlesInfoBean> articlesInfo,
                              int totalNum) {

        mMyArticleTitleVO.setCount(totalNum);

        onHandleResponseData(articlesInfo, isFirst);

    }

    @Override
    protected boolean isLazyLoad() {
        return false;
    }

    /**
     * 删除文章
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void deleteArticle(DeleteArticleEvent event) {
        int articleIndex = -1;
        for (int i = 0; i < mData.size(); i++) {
            MyArticleResp.ArticlesInfoBean item = mData.get(i);
            if (item.getId().equals(event.getArticleId())) {
                articleIndex = i;
                break;
            }
        }
        if (articleIndex == -1) {
            return;
        }

        mData.remove(articleIndex);

        // 头部坐标
        int headPos = (isNeedRefresh ? 1 : 0);

        int realIndex = articleIndex + headPos + 1;
        mBaseAdapter.notifyItemRemoved(realIndex);

        mMyArticleTitleVO.setCount(mMyArticleTitleVO.getCount() - 1);
        mBaseAdapter.notifyItemChanged(headPos);
    }

    private void initWindow() {
        View window = getLayoutInflater().inflate(R.layout.window_my_article_type, null);
        window.findViewById(R.id.tv_all).setOnClickListener(this);
        window.findViewById(R.id.tv_draft).setOnClickListener(this);
        window.findViewById(R.id.tv_exam).setOnClickListener(this);
        window.findViewById(R.id.tv_publish).setOnClickListener(this);
        window.findViewById(R.id.tv_refuse).setOnClickListener(this);
        window.findViewById(R.id.tv_hide).setOnClickListener(this);
        mTypeWindow = new PopupWindow(window,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true);

        mTypeWindow.setOutsideTouchable(true);
        mTypeWindow.setBackgroundDrawable(
                ContextCompat.getDrawable(getContext(), R.drawable.ic_one_inch_alpha)
        );
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_all:
                mMyArticleTitleVO.setType(Constant.MyArticleType.ALL);
                break;
            case R.id.tv_draft:
                mMyArticleTitleVO.setType(Constant.MyArticleType.DRAFT);
                break;
            case R.id.tv_exam:
                mMyArticleTitleVO.setType(Constant.MyArticleType.EXAM);
                break;
            case R.id.tv_publish:
                mMyArticleTitleVO.setType(Constant.MyArticleType.PUBLISH);
                break;
            case R.id.tv_refuse:
                mMyArticleTitleVO.setType(Constant.MyArticleType.REFUSE);
                break;
            case R.id.tv_hide:
                mMyArticleTitleVO.setType(Constant.MyArticleType.HIDE);
                break;
        }

        if (mTypeWindow != null) {
            mTypeWindow.dismiss();
        }

        mBaseAdapter.onLoading();
        getFirstData(LoadType.CUSTOM);

    }

    @Override
    public void onTypeClick(View v) {
        mTypeWindow.showAsDropDown(v, 0, -yOffset, Gravity.START);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void articleHideStatusChange(HideArticleEvent event){

        int articleIndex = -1;
        for (int i = 0; i < mData.size(); i++){
            MyArticleResp.ArticlesInfoBean item = mData.get(i);
            if (item.getId().equals(event.getAccountId())){
                articleIndex = i;
                break;
            }
        }

        if (articleIndex == -1){
            return;
        }

        mData.get(articleIndex).setStatus(event.getStatu());
        //头部坐标
        int headPos = (isNeedHeader ? 1 : 0);
        //刷新头部坐标
        int refreshPos = (isNeedRefresh ? 1 : 0);
        int realIndex = articleIndex + headPos + refreshPos;
        mBaseAdapter.notifyItemChanged(realIndex);
    }
}

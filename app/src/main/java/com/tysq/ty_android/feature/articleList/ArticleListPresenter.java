package com.tysq.ty_android.feature.articleList;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.bit.presenter.BasePresenter;
import com.tysq.ty_android.config.Constant;
import com.tysq.ty_android.config.TyConfig;
import com.tysq.ty_android.net.RetrofitFactory;
import com.tysq.ty_android.net.rx.RxObservableSubscriber;
import com.tysq.ty_android.net.rx.RxParser;
import com.tysq.ty_android.net.rx.RxSingleSubscriber;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import response.AdResp;
import response.TopArticleResp;
import response.home.ArticleInfo;
import response.home.ArticleResp;
import response.home.PageInfo;

public final class ArticleListPresenter extends BasePresenter<ArticleListView> {

    private Disposable disposable;
    private int flag = 0;

    private int mSelTopIndex;
    private int mSelSubIndex;

    //排序类型
    private int mType;
    @Inject
    public ArticleListPresenter(ArticleListView view) {
        super(view);
    }

    /**
     * 获取
     *
     * @param selTopIndex 一级分类
     * @param selSubIndex 二级分类
     * @param type        排序
     * @param startIndex  开始下标
     * @param pageSize    每页大小
     * @param pageInfo
     * @param isFirst     是否第一次拉取
     */
    public void loadArticleList(int selTopIndex,
                                int selSubIndex,
                                int type,
                                long startIndex,
                                int pageSize,
                                PageInfo pageInfo,
                                boolean isFirst) {

        if (disposable != null) {
            if (!disposable.isDisposed()) {
                disposable.dispose();
            }
            getDisposable().remove(disposable);
        }

        if (selTopIndex == selSubIndex) {
            selSubIndex = TyConfig.CATEGORY_SUB_ALL_ID;
        }

        mSelTopIndex = selTopIndex;
        mSelSubIndex = selSubIndex;
        mType = type;

        RetrofitFactory
                .getApiService()
                .getArticleListInfo(selTopIndex,
                        selSubIndex,
                        type,
                        startIndex,
                        pageSize)
                .compose(RxParser.handleObservableDataResult())
                .map(new Function<ArticleResp, ArticleResp>() {
                    @Override
                    public ArticleResp apply(ArticleResp articleResp) throws Exception {

                        if (articleResp.getArticleInfoList() == null) {
                            articleResp.setArticleInfoList(new ArrayList<>());
                        }

                        for (ArticleInfo articleInfo : articleResp.getArticleInfoList()) {

                            int type;

                            if (TextUtils.isEmpty(articleInfo.getCoverType())) {
                                type = ArticleInfo.TEXT;
                            } else if (articleInfo.getCoverType().equals(Constant.ArticleType.VIDEO)) {
                                type = ArticleInfo.VIDEO;
                            } else if (articleInfo.getCoverType().equals(Constant.ArticleType.IMAGE)) {
                                type = ArticleInfo.IMAGE;
                            } else {
                                type = ArticleInfo.TEXT;
                            }

                            articleInfo.setType(type);

                        }


                        if (articleResp.getArticleInfoList() != null
                                && articleResp.getArticleInfoList().size() > 0) {
                            int size = articleResp.getArticleInfoList().size() / Constant.ARTICLE_ADVERTISEMENT_POS;
                            if (size > 0) {
                                for (int i = 1; i <= size; i++) {
                                    ArticleInfo articleInfo = new ArticleInfo();
                                    articleInfo.setType(ArticleInfo.ADVERTISEMENT);
                                    articleResp.getArticleInfoList()
                                            .add(i * Constant.ARTICLE_ADVERTISEMENT_POS + (i - 1), articleInfo);
                                }
                            }
                        }


                        if (articleResp.getArticleInfoList() != null
                                && articleResp.getArticleInfoList().size() > 0) {
                            long totalNum = articleResp.getTotalNum();
                            pageInfo.setTotal(totalNum);

                            ArticleInfo articleInfo = new ArticleInfo();
                            articleInfo.setType(ArticleInfo.PAGE);

                            articleResp.getArticleInfoList().add(articleInfo);
                        }

                        /*ArticleInfo articleInfo = new ArticleInfo();
                        articleInfo.setType(ArticleInfo.TOP_ARTICLE);
                        articleResp.getArticleInfoList().add(0, articleInfo);*/

                        return articleResp;
                    }
                })
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObservableSubscriber<ArticleResp>(mySelf) {
                    @Override
                    protected void onError(int code, String message) {
                        showErrorMsg(code, message);
//                        if (isFirst) {
                        mView.onError();
//                        } else {
//                            mView.onErrorLoadMore();
//                        }
                    }

                    @Override
                    protected void onSuccessRes(ArticleResp value) {
//                        if (isFirst) {
                        getAdvertisement(value.getArticleInfoList());
//                        } else {
//                            mView.onLoadMoreArticleList(value.getArticleInfoList());
//                        }
                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                        super.onSubscribe(d);
                        disposable = d;
                    }
                });

    }

    private void getAdvertisement(List<ArticleInfo> articleInfoList) {
        RetrofitFactory
                .getApiService()
                .getAdvertisement(0, 10, Constant.AnnouncementType.BANNER)
                .compose(RxParser.handleSingleDataResult())
                .subscribe(new RxSingleSubscriber<AdResp>(mySelf) {
                    @Override
                    protected void onError(int code, String message) {
                        showErrorMsg(code, message);
                        mView.onError();
                    }

                    @Override
                    protected void onSuccessRes(@NonNull AdResp value) {

                        if (mType != Constant.SortType.COMPOSITE || mSelTopIndex == TyConfig.CATEGORY_RECOMMEND_ID){
                            mView.onLoadArticleList(articleInfoList,
                                    value.getAdvertisementList(),
                                    null,
                                    mSelTopIndex,
                                    mSelSubIndex);
                        }else {
                            getTopArticleList(articleInfoList, value.getAdvertisementList());
                        }
                    }
                });
    }

    private void getTopArticleList(List<ArticleInfo> articleInfoList,
                                   List<AdResp.AdvertisementListBean> advertisementListBeanList) {
        RetrofitFactory
                .getApiService()
                .getTopArticleList(mSelTopIndex, mSelSubIndex)
                .compose(RxParser.handleSingleDataResult())
                .subscribe(new RxSingleSubscriber<TopArticleResp>(mySelf) {
                    @Override
                    protected void onError(int code, String message) {
                        showErrorMsg(code, message);
                        mView.onError();
                    }

                    @Override
                    protected void onSuccessRes(@NonNull TopArticleResp value) {
                        mView.onLoadArticleList(articleInfoList,
                                advertisementListBeanList,
                                value.getTopArticleBeanList(),
                                mSelTopIndex,
                                mSelSubIndex);
                    }
                });
    }

}

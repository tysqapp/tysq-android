package com.tysq.ty_android.feature.editArticle;

import android.support.annotation.NonNull;

import com.bit.presenter.BasePresenter;
import com.tysq.ty_android.config.Constant;
import com.tysq.ty_android.config.NetCode;
import com.tysq.ty_android.net.RetrofitFactory;
import com.tysq.ty_android.net.rx.RxObservableSubscriber;
import com.tysq.ty_android.net.rx.RxParser;
import com.tysq.ty_android.net.rx.RxSingleSubscriber;

import java.util.List;

import javax.inject.Inject;

import common.RespData;
import model.VideoModel;
import request.PublishArticleReq;
import response.article.ArticleDetailResp;
import response.article.PublishArticleResp;
import response.home.CategoryResp;

public final class EditArticlePresenter extends BasePresenter<EditArticleView> {
    @Inject
    public EditArticlePresenter(EditArticleView view) {
        super(view);
    }

    /**
     * 发布文章
     *
     * @param title      文章id
     * @param categoryId 分类
     * @param labelList  标签
     * @param html       html内容
     * @param imageList  图片
     * @param audioList  音频
     * @param videoList  视频
     * @param isPublish  是否发布
     */
    public void publishArticle(String title,
                               int categoryId,
                               List<Integer> labelList,
                               String html,
                               List<Integer> imageList,
                               List<Integer> audioList,
                               List<VideoModel> videoList,
                               boolean isPublish) {

        PublishArticleReq req = new PublishArticleReq();

        req.setTitle(title);
        req.setCategoryId(categoryId);
        req.setLabel(labelList);
        req.setContent(html);
        req.setImages(imageList);
        req.setAudios(audioList);
        req.setVideos(videoList);
        req.setStatus(isPublish ?
                Constant.MyArticleType.PUBLISH :
                Constant.MyArticleType.DRAFT);

        RetrofitFactory
                .getApiService()
                .postArticle(req)
                .compose(RxParser.handleSingleToResult())
                .subscribe(new RxSingleSubscriber<RespData<PublishArticleResp>>(mySelf) {
                    @Override
                    protected void onError(int code, String message) {
                        showErrorMsg(code, message);
                        mView.hideDialog();
                    }

                    @Override
                    protected void onSuccessRes(@NonNull RespData<PublishArticleResp> value) {

                        if (value.getStatus() == NetCode.NO_PERMISSION_CODE) {
                            mView.onNotEnoughRank(value.getData());
                        } else {
                            mView.onPublishArticle(isPublish, value.getData());
                        }
                    }
                });

    }

    /**
     * 加载文章信息
     *
     * @param articleId 文章id
     */
    public void loadArticleInfo(String articleId, CategoryResp categoryResp) {
        RetrofitFactory
                .getApiService()
                .getArticleDetail(articleId)
                .compose(RxParser.handleSingleDataResult())
                .subscribe(new RxSingleSubscriber<ArticleDetailResp>(mySelf) {
                    @Override
                    protected void onError(int code, String message) {
                        showErrorMsg(code, message);
                        mView.onLoadArticleInfoError();
                    }

                    @Override
                    protected void onSuccessRes(@NonNull ArticleDetailResp value) {
                        mView.onLoadArticleInfo(value.getArticleInfo(), categoryResp);
                    }
                });
    }

    /**
     * 编辑文章
     */
    public void editArticleInfo(String articleId, CategoryResp categoryResp){
        RetrofitFactory
                .getApiService()
                .editArticleDetail(articleId)
                .compose(RxParser.handleSingleDataResult())
                .subscribe(new RxSingleSubscriber<ArticleDetailResp>(mySelf) {
                    @Override
                    protected void onError(int code, String message) {
                        showErrorMsg(code, message);
                        mView.onLoadArticleInfoError();
                    }

                    @Override
                    protected void onSuccessRes(@NonNull ArticleDetailResp value) {
                        mView.onLoadArticleInfo(value.getArticleInfo(), categoryResp);
                    }
                });
    }

    /**
     * 更新文章
     *
     * @param articleId  文章id
     * @param title      标题
     * @param categoryId 分类
     * @param labelList  标签
     * @param html       html内容
     * @param imageList  图片
     * @param audioList  音频
     * @param videoList  视频
     * @param isPublish  是否发布
     */
    public void updateArticle(String articleId,
                              String title,
                              int categoryId,
                              List<Integer> labelList,
                              String html,
                              List<Integer> imageList,
                              List<Integer> audioList,
                              List<VideoModel> videoList,
                              boolean isPublish) {

        PublishArticleReq req = new PublishArticleReq();

        req.setArticleId(articleId);
        req.setTitle(title);
        req.setCategoryId(categoryId);
        req.setLabel(labelList);
        req.setContent(html);
        req.setImages(imageList);
        req.setAudios(audioList);
        req.setVideos(videoList);
        req.setStatus(isPublish ?
                Constant.MyArticleType.PUBLISH :
                Constant.MyArticleType.DRAFT);

        RetrofitFactory
                .getApiService()
                .putUpdateArticle(req)
                .compose(RxParser.handleSingleToResult())
                .subscribe(new RxSingleSubscriber<RespData<PublishArticleResp>>(mySelf) {
                    @Override
                    protected void onError(int code, String message) {
                        showErrorMsg(code, message);
                        mView.hideDialog();
                    }

                    @Override
                    protected void onSuccessRes(@NonNull RespData<PublishArticleResp> value) {

                        if (value.getStatus() == NetCode.NO_PERMISSION_CODE) {
                            mView.onNotEnoughRank(value.getData());
                        } else {
                            mView.onUpdateArticle(value.getData(), isPublish);
                        }


                    }
                });

    }

    public void loadCategory() {
        RetrofitFactory
                .getApiService()
                .getCategory()
                .compose(RxParser.handleObservableDataResult())
                .subscribe(new RxObservableSubscriber<CategoryResp>(mySelf) {
                    @Override
                    protected void onError(int code, String message) {
                        showErrorMsg(code, message);
                    }

                    @Override
                    protected void onSuccessRes(CategoryResp value) {
                        mView.onLoadCategory(value);
                    }
                });
    }

    public void loadCategory(String articleId, boolean isNeedCode){
        RetrofitFactory
                .getApiService()
                .getCategory()
                .compose(RxParser.handleObservableDataResult())
                .subscribe(new RxObservableSubscriber<CategoryResp>(mySelf) {
                    @Override
                    protected void onError(int code, String message) {
                        showErrorMsg(code, message);
                    }

                    @Override
                    protected void onSuccessRes(CategoryResp value) {
                        if (isNeedCode){
                            loadArticleInfo(articleId, value);
                        }else {
                            editArticleInfo(articleId, value);
                        }

                    }
                });
    }

}

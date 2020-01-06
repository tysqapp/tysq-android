package com.tysq.ty_android.feature.articleDetail.fragment;

import android.support.annotation.NonNull;

import com.bit.presenter.BasePresenter;
import com.jerry.image_watcher.model.WatchImageVO;
import com.tysq.ty_android.config.Constant;
import com.tysq.ty_android.config.NetCode;
import com.tysq.ty_android.feature.articleDetail.config.ArticleConstants;
import com.tysq.ty_android.feature.editArticle.parser.ParserFactory;
import com.tysq.ty_android.net.RetrofitFactory;
import com.tysq.ty_android.net.rx.RxObservableSubscriber;
import com.tysq.ty_android.net.rx.RxParser;
import com.tysq.ty_android.net.rx.RxSingleSubscriber;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import common.RespData;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import request.DeleteArticleReq;
import request.DeleteCommentReq;
import request.RewardArticleReq;
import response.article.ArticleDetailResp;
import response.article.ArticleDownloadVideoResp;
import response.article.ArticleReviewResp;
import response.article.RecommendArticleResp;
import response.article.RewardArticleResp;
import response.article.RewardListResp;
import response.common.TitleCountVO;
import response.permission.PermissionResp;
import response.rank.JudgementResp;
import vo.article.ArticleDetailVO;
import vo.article.ArticleDividerVO;
import vo.article.ArticleMoreVO;
import vo.article.ArticleTextVO;

public final class ArticleDetailPresenter extends BasePresenter<ArticleDetailView> {

    private ArticleDetailResp.ArticleInfoBean mArticleInfo;
    private List<RecommendArticleResp.ArticlesInfoBean> mRecommendList;
    private List<ArticleReviewResp.ArticleCommentsBean> mReviewList;
    private RewardListResp mRewardResp;
    private int mReviewTotalSize;

    @Inject
    public ArticleDetailPresenter(ArticleDetailView view) {
        super(view);
    }

    public void loadArticleInfo(String articleId,
                                long startTime,
                                int size) {
        loadArticleDetail(articleId, startTime, size);
    }

    /**
     * 获取当前用户对文章的权限
     */
    private void getArticlePermission(String articleId,
                                      long startTime,
                                      int size) {
        RetrofitFactory
                .getApiService()
                .getAccountPermission(articleId)
                .compose(RxParser.handleSingleDataResult())
                .subscribe(new RxSingleSubscriber<PermissionResp>(mySelf) {
                    @Override
                    protected void onError(int code, String message) {
                        showErrorMsg(code, message);
                        mView.onLoadArticleInfoError();
                    }

                    @Override
                    protected void onSuccessRes(@NonNull PermissionResp value) {
                        mView.onGetArticlePermission(value);
                        loadReview(articleId, startTime, size, value);
                    }
                });
    }

    /**
     * 获取详情
     */
    private void loadArticleDetail(String articleId,
                                   long startTime,
                                   int size) {
        RetrofitFactory
                .getApiService()
                .getArticleDetail(articleId)
                .compose(RxParser.handleSingleToResult())
                .subscribe(new RxSingleSubscriber<RespData<ArticleDetailResp>>(mySelf) {
                    @Override
                    protected void onError(int code, String message) {
                        showErrorMsg(code, message);
                        mView.onLoadArticleInfoError();
                    }

                    @Override
                    protected void onSuccessRes(@NonNull RespData<ArticleDetailResp> value) {
                        mArticleInfo = value.getData().getArticleInfo();
                        // 文章被删除
                        if (mArticleInfo.getStatus() == Constant.MyArticleType.DELETE) {
                            loadRewardArticleList(value.getData().getArticleInfo().getCategoryId(),
                                    articleId,
                                    size,
                                    0,
                                    startTime);
                            /*loadRecommend(value.getArticleInfo().getCategoryId(),
                                    articleId, startTime, size, true);*/
                            return;
                        }

                        if (value.getData().getArticleInfo().isSatisfy()) {
                            loadRewardArticleList(value.getData().getArticleInfo().getCategoryId(),
                                    articleId,
                                    size,
                                    0,
                                    startTime);
                            /*loadRecommend(value.getArticleInfo().getCategoryId(),
                                    articleId, startTime, size, false);*/
                        } else if (value.getData().getArticleInfo().getGrade() != 0) {
                            mView.onGradeNotEnough(value.getData().getArticleInfo().getGrade());
                        } else if (value.getStatus() == NetCode.GRADE_NO_ENOUGH_PERMISSION_CODE) {
                            mView.onGradeToRankNotEnough(value.getData().getArticleInfo().getLimitScore());
                        } else if (value.getStatus() == NetCode.NO_PERMISSION_CODE){
                            mView.onRankNotEnough(value.getData().getArticleInfo().getLimitScore());
                        }
                        else { // 文章验证
                            mView.onArticleNeedValidate();
                        }
                    }
                });
//                .compose(RxParser.handleSingleDataResult())
//                .subscribe(new RxSingleSubscriber<ArticleDetailResp>(mySelf) {
//                    @Override
//                    protected void onError(int code, String message) {
//                        showErrorMsg(code, message);
//
//                        mView.onLoadArticleInfoError();
//                    }
//
//                    @Override
//                    protected void onSuccessRes(@NonNull ArticleDetailResp value) {
//                        mArticleInfo = value.getArticleInfo();
//
//                        // 文章被删除
//                        if (mArticleInfo.getStatus() == Constant.MyArticleType.DELETE) {
//                            loadRewardArticleList(value.getArticleInfo().getCategoryId(),
//                                    articleId,
//                                    size,
//                                    0,
//                                    startTime);
//                            /*loadRecommend(value.getArticleInfo().getCategoryId(),
//                                    articleId, startTime, size, true);*/
//                            return;
//                        }
//
//                        if (value.getArticleInfo().isSatisfy()) {
//                            loadRewardArticleList(value.getArticleInfo().getCategoryId(),
//                                    articleId,
//                                    size,
//                                    0,
//                                    startTime);
//                            /*loadRecommend(value.getArticleInfo().getCategoryId(),
//                                    articleId, startTime, size, false);*/
//                        } else if (value.getArticleInfo().getGrade() != 0) {
//                            mView.onGradeNotEnough(value.getArticleInfo().getGrade());
//                        } else if (value.getArticleInfo().getLimitScore() != 0) {
//                            mView.onRankNotEnough(value.getArticleInfo().getLimitScore());
//                        } else { // 文章验证
//                            mView.onArticleNeedValidate();
//                        }
//
//                    }
//                });
    }

    /**
     * 获取相关推荐
     */
    private void loadRecommend(int categoryId,
                               String articleId,
                               long startTime,
                               int size,
                               boolean isDelete) {
        RetrofitFactory
                .getApiService()
                .getRecommendArticleList(categoryId, articleId, 0, 3)
                .compose(RxParser.handleSingleDataResult())
                .subscribe(new RxSingleSubscriber<RecommendArticleResp>(mySelf) {
                    @Override
                    protected void onError(int code, String message) {
                        showErrorMsg(code, message);
                        mView.onLoadArticleInfoError();
                    }

                    @Override
                    protected void onSuccessRes(@NonNull RecommendArticleResp value) {
                        mRecommendList = value.getArticlesInfo();
                        if (isDelete) {
                            handleArticleRemoved();
                        } else {
                            getArticlePermission(articleId, startTime, size);
                        }
                    }
                });
    }

    /**
     * 处理文章删除
     */
    private void handleArticleRemoved() {
        Observable.just(true)
                .map(new Function<Boolean, List<ArticleDetailVO>>() {
                    @Override
                    public List<ArticleDetailVO> apply(Boolean aBoolean) throws Exception {

                        List<ArticleDetailVO> resultList = new ArrayList<>();
                        // 添加标题
                        resultList.add(new ArticleDetailVO<>(ArticleConstants.ARTICLE_EMPTY, new Object()));

                        // 添加相关推荐
                        if (mRecommendList != null && mRecommendList.size() > 0) {
                            // 标题
                            resultList.add(new ArticleDetailVO<>(ArticleConstants.RECOMMEND_TITLE, new Object()));

                            // 内容
                            for (RecommendArticleResp.ArticlesInfoBean item : mRecommendList) {
                                resultList.add(new ArticleDetailVO<>(ArticleConstants.RECOMMEND_ITEM, item));
                            }
                        }

                        return resultList;
                    }
                })
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObservableSubscriber<List<ArticleDetailVO>>(mySelf) {
                    @Override
                    protected void onError(int code, String message) {
                        mView.onLoadArticleInfoError();
                    }

                    @Override
                    protected void onSuccessRes(List<ArticleDetailVO> value) {
                        mView.onLoadArticleInfo(mArticleInfo,
                                value,
                                0,
                                0,
                                0,
                                true);
                    }
                });

    }

    /**
     * 加载评论
     */
    private void loadReview(String articleId,
                            long startTime,
                            int size,
                            PermissionResp permissionInfo) {
        RetrofitFactory
                .getApiService()
                .getArticleReviewList(articleId, startTime, size)
                .compose(RxParser.handleObservableDataResult())
                .subscribe(new RxObservableSubscriber<ArticleReviewResp>(mySelf) {
                    @Override
                    protected void onError(int code, String message) {
                        showErrorMsg(code, message);
                        mView.onLoadArticleInfoError();
                    }

                    @Override
                    protected void onSuccessRes(@NonNull ArticleReviewResp value) {
                        mReviewList = value.getArticleComments();
                        mReviewTotalSize = value.getTotalNumber();
                        handleTheResult(permissionInfo);
                    }
                });
    }

    /**
     * 获取完数据
     */
    private void handleTheResult(PermissionResp permissionInfo) {

        Observable.just(true)
                .map(new Function<Boolean, List<ArticleDetailVO>>() {
                    @Override
                    public List<ArticleDetailVO> apply(Boolean aBoolean) throws Exception {

                        List<ArticleDetailVO> resultList = new ArrayList<>();

                        // 处理图片
                        if (mArticleInfo.getImages() != null) {
                            ArrayList<WatchImageVO> imageList = new ArrayList<>();

                            for (ArticleDetailResp.MediaBean image : mArticleInfo.getImages()) {
                                WatchImageVO watchImageVO = new WatchImageVO();
                                watchImageVO.setBlurryUrl(image.getUrl());
                                watchImageVO.setOriginalUrl(image.getOriginalUrl());
                                imageList.add(watchImageVO);
                            }

                            mView.onLoadImage(imageList);
                        }

                        // 添加标题
                        resultList.add(new ArticleDetailVO<>(ArticleConstants.TITLE, mArticleInfo));

                        String content = mArticleInfo.getContentOriginal()
                                .replace("\n", "")
                                .replace("<p></p>", "");


                        // 如果是待审核，需要对视频信息进行处理
                        if (mArticleInfo.getStatus() != Constant.MyArticleType.PUBLISH) {
                            for (ArticleDetailResp.ArticleInfoBean.VideosBean video : mArticleInfo.getVideos()) {
                                video.setStatus(Constant.VideoStatus.EXAM_MP4);
                            }
                        }

                        // 添加内容
                        List<ArticleDetailVO> contentList =
                                ParserFactory.parser(
                                        mArticleInfo.getVideos(),
                                        mArticleInfo.getAudios(),
                                        mArticleInfo.getImages(),
                                        content);
                        resultList.addAll(contentList);

                        //添加打赏列表
                        if (mRewardResp != null) {
                            resultList.add(new ArticleDetailVO<>(ArticleConstants.ARTICLE_REWARD, mRewardResp));
                        }

                        // 添加相关推荐
                        if (mRecommendList != null && mRecommendList.size() > 0) {
                            // 标题
                            resultList.add(new ArticleDetailVO<>(ArticleConstants.RECOMMEND_TITLE, new Object()));

                            // 内容
                            for (RecommendArticleResp.ArticlesInfoBean item : mRecommendList) {
                                resultList.add(new ArticleDetailVO<>(ArticleConstants.RECOMMEND_ITEM, item));
                            }
                        }

                        //添加广告位
                        resultList.add(new ArticleDetailVO<>(ArticleConstants.ADVERTISEMENT_ITEM, new Object()));

                        // 添加评论
                        // 加标题
                        resultList.add(new ArticleDetailVO<>(ArticleConstants.REVIEW_TITLE,
                                new TitleCountVO(mReviewTotalSize)));

                        // 添加评论具体
                        addReviewItem(permissionInfo, mReviewList, resultList);

                        Iterator<ArticleDetailVO> iterator = resultList.iterator();
                        while (iterator.hasNext()) {
                            ArticleDetailVO item = iterator.next();
                            if (item.getType() == ArticleConstants.CONTENT_TEXT) {
                                ArticleTextVO articleTextVO = (ArticleTextVO) item.getData();
                                if (articleTextVO.getContent().equals("<p></p>")) {
                                    iterator.remove();
                                }
                            }
                        }

                        return resultList;
                    }
                })
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObservableSubscriber<List<ArticleDetailVO>>(mySelf) {
                    @Override
                    protected void onError(int code, String message) {
                        mView.onLoadArticleInfoError();
                    }

                    @Override
                    protected void onSuccessRes(List<ArticleDetailVO> value) {
                        long startTime = 0;
                        if (mReviewList != null && mReviewList.size() > 0) {
                            startTime = mReviewList.get(mReviewList.size() - 1).getUpdatedAt();
                        }

                        int size = mReviewList == null ? 0 : mReviewList.size();

                        mView.onLoadArticleInfo(mArticleInfo,
                                value,
                                mReviewTotalSize,
                                size,
                                startTime,
                                false);
                    }
                });

    }

    /**
     * 添加评论
     */
    private void addReviewItem(PermissionResp permissionInfo,
                               List<ArticleReviewResp.ArticleCommentsBean> reviewList,
                               List<ArticleDetailVO> resultList) {
        if (reviewList == null || reviewList.size() <= 0) {
            resultList.add(new ArticleDetailVO<>(ArticleConstants.REVIEW_EMPTY, new Object()));
            return;
        }

        // 循环添加内容
        for (ArticleReviewResp.ArticleCommentsBean item : reviewList) {
            // 添加删除权限
            item.setCanDelete(permissionInfo.isCanDeleteComment());
            item.setCanForbid(permissionInfo.isCanForbidComment());
            resultList.add(new ArticleDetailVO<>(ArticleConstants.REVIEW_TOP_ITEM, item));

            // 二级评论为空，或条目为0，则直接略过
            if (item.getReply() == null || item.getReply().size() <= 0) {
                continue;
            }

            for (ArticleReviewResp.ArticleCommentsBean.ReplyBean subItem : item.getReply()) {
                // 添加删除权限
                subItem.setArticleId(item.getArticleId());
                subItem.setCanDelete(permissionInfo.isCanDeleteComment());
                resultList.add(new ArticleDetailVO<>(ArticleConstants.REVIEW_SUB_ITEM, subItem));
            }

            if (item.getReply().size() >= 3) {
                resultList.add(new ArticleDetailVO<>(ArticleConstants.REVIEW_MORE_ITEM,
                        new ArticleMoreVO(item.getArticleId(),
                                item.getId(),
                                item.isCanDelete(),
                                item.isCanForbid())));
            }

            // 添加分割线
            resultList.add(new ArticleDetailVO<>(ArticleConstants.REVIEW_DIVIDER_ITEM,
                    new ArticleDividerVO(item.getArticleId(), item.getId())));

        }

    }

    /**
     * 删除评论
     */
    public void deleteComment(final int position,
                              final String commentId,
                              final String topId,
                              final boolean isTop) {

        RetrofitFactory
                .getApiService()
                .deleteArticleComment(new DeleteCommentReq(commentId))
                .compose(RxParser.handleSingleDataResult())
                .subscribe(new RxSingleSubscriber<Object>(mySelf) {
                    @Override
                    protected void onError(int code, String message) {
                        showErrorMsg(code, message);
                        mView.hideDialog();
                    }

                    @Override
                    protected void onSuccessRes(@NonNull Object value) {
                        mView.onDeleteComment(position, commentId, topId, isTop, true);
                    }
                });

    }

    /**
     * 加载更多的评论
     */
    public void loadReviewMore(PermissionResp permissionInfo,
                               String articleId,
                               long startTime,
                               int size) {
        RetrofitFactory
                .getApiService()
                .getArticleReviewList(articleId, startTime, size)
                .compose(RxParser.handleObservableDataResult())
                .observeOn(Schedulers.computation())
                .map(new Function<ArticleReviewResp, List<ArticleDetailVO>>() {
                    @Override
                    public List<ArticleDetailVO> apply(ArticleReviewResp articleReviewResp)
                            throws Exception {

                        List<ArticleDetailVO> resultList = new ArrayList<>();

                        mReviewList = articleReviewResp.getArticleComments();

                        List<ArticleReviewResp.ArticleCommentsBean> articleComments
                                = articleReviewResp.getArticleComments();
                        if (articleComments != null && articleComments.size() > 0) {
                            // 添加评论具体
                            addReviewItem(permissionInfo, articleComments, resultList);
                        }

                        return resultList;

                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObservableSubscriber<List<ArticleDetailVO>>(mySelf) {
                    @Override
                    protected void onError(int code, String message) {
                        showErrorMsg(code, message);
                        mView.onLoadReviewMoreError();
                    }

                    @Override
                    protected void onSuccessRes(@NonNull List<ArticleDetailVO> value) {
                        long startTime = 0;
                        if (mReviewList != null && mReviewList.size() > 0) {
                            startTime = mReviewList.get(mReviewList.size() - 1).getUpdatedAt();
                        }

                        int size = mReviewList == null ? 0 : mReviewList.size();

                        mView.onLoadReviewMore(value, size, startTime);
                    }
                });
    }

    public void deleteArticle(String articleId) {

        DeleteArticleReq req = new DeleteArticleReq();
        req.setArticleId(articleId);

        RetrofitFactory
                .getApiService()
                .deleteArticle(req)
                .compose(RxParser.handleSingleDataResult())
                .subscribe(new RxSingleSubscriber<Object>(mySelf) {
                    @Override
                    protected void onError(int code, String message) {
                        showErrorMsg(code, message);
                        mView.hideDialog();
                    }

                    @Override
                    protected void onSuccessRes(@NonNull Object value) {
                        mView.onDeleteArticle(articleId);
                    }
                });


    }

    public void getJudgement(final String tag,
                             final String receiverName,
                             final String articleId,
                             final int receiverId,
                             final String topCommentId,
                             final String commentId,
                             final int position) {
        RetrofitFactory
                .getApiService()
                .getJudgement(Constant.JudgementType.COMMENT, articleId, articleId)
                .compose(RxParser.handleSingleDataResult())
                .subscribe(new RxSingleSubscriber<JudgementResp>(mySelf) {
                    @Override
                    protected void onError(int code, String message) {
                        showErrorMsg(code, message);
                        mView.hideDialog();
                    }

                    @Override
                    protected void onSuccessRes(@NonNull JudgementResp value) {
                        mView.hideDialog();
                        mView.onGetJudgement(value,
                                tag,
                                receiverName,
                                articleId,
                                receiverId,
                                topCommentId,
                                commentId,
                                position);
                    }
                });
    }

    /**
     * 获取下载视频所需积分
     */
    public void getDownloadVideoLimitScore(final String articleId, final int fileId, String cover) {
        RetrofitFactory
                .getApiService()
                .getJudgement(Constant.JudgementType.DOWNLOAD_VIDEO,
                        "" + fileId,
                        articleId)
                .compose(RxParser.handleSingleDataResult())
                .subscribe(new RxSingleSubscriber<JudgementResp>(mySelf) {
                    @Override
                    protected void onError(int code, String message) {
                        showErrorMsg(code, message);
                        mView.hideDialog();
                    }

                    @Override
                    protected void onSuccessRes(@NonNull JudgementResp value) {
                        mView.hideDialog();
                        mView.onGetDownloadVideoJudgement(fileId, value, cover);
                    }
                });
    }

    /**
     * 获取下载视频所需积分
     */
    public void getDownloadJudgment(String articleId, int fileId, String cover) {
        RetrofitFactory
                .getApiService()
                .getVideoJudgement(fileId, articleId)
                .compose(RxParser.handleSingleDataResult())
                .subscribe(new RxSingleSubscriber<ArticleDownloadVideoResp>(mySelf) {
                    @Override
                    protected void onError(int code, String message) {
                        showErrorMsg(code, message);
                        mView.hideDialog();
                    }

                    @Override
                    protected void onSuccessRes(@NonNull ArticleDownloadVideoResp value) {
                        mView.onGetDownloadJudgment(value, cover);
                    }
                });
    }

    /**
     * 加载打赏列表
     */
    public void loadRewardArticleList(int categoryId, String articleId, int size, int start, long startTime) {
        RetrofitFactory
                .getApiService()
                .getRewardArticleList(articleId, size, start)
                .compose(RxParser.handleSingleDataResult())
                .subscribe(new RxSingleSubscriber<RewardListResp>(mySelf) {
                    @Override
                    protected void onError(int code, String message) {
                        showErrorMsg(code, message);
                    }

                    @Override
                    protected void onSuccessRes(@NonNull RewardListResp value) {
                        mRewardResp = value;

                        // 文章被删除
                        if (mArticleInfo.getStatus() == Constant.MyArticleType.DELETE) {

                            loadRecommend(categoryId, articleId, startTime, size, true);
                        } else {
                            loadRecommend(categoryId, articleId, startTime, size, false);
                        }


                    }
                });
    }

    /**
     * 打赏文章
     */
    public void postRewardArticle(String articleId, int num) {
        RetrofitFactory
                .getApiService()
                .postRewardArticle(new RewardArticleReq(articleId, num))
                .compose(RxParser.handleSingleDataResult())
                .subscribe(new RxSingleSubscriber<RewardArticleResp>(mySelf) {
                    @Override
                    protected void onError(int code, String message) {
                        showErrorMsg(code, message);
                    }

                    @Override
                    protected void onSuccessRes(@NonNull RewardArticleResp value) {
                        mView.onPostRewardArticle(value);
                    }
                });
    }
}

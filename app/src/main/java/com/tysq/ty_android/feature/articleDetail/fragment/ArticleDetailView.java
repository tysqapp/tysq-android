package com.tysq.ty_android.feature.articleDetail.fragment;

import com.bit.view.IView;
import com.jerry.image_watcher.model.WatchImageVO;

import java.util.ArrayList;
import java.util.List;

import response.article.ArticleDetailResp;
import response.article.ArticleDownloadVideoResp;
import response.article.RecommendArticleResp;
import response.article.RewardArticleResp;
import response.permission.PermissionResp;
import response.rank.JudgementResp;
import vo.article.ArticleDetailVO;

public interface ArticleDetailView extends IView {
    void onLoadArticleInfoError();

    /**
     * @param articleInfo   文章信息
     * @param data          列表
     * @param reviewSize    评论条数
     * @param topReviewSize 一级评论条数
     * @param startTime     当前获取到的评论最新的那条时间
     * @param isDelete      文章是否被删除
     */
    void onLoadArticleInfo(ArticleDetailResp.ArticleInfoBean articleInfo,
                           List<ArticleDetailVO> data,
                           int reviewSize,
                           int topReviewSize,
                           long startTime,
                           boolean isDelete);

    void onDeleteComment(int position,
                         String commentId,
                         String topId,
                         boolean isTop,
                         boolean isShowTip);

    void onLoadReviewMoreError();

    void onLoadReviewMore(List<ArticleDetailVO> value,
                          int topReviewSize,
                          long startTime);

    void onArticleRemoved(List<RecommendArticleResp.ArticlesInfoBean> mRecommendList);

    void onDeleteArticle(String articleId);

    /**
     * 积分不足回调
     *
     * @param limitScore
     */
    void onRankNotEnough(int limitScore);

    void onGetJudgement(JudgementResp value,
                        String tag,
                        String receiverName,
                        String articleId,
                        int receiverId,
                        String topCommentId,
                        String commentId,
                        int position);

    void onArticleNeedValidate();

    /**
     * 权限回调
     */
    void onGetArticlePermission(PermissionResp value);

    /**
     * 回调图片
     *
     * @param imageList 图片列表
     */
    void onLoadImage(ArrayList<WatchImageVO> imageList);

    /**
     * 下载积分
     *  @param fileId        文件
     * @param judgementResp 权限返回
     * @param cover
     */
    void onGetDownloadVideoJudgement(int fileId, JudgementResp judgementResp, String cover);

    /**
     * 扣除下载积分
     *
     * @param value 积分信息
     * @param cover
     */
    void onGetDownloadJudgment(ArticleDownloadVideoResp value, String cover);

    /**
     * 等级不足回调
     */
    void onGradeNotEnough(int limitGrade);

    /**
     * 等级不足对应的积分不足
     */
    void onGradeToRankNotEnough(int limitScore);

    /**
     * 打赏文章回调
     */
    void onPostRewardArticle(RewardArticleResp value);
}

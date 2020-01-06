package com.tysq.ty_android.feature.articleDetail.listener;

import android.view.View;

import com.jerry.image_watcher.model.WatchImageVO;

import java.util.ArrayList;

import vo.article.ArticleAudioVO;
import vo.article.ArticleVideoVO;

/**
 * author       : frog
 * time         : 2019/5/22 上午10:28
 * desc         : 文章详情监听
 * version      : 1.3.0
 */
public interface ArticleDetailListener {

    void onDeleteCommentItem(View view,
                             int position,
                             String commentId,
                             String topId,
                             boolean isTop);

    /**
     * @param view         视图
     * @param position     位置
     * @param articleId    文章id
     * @param receiverId   被评论者id
     * @param receiverName 被评论者名字
     * @param commentId    该条评论的id
     * @param topCommentId 一级评论的id
     */
    void onReplyComment(View view,
                        int position,
                        String articleId,
                        int receiverId,
                        String receiverName,
                        String commentId,
                        String topCommentId);

    /**
     * 播放音频
     *
     * @param v               视图
     * @param adapterPosition 位置
     * @param articleAudioVO  音频信息
     */
    void onAudioClick(View v,
                      int adapterPosition,
                      ArticleAudioVO articleAudioVO);

    /**
     * 切换进度
     *
     * @param v               视图
     * @param adapterPosition 位置
     * @param articleAudioVO  音频信息
     */
    void onSeekTo(View v,
                  int adapterPosition,
                  ArticleAudioVO articleAudioVO);

    /**
     * 弹权限
     *
     * @param commentatorId   评论人的id
     * @param iconUrl         评论人的头像
     * @param commentatorName 评论人的名称
     * @param lv              评论人的等级
     */
    void onShowUserAdmin(int commentatorId,
                         String iconUrl,
                         String commentatorName,
                         int lv);

    /**
     * 显示评论图片
     *
     * @param imageList 图片数据
     * @param pos       显示的下标
     */
    void onShowCommentImage(ArrayList<WatchImageVO> imageList, int pos);

    /**
     * 显示详情图片
     *
     * @param pos 显示的下标
     */
    void onShowDetailImage(int pos);

    /**
     * 下载视屏
     *
     * @param articleVideoVO 视频
     */
    void downloadVideo(ArticleVideoVO articleVideoVO);

    /**
     * 弹出打赏弹窗
     */
    void onShowRewardTip();

    /**
     * 弹出打赏列表
     */
    void onShowRewardList();

}

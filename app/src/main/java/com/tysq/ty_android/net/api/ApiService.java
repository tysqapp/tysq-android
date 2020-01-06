package com.tysq.ty_android.net.api;

import java.util.List;

import common.RespData;
import io.reactivex.Observable;
import io.reactivex.Single;
import okhttp3.MultipartBody;
import request.ArticleCaptchaReq;
import request.ArticleCollectReq;
import request.ArticleReportReq;
import request.ArticleReviewReq;
import request.AttentionReq;
import request.ChangeFilenameReq;
import request.ChangePwdReq;
import request.CloudDeleteReq;
import request.ConfigurationReq;
import request.DeleteArticleReq;
import request.DeleteCommentReq;
import request.EmailVerifyReq;
import request.ForbidReviewReq;
import request.HideArticleReq;
import request.LoginReq;
import request.MergeFileReq;
import request.NotificationConfigReq;
import request.NotificationReadReq;
import request.PersonInfoUpdateReq;
import request.PublishArticleReq;
import request.RegisterReq;
import request.ResetPwdReq;
import request.ReviewReq;
import request.RewardArticleReq;
import request.TopArticleReq;
import request.VideoCoverReq;
import request.WithdrawReq;
import response.AdResp;
import response.ConfigurationResp;
import response.InviteResp;
import response.LabelResp;
import response.MyAttentionListResp;
import response.MyCommentListResp;
import response.MyFansListResp;
import response.TopArticleResp;
import response.UpdateResp;
import response.UserInfoResp;
import response.article.ArticleCollectResp;
import response.article.ArticleDetailResp;
import response.article.ArticleDownloadVideoResp;
import response.article.ArticleReviewResp;
import response.article.MyArticleResp;
import response.article.PublishArticleResp;
import response.article.RecommendArticleResp;
import response.article.ReviewArticleListResp;
import response.article.ReviewResp;
import response.article.RewardArticleResp;
import response.article.RewardListResp;
import response.cloud.FileInfoResp;
import response.cloud.FileListResp;
import response.coin.CoinOrderResp;
import response.coin.MyCoinDetailResp;
import response.coin.WithdrawInfoResp;
import response.coin.WithdrawLogResp;
import response.common.EmailCodeResp;
import response.forbid.ForbidReviewBanned;
import response.forbid.ForbidReviewResp;
import response.forbidlist.ForbidCommentResp;
import response.home.ArticleResp;
import response.home.CategoryResp;
import response.login.LoginResp;
import response.login.RegisterResp;
import response.login.RespCaptcha;
import response.notification.NotificationConfigResp;
import response.notification.NotificationReadedResp;
import response.notification.NotifyInfoResp;
import response.notification.NotifyUnReadResp;
import response.permission.PermissionResp;
import response.personal.PersonalPageResp;
import response.rank.JudgementResp;
import response.rank.RankDetailResp;
import response.rank.RankOrderResp;
import response.report.ReportDetailResp;
import response.search.SearchResultResp;
import response.upload.FileInfoCheckResp;
import response.upload.FileMergeResp;
import response.upload.FileUploadResp;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * author       : frog
 * time         : 2019/4/12 上午9:20
 * desc         : API
 * version      : 1.3.0
 */
public interface ApiService {

    /**
     * 获取验证码 base64 （不带宽高）
     */
    @GET("captcha/base64")
    Single<RespData<RespCaptcha>> getVerifyCode(@Query("captcha_type") String captchaType);

    /**
     * 获取验证码 base64
     */
    @GET("article/captcha")
    Single<RespData<RespCaptcha>> getVerifyCode(@Query("captcha_type") String captchaType,
                                                @Query("width") int width,
                                                @Query("height") int height);

    /**
     * 注册
     */
    @POST("account/register")
    Single<RespData<RegisterResp>> postRegister(@Body RegisterReq req);

    /**
     * 请求验证码
     *
     * @param email 邮箱
     * @param type  类型
     *              登录：login
     *              注册：registe
     *              重置密码：resetPassword
     */
    @GET("captcha/email")
    Single<RespData<EmailCodeResp>> getCaptcha(@Query("email") String email,
                                               @Query("type") String type);

    /**
     * 登录
     */
    @POST("account/login")
    Observable<RespData<LoginResp>> postLogin(@Body LoginReq req);

    /**
     * 邮箱验证
     */
    @GET("account/email/verify")
    Single<RespData<Object>> getEmailVerify(@Query("email") String email,
                                            @Query("captcha") String captcha,
                                            @Query("captcha_id") String captchaId);

    /**
     * 重置密码
     */
    @PUT("account/password/reset")
    Single<RespData<Object>> putResetPwd(@Body ResetPwdReq req);

    /**
     * 获取分类
     */
    @GET("article/category")
    Observable<RespData<CategoryResp>> getCategory();

    /**
     * 获取文章列表
     */
    @GET("article/list")
    Observable<RespData<ArticleResp>> getArticleListInfo(@Query("parent_id") int topId,
                                                         @Query("category_id") int subId,
                                                         @Query("type") int type,
                                                         @Query("start") long startIndex,
                                                         @Query("size") int pageSize);

    /**
     * 获取云盘文件列表
     */
    @GET("account/file/list")
    Single<RespData<FileInfoResp>> getCloudFileList(@Query("file_type") int fileType,
                                                    @Query("start") int start,
                                                    @Query("size") int size,
                                                    @Query("filename") String filename);

    /**
     * 获取标签
     */
    @GET("article/label")
    Single<RespData<LabelResp>> getLabel(@Query("start") int start,
                                         @Query("size") int size,
                                         @Query("name") String name);

    /**
     * 更换密码
     */
    @PUT("account/password")
    Single<RespData<Object>> putChangePwd(@Body ChangePwdReq req);

    /**
     * 更新用户信息
     */
    @PUT("account/info")
    Single<RespData<LoginResp>> putUpdatePersonData(@Body PersonInfoUpdateReq req);

    /**
     * 发表文章
     */
    @POST("article")
    Single<RespData<PublishArticleResp>> postArticle(@Body PublishArticleReq req);

    /**
     * 获取文章详情
     */
    @GET("article/info")
    Single<RespData<ArticleDetailResp>> getArticleDetail(@Query("article_id") String articleId);

    /**
     * 编辑文章
     */
    @GET("article/edit")
    Single<RespData<ArticleDetailResp>> editArticleDetail(@Query("article_id") String articleId);

    /**
     * 获取推荐文章
     */
    @GET("article/recommend/list")
    Single<RespData<RecommendArticleResp>> getRecommendArticleList(@Query("category_id") int categoryId,
                                                                   @Query("article_id") String articleId,
                                                                   @Query("start") int start,
                                                                   @Query("size") int size);

    /**
     * 获取打赏文章列表
     */
    @GET("articles/{article_id}/reward_records")
    Single<RespData<RewardListResp>> getRewardArticleList(@Path("article_id") String articleId,
                                                          @Query("size") int size,
                                                          @Query("start") int start);

    /**
     * 打赏文章
     */
    @POST("article/reward")
    Single<RespData<RewardArticleResp>> postRewardArticle(@Body RewardArticleReq req);

    /**
     * 获取评论
     */
    @GET("article/comment/list")
    Observable<RespData<ArticleReviewResp>> getArticleReviewList(@Query("article_id") String articleId,
                                                                 @Query("start_time") long startTime,
                                                                 @Query("size") int size);

    /**
     * 删除评论
     */
    @HTTP(method = "DELETE", path = "article/comment", hasBody = true)
    Single<RespData<Object>> deleteArticleComment(@Body DeleteCommentReq req);

    /**
     * 发送评论
     */
    @POST("article/comment")
    Single<RespData<ReviewResp>> postComment(@Body ReviewReq reviewReq);

    /**
     * 获取二级评论
     */
    @GET("article/subcomment/list")
    Observable<RespData<ArticleReviewResp>> getReviewList(@Query("article_id") String articleId,
                                                          @Query("comment_id") String commentId,
                                                          @Query("start_time") long startTime,
                                                          @Query("size") int size);

    /**
     * 获取我的评论
     */
    @GET("account/comments")
    Single<RespData<MyCommentListResp>> getMyCommentList(@Query("account_id") int accountId,
                                                         @Query("start") int start,
                                                         @Query("size") int size);

    /**
     * 获取我的文章
     */
    @GET("account/articles")
    Single<RespData<MyArticleResp>> getMyArticleList(@Query("account_id") int accountId,
                                                     @Query("status") int status,
                                                     @Query("start") int start,
                                                     @Query("size") int size);

    /**
     * 删除我的文章
     */
    @HTTP(method = "DELETE", path = "article", hasBody = true)
    Single<RespData<Object>> deleteArticle(@Body DeleteArticleReq req);

    /**
     * 获取文件列表
     *
     * @param type 获取列表来源
     *             1: 为云盘列表
     *             默认为文件列表
     */
    @GET("account/file/list")
    Single<RespData<FileListResp>> getFileList(@Query("start") int start,
                                               @Query("size") int size,
                                               @Query("type") int type);

    /**
     * 删除文件
     */
    @HTTP(method = "DELETE", path = "account/file", hasBody = true)
    Single<RespData<Object>> deleteFile(@Body CloudDeleteReq req);

    /**
     * 个人上传视频封面
     */
    @POST("account/video_cover")
    Single<RespData<Object>> postVideoCover(@Body VideoCoverReq req);

    /**
     * 更新文章
     */
    @PUT("article")
    Single<RespData<PublishArticleResp>> putUpdateArticle(@Body PublishArticleReq req);

    /**
     * 查询文件是否存在
     */
    @GET("upload_file/info")
    Call<RespData<FileInfoCheckResp>> getFileInfo(@Query("hash") String hash,
                                                  @Query("filename") String filename);

    /**
     * 文件上传
     *
     * @param chunkNumber      当前块的次序，第一个块是 1，注意不是从 0 开始的
     * @param totalChunks      文件被分成块的总数
     * @param chunkSize        分块大小，根据 totalSize 和这个值你就可以计算出总共的块数。
     *                         注意最后一块的大小可能会比这个要大
     * @param currentChunkSize 当前块的大小，实际大小。
     * @param totalSize        文件总大小。
     * @param hash             这个就是每个文件的唯一标示
     * @param filename         文件名
     * @param file             文件流
     */
    @Multipart
    @POST("upload_file")
    Call<RespData<FileUploadResp>> uploadFileSlice(@Part("chunk_number") int chunkNumber,
                                                   @Part("total_chunks") int totalChunks,
                                                   @Part("chunk_size") long chunkSize,
                                                   @Part("current_chunk_size") long currentChunkSize,
                                                   @Part("total_size") long totalSize,
                                                   @Part("hash") String hash,
                                                   @Part("filename") String filename,
                                                   @Part List<MultipartBody.Part> file);

    /**
     * 文件合并
     */
    @PUT("upload_file")
    Call<RespData<FileMergeResp>> mergeFileInfo(@Body MergeFileReq req);

    /**
     * 获取用户信息
     */
    @GET("account/info")
    Single<RespData<UserInfoResp>> getUserInfo();

    /**
     * 邮箱验证
     */
    @PUT("account/email")
    Single<RespData<Object>> putEmailVerify(@Body EmailVerifyReq req);

    /**
     * 个人积分判断
     *
     * @param action      发布文章：create_article
     *                    阅读文章：read_article
     *                    评论文章：comment_article
     *                    下载视频：download_video
     *                    {@link com.tysq.ty_android.config.Constant.JudgementType}
     * @param resourcesId 发布文章时可空
     */
    @GET("account/score/judgement")
    Single<RespData<JudgementResp>> getJudgement(@Query("action") String action,
                                                 @Query("resources_id") String resourcesId,
                                                 @Query("article_id") String articleId);

    /**
     * 查看用户积分明细
     */
    @GET("account/score_detail")
    Single<RespData<RankDetailResp>> getScoreDetail(@Query("start") int start,
                                                    @Query("size") int size);

    /**
     * 获取积分订单
     */
    @GET("account/score_order")
    Single<RespData<RankOrderResp>> getScoreOrder(@Query("start") int start,
                                                  @Query("size") int size);

    /**
     * 获取金币明细
     */
    @GET("account/coin")
    Single<RespData<MyCoinDetailResp>> getCoin(@Query("start") int start,
                                               @Query("size") int size);

    /**
     * 获取金币订单
     */
    @GET("account/coin_order")
    Single<RespData<CoinOrderResp>> getCoinOrder(@Query("status") int status,
                                                 @Query("start") int start,
                                                 @Query("size") int size);

    /**
     * 获取推荐
     *
     * @param start
     * @param size
     * @return
     */
    @GET("account/invite/friend")
    Single<RespData<InviteResp>> getInvite(@Query("start") int start,
                                           @Query("size") int size);

    /**
     * 获取配置
     */
    @POST("account/configuration")
    Single<RespData<ConfigurationResp>> getConfiguration(@Body ConfigurationReq req);

    /**
     * 获取公告
     */
    @GET("article/announcements")
    Single<RespData<AdResp>> getAdvertisement(@Query("start") int start,
                                              @Query("size") int size,
                                              @Query("position") int position);

    /**
     * 添加收藏
     */
    @PUT("article/collect")
    Single<RespData<Object>> putArticleCollect(@Body ArticleCollectReq req);

    /**
     * 获取文章收藏
     */
    @GET("account/collects")
    Single<RespData<ArticleCollectResp>> getArticleCollectList(@Query("account_id") int accountId,
                                                               @Query("start") int start,
                                                               @Query("size") int size);

    /**
     * 验证验证码
     */
    @POST("article/captcha")
    Single<RespData<Object>> postArticleCaptcha(@Body ArticleCaptchaReq req);

    /**
     * 获取提现记录
     */
    @GET("account/withdraws")
    Single<RespData<WithdrawLogResp>> getWithdrawLog(@Query("start") int start,
                                                     @Query("size") int size);

    /**
     * 获取分类
     */
    @GET("")
    Single<RespData<Object>> checkDataSource(@Url String url);

    /**
     * 获取 BTC 汇率
     */
    @GET("account/btc_rate")
    Single<RespData<WithdrawInfoResp>> getBtcRate(@Query("coin_amount") long coinAmount);

    /**
     * 提交提现
     */
    @POST("account/withdraw")
    Single<RespData<Object>> postWithdraw(@Body WithdrawReq req);

    /**
     * 获取更新信息
     */
    @GET("")
    Observable<UpdateResp> getUpdateInfo(@Url String url);

    /**
     * 个人权限判断
     */
    @GET("account/permission/judgement")
    Single<RespData<PermissionResp>> getAccountPermission(@Query("article_id") String articleId);

    /**
     * 审核文章
     */
    @PUT("article/review")
    Single<RespData<Object>> putArticleReview(@Body ArticleReviewReq req);

    /**
     * 获取版主权利分类
     */
    @GET("account/moderator_categorys")
    Single<RespData<ForbidReviewResp>> getForbidCategory();

    /**
     * 获取用户被禁评论
     */
    @GET("account/forbid_categorys")
    Single<RespData<ForbidReviewBanned>> getForbidReview(@Query("uid") int uid);

    /**
     * 获取禁止评论列表用户驳回原因
     */
    @GET("account/comment_blacklist")
    Single<RespData<ForbidCommentResp>> getForbidComment(@Query("start") int start,
                                                         @Query("size") int size,
                                                         @Query("account") String account);

    /**
     * 提交禁止评论的内容
     */
    @POST("account/forbid_comment")
    Single<RespData<Object>> postForbidComment(@Body ForbidReviewReq req);

    /**
     * 获取文章审核列表
     */
    @GET("account/review_articles")
    Single<RespData<ReviewArticleListResp>> getReviewArticles(@Query("start") int start,
                                                              @Query("size") int size,
                                                              @Query("status") int status);

    /**
     * 获取通知列表
     */
    @GET("notify/notifications")
    Single<RespData<NotifyInfoResp>> getNotificationList(@Query("size") int size,
                                                         @Query("start") int start);

    /**
     * 获取未读通知数量
     */
    @GET("notify/unread_count")
    Single<RespData<NotifyUnReadResp>> getNotifyUnReadCount();

    /**
     * 设置通知已读
     */
    @PUT("notify/read")
    Single<RespData<Object>> putNotificationRead(@Body NotificationReadReq req);

    /**
     * 设置通知全部为已读
     *
     * @return
     */
    @PUT("notify/state")
    Single<RespData<NotificationReadedResp>> putNotificationAllRead();

    /**
     * 获取用户订阅配置
     */
    @GET("notify/sub_configs")
    Single<RespData<NotificationConfigResp>> getNotificationConfig();

    /**
     * 更新用户订阅配置
     */
    @PUT("notify/sub_configs")
    Single<RespData<Object>> putNotificationConfig(@Body NotificationConfigReq req);

    /**
     * 文章详情举报文章
     */
    @POST("reports")
    Single<RespData<Object>> postArticleReport(@Body ArticleReportReq req);

    /**
     * 获取举报详情
     */
    @GET("reports/{id}")
    Single<RespData<ReportDetailResp>> getReportDetail(@Path("id") String id);

    /**
     * 添加/取消关注
     */
    @POST("account/attentions")
    Single<RespData<Object>> postAttention(@Body AttentionReq req);

    /**
     * 获取关注列表
     */
    @GET("account/attentions")
    Single<RespData<MyAttentionListResp>> getAttentionList(@Query("account_id") int accountId,
                                                           @Query("start") int start,
                                                           @Query("size") int size);

    /**
     * 获取粉丝列表
     */
    @GET("account/fans")
    Single<RespData<MyFansListResp>> getFansList(@Query("account_id") int accountId,
                                                 @Query("start") int start,
                                                 @Query("size") int size);

    /**
     * 获取搜索结果
     */
    @GET("search")
    Single<RespData<SearchResultResp>> getSearchResult(@Query("type") String type,
                                                       @Query("keyword") String keyword,
                                                       @Query("size") int size,
                                                       @Query("start") int start);

    /**
     * 获取用户信息
     */
    @GET("account/info")
    Single<RespData<PersonalPageResp>> getPersonalPage(@Query("account_id") int accountId);

    /**
     * 隐藏文章
     */
    @PUT("articles/{id}/state")
    Single<RespData<Object>> putHideArticle(@Path("id") String id,
                                            @Body HideArticleReq req);

    /**
     * 下载视频扣积分   mArticleId,
     */
    @GET("original_videos/{fileId}/judgement")
    Single<RespData<ArticleDownloadVideoResp>> getVideoJudgement(@Path("fileId") int fileId,
                                                                 @Query("article_id") String articleId);

    /**
     * 修改文件名称
     */
    @PUT("files/{file_id}")
    Single<RespData<Object>> putChangeFilename(@Path("file_id") int fileId,
                                               @Body ChangeFilenameReq filenameReq);

    /**
     * 置顶文章列表
     */
    @GET("top_articles")
    Single<RespData<TopArticleResp>> getTopArticleList(@Query("parent_id") int partentId,
                                                       @Query("category_id") int categoryId);

    /**
     * 置顶文章
     */
    @PUT("articles/{id}/top")
    Single<RespData<Object>> putTopArticle(@Path("id") String id,
                                           @Body TopArticleReq topArticleReq);
}


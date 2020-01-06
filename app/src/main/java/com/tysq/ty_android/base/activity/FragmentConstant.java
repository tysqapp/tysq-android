package com.tysq.ty_android.base.activity;

import com.tysq.ty_android.feature.aboutUs.AboutUsFragment;
import com.tysq.ty_android.feature.adminCenter.AdminCenterFragment;
import com.tysq.ty_android.feature.announcement.AnnouncementFragment;
import com.tysq.ty_android.feature.articleCollect.ArticleCollectFragment;
import com.tysq.ty_android.feature.articleExam.ArticleExamFragment;
import com.tysq.ty_android.feature.coin.coinWithdraw.CoinWithdrawFragment;
import com.tysq.ty_android.feature.coin.coinWithdrawLog.CoinWithdrawLogFragment;
import com.tysq.ty_android.feature.invite.InviteFragment;
import com.tysq.ty_android.feature.coin.myCoin.MyCoinFragment;
import com.tysq.ty_android.feature.myArticle.MyArticleFragment;
import com.tysq.ty_android.feature.myAttention.MyAttentionFragment;
import com.tysq.ty_android.feature.myReview.MyReviewFragment;
import com.tysq.ty_android.feature.notificationSetting.NotificationSettingFragment;
import com.tysq.ty_android.feature.rank.myRank.MyRankFragment;
import com.tysq.ty_android.feature.rank.orderRank.OrderRankFragment;

import java.util.HashMap;
import java.util.Map;

/**
 * author       : frog
 * time         : 2019-07-15 17:55
 * desc         : fragment 配置
 * version      : 1.3.0
 */
public class FragmentConstant {

    public static Map<String, Class<? extends CommonToolbarActivity.ICommonFragment>> FRAGMENT_INFO
            = new HashMap<>();

    static {

        // 积分订单
        FragmentConstant.FRAGMENT_INFO.put(OrderRankFragment.TAG, OrderRankFragment.class);
        // 邀请好友
        FragmentConstant.FRAGMENT_INFO.put(InviteFragment.TAG, InviteFragment.class);
        // 关于我们
        FragmentConstant.FRAGMENT_INFO.put(AboutUsFragment.TAG, AboutUsFragment.class);
        // 文章收藏
        FragmentConstant.FRAGMENT_INFO.put(ArticleCollectFragment.TAG, ArticleCollectFragment.class);
        // 提现记录
        FragmentConstant.FRAGMENT_INFO.put(CoinWithdrawLogFragment.TAG, CoinWithdrawLogFragment.class);
        // 我的评论
        FragmentConstant.FRAGMENT_INFO.put(MyReviewFragment.TAG, MyReviewFragment.class);
        // 提现
        FragmentConstant.FRAGMENT_INFO.put(CoinWithdrawFragment.TAG, CoinWithdrawFragment.class);
        // 公告
        FragmentConstant.FRAGMENT_INFO.put(AnnouncementFragment.TAG, AnnouncementFragment.class);
        // 版主中心
        FragmentConstant.FRAGMENT_INFO.put(AdminCenterFragment.TAG, AdminCenterFragment.class);
        // 待审核文章
        FragmentConstant.FRAGMENT_INFO.put(ArticleExamFragment.TAG, ArticleExamFragment.class);
        // 我的文章
        FragmentConstant.FRAGMENT_INFO.put(MyArticleFragment.TAG, MyArticleFragment.class);
        // 消息设置
        //FragmentConstant.FRAGMENT_INFO.put(NotificationSettingFragment.TAG, NotificationSettingFragment.class);
        FragmentConstant.FRAGMENT_INFO.put(MyAttentionFragment.TAG, MyAttentionFragment.class);
    }

}

package com.tysq.ty_android.base.activity;

import com.tysq.ty_android.feature.coin.myCoin.MyCoinFragment;
import com.tysq.ty_android.feature.forbidReview.ForbidReviewFragment;
import com.tysq.ty_android.feature.myAttention.MyAttentionFragment;
import com.tysq.ty_android.feature.myFans.MyFansFragment;
import com.tysq.ty_android.feature.notificationSetting.NotificationSettingFragment;
import com.tysq.ty_android.feature.rank.myRank.MyRankFragment;
import com.tysq.ty_android.feature.rewardList.RewardListFragment;

import java.util.HashMap;
import java.util.Map;

/**
 * author       : frog
 * time         : 2019-07-15 17:55
 * desc         : fragment 配置
 * version      : 1.0.0
 */
public class FragmentStrengthenConstant {

    public static Map<String, Class<? extends CommonToolbarStrengthenActivity.ICommonFragment>> FRAGMENT_INFO
            = new HashMap<>();

    static {
        // 消息设置
        FragmentStrengthenConstant.FRAGMENT_INFO.put(NotificationSettingFragment.TAG, NotificationSettingFragment.class);
        //我的关注
        FragmentStrengthenConstant.FRAGMENT_INFO.put(MyAttentionFragment.TAG, MyAttentionFragment.class);
        //我的粉丝
        FragmentStrengthenConstant.FRAGMENT_INFO.put(MyFansFragment.TAG, MyFansFragment.class);
        //我的金币
        FragmentStrengthenConstant.FRAGMENT_INFO.put(MyCoinFragment.TAG, MyCoinFragment.class);
        //我的积分
        FragmentStrengthenConstant.FRAGMENT_INFO.put(MyRankFragment.TAG, MyRankFragment.class);



    }

}

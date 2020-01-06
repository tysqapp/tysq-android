package com.tysq.ty_android.feature.rewardList;

import android.content.Context;
import android.content.Intent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bit.view.activity.BitBaseActivity;
import com.tysq.ty_android.R;

import butterknife.BindView;
/**
 * author       : liaozhenlin
 * time         : 2019/11/13 16:44
 * desc         : 打赏文章列表
 * version      : 1.5.0
 */
public class RewardListActivity extends BitBaseActivity {

    public static final String ARTICLE_ID = "ARTICLE_ID";

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.frame_layout_container)
    FrameLayout frameLayoutContainer;

    private String mArticleId;
    public static void startActivity(Context context, String articleId) {
        Intent intent = new Intent(context, RewardListActivity.class);

        intent.putExtra(ARTICLE_ID, articleId);

        context.startActivity(intent);
    }

    protected int getLayout() {
        return R.layout.common_activity_with_bar;
    }

    @Override
    protected void initIntent(Intent intent) {
        mArticleId = intent.getStringExtra(ARTICLE_ID);
    }

    @Override
    protected void initView() {
        ivBack.setOnClickListener(v -> finish());

        RewardListFragment fragment = RewardListFragment.newInstance(mArticleId);

        tvTitle.setText(fragment.getTitleId());

        addLayerFragment(frameLayoutContainer.getId(), fragment);
    }
}

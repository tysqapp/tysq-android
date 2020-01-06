package com.tysq.ty_android.feature.topArticleList;

import android.content.Context;
import android.content.Intent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bit.view.activity.BitBaseActivity;
import com.tysq.ty_android.R;
import com.tysq.ty_android.config.TyConfig;

import butterknife.BindView;

/**
 * author       : liaozhenlin
 * time         : 2019/11/12 11:53
 * desc         : 置顶文章列表
 * version      : 1.5.0
 */
public class TopArticleListActivity extends BitBaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.frame_layout_container)
    FrameLayout frameLayoutContainer;

    private int mTopId;
    private int mSelId;

    public static void startActivity(Context context, int topId, int subId) {
        Intent intent = new Intent(context, TopArticleListActivity.class);
        intent.putExtra(TyConfig.TOP_CATEGORY, topId);
        intent.putExtra(TyConfig.SUB_CATEGORY, subId);

        context.startActivity(intent);
    }

    @Override
    protected int getLayout() {
        return R.layout.common_activity_with_bar;
    }

    @Override
    protected void initIntent(Intent intent) {
        mTopId = intent.getIntExtra(TyConfig.TOP_CATEGORY, 1);
        mSelId = intent.getIntExtra(TyConfig.SUB_CATEGORY, 2);
    }

    @Override
    protected void initView() {
        ivBack.setOnClickListener(v -> finish());

        TopArticleListFragment fragment = TopArticleListFragment.newInstance(mTopId, mSelId);

        tvTitle.setText(fragment.getTitleId());

        addLayerFragment(frameLayoutContainer.getId(), fragment);


    }
}

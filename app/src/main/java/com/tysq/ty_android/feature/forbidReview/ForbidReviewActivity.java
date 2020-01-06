package com.tysq.ty_android.feature.forbidReview;

import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import com.bit.view.activity.BitBaseActivity;
import com.tysq.ty_android.R;

/**
 * author       : liaozhenlin
 * time         : 2019-8-27 10:07
 * desc         :禁止评论
 * version      : 1.0.0
 */
public class ForbidReviewActivity extends BitBaseActivity {

    public static final String ACCOUNT_NAME = "account_name";
    public static final String ACCOUNT_ID = "account_id";

    private String accountName;
    private int accountId;

    public static void startActivity(Context context, String name, int id) {
        Intent intent = new Intent(context, ForbidReviewActivity.class);
        intent.putExtra(ACCOUNT_NAME, name);
        intent.putExtra(ACCOUNT_ID, id);

        context.startActivity(intent);
    }



    @Override
    protected int getLayout() {
        return R.layout.actvity_banner;
    }

    @Override
    protected void initIntent(Intent intent) {
        intent = getIntent();
        accountName = intent.getStringExtra(ACCOUNT_NAME);
        accountId = intent.getIntExtra("account_id", accountId);
    }


    @Override
    protected void initView() {
        addLayerFragment(ID_FRAME_LAYOUT_CONTAINER, ForbidReviewFragment.newInstance(accountName, accountId));

    }

}

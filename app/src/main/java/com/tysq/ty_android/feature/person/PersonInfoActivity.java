package com.tysq.ty_android.feature.person;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;

import com.tysq.ty_android.R;
import com.tysq.ty_android.base.activity.CommonBarStrengthenActivity;
import com.tysq.ty_android.feature.person.personInfoChange.PersonInfoChangeFragment;
import com.tysq.ty_android.feature.person.personResumeChange.PersonResumeChangeFragment;

/**
 * author       : frog
 * time         : 2019/5/16 下午6:35
 * desc         : 用户信息
 * version      : 1.3.0
 */
public class PersonInfoActivity extends CommonBarStrengthenActivity {

    private static final String TYPE = "type";

    public static final int INFO_LIST = 1;
    public static final int NAME_CHANGE = 2;
    public static final int ADDRESS_CHANGE = 3;
    public static final int JOB_CHANGE = 4;
    public static final int INDUSTRY_CHANGE = 5;
    public static final int RESUME_CHANGE = 6;

    private int mType;

    private Fragment mCurFragment;

    public static void startActivity(Context context, int type) {
        Intent intent = new Intent(context, PersonInfoActivity.class);

        intent.putExtra(TYPE, type);

        context.startActivity(intent);
    }

    @Override
    protected void initIntent(Intent intent) {
        mType = intent.getIntExtra(TYPE, INFO_LIST);
    }

    @Override
    protected void initView() {
        super.initView();
        if (mType == NAME_CHANGE || mType == ADDRESS_CHANGE
                || mType == JOB_CHANGE || mType == INDUSTRY_CHANGE) {
            tvConfirm.setVisibility(View.VISIBLE);
            tvConfirm.setOnClickListener(v -> {
                if (mCurFragment instanceof PersonInfoChangeFragment) {
                    PersonInfoChangeFragment personInfoChangeFragment
                            = (PersonInfoChangeFragment) mCurFragment;

                    personInfoChangeFragment.isCommit();
                }
            });
        } else if (mType == RESUME_CHANGE){
            tvConfirm.setVisibility(View.VISIBLE);
            tvConfirm.setOnClickListener(v -> {
                if (mCurFragment instanceof PersonResumeChangeFragment) {
                    PersonResumeChangeFragment personResumeChangeFragment
                            = (PersonResumeChangeFragment) mCurFragment;

                    personResumeChangeFragment.isCommit();
                }
            });
        }

        else {
            tvConfirm.setVisibility(View.GONE);
        }
    }

    @Override
    protected int getPageTitle() {
        int title;
        switch (mType) {
            case INFO_LIST:
                title = R.string.person_title;
                break;

            case NAME_CHANGE:
                title = R.string.nick_change_title;
                break;

            case ADDRESS_CHANGE:
                title= R.string.person_address;
                break;

            case JOB_CHANGE:
                title=R.string.job_change_title;
                break;

            case INDUSTRY_CHANGE:
                title = R.string.industry_change_title;
                break;

            case RESUME_CHANGE:
                title = R.string.resume_change_title;

            default:
                title = R.string.person_title;
                break;
        }
        return title;
    }

    @Override
    protected Fragment getContentFragment() {
        switch (mType) {
            case INFO_LIST:
                mCurFragment = PersonInfoFragment.newInstance();
                break;

            case NAME_CHANGE:
                mCurFragment = PersonInfoChangeFragment.newInstance(R.string.nick_change_hint);
                break;

            case ADDRESS_CHANGE:
                mCurFragment = PersonInfoChangeFragment.newInstance(R.string.address_change_hint);
                break;

            case JOB_CHANGE:
                mCurFragment = PersonInfoChangeFragment.newInstance(R.string.job_change_hint);
                break;

            case INDUSTRY_CHANGE:
                mCurFragment = PersonInfoChangeFragment.newInstance(R.string.industry_change_hint);
                break;

            case RESUME_CHANGE:
                mCurFragment = PersonResumeChangeFragment.newInstance();
                break;
            default:
                mCurFragment = null;
                break;
        }
        return mCurFragment;
    }

    @Override
    protected boolean isGetConfirm() {
        return false;
    }

    @Override
    protected int getConfirmText() {
        return 0;
    }

    @Override
    protected int getConfirmBackground() {
        return R.drawable.selector_btn_round_blue_bg;
    }

    @Override
    protected int getConfirmTextColor() {
        return R.color.white;
    }


}

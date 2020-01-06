package com.tysq.ty_android.feature.person;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bit.view.fragment.BitBaseFragment;
import com.tysq.ty_android.R;
import com.tysq.ty_android.feature.cloudChoose.CloudChooseActivity;
import com.tysq.ty_android.local.sp.UserCache;
import com.tysq.ty_android.utils.TyUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import eventbus.UserInfoUpdateEvent;

/**
 * author       : frog
 * time         : 2019/5/17 上午11:59
 * desc         :
 * version      :
 */
public class PersonInfoFragment extends BitBaseFragment
        implements View.OnClickListener {

    @BindView(R.id.ll_photo)
    LinearLayout llPhoto;
    @BindView(R.id.iv_photo)
    ImageView ivPhoto;
    @BindView(R.id.ll_nick)
    LinearLayout llNick;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.ll_address)
    LinearLayout llAddress;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.ll_job)
    LinearLayout llJob;
    @BindView(R.id.tv_job)
    TextView tvJob;
    @BindView(R.id.ll_industry)
    LinearLayout llIndustry;
    @BindView(R.id.tv_industry)
    TextView tvIndustry;
    @BindView(R.id.ll_resume)
    LinearLayout llResume;
    @BindView(R.id.tv_resume)
    TextView tvResume;

    public static PersonInfoFragment newInstance() {
        Bundle args = new Bundle();

        PersonInfoFragment fragment = new PersonInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected View onCreateFragmentView(LayoutInflater inflater,
                                        @Nullable ViewGroup container,
                                        @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_person_info, container, false);
    }

    @Override
    protected void initView(View view) {
        setHeadPhoto();

        llPhoto.setOnClickListener(this);
        llNick.setOnClickListener(this);
        llAddress.setOnClickListener(this);
        llJob.setOnClickListener(this);
        llIndustry.setOnClickListener(this);
        llResume.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        settingPersonInfo(tvName, UserCache.getDefault().getAccount());
        settingPersonInfo(tvAddress, UserCache.getDefault().getHomeAddress());
        settingPersonInfo(tvJob,UserCache.getDefault().getCareer());
        settingPersonInfo(tvIndustry, UserCache.getDefault().getTrade());
        settingPersonInfo(tvResume, UserCache.getDefault().getPersonalProfile());
    }

    /**
     * 更新个人资料的内容
     * @param name
     * @param content
     */
    private void settingPersonInfo(TextView name, String content){
        if (content.trim().length() > 0){
            name.setText(content);
        }else{
            name.setText(R.string.person_verify);
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_photo:
                CloudChooseActivity.startActivity(getContext(), TAG,
                        CloudChooseActivity.HEAD_PHOTO, CloudChooseActivity.NO_LIMIT);
                break;

            case R.id.ll_nick:
                PersonInfoActivity.startActivity(getContext(), PersonInfoActivity.NAME_CHANGE);
                break;

            case R.id.ll_address:
                PersonInfoActivity.startActivity(getContext(), PersonInfoActivity.ADDRESS_CHANGE);
                break;

            case R.id.ll_job:
                PersonInfoActivity.startActivity(getContext(), PersonInfoActivity.JOB_CHANGE);
                break;

            case R.id.ll_industry:
                PersonInfoActivity.startActivity(getContext(), PersonInfoActivity.INDUSTRY_CHANGE);
                break;

            case R.id.ll_resume:
                PersonInfoActivity.startActivity(getContext(), PersonInfoActivity.RESUME_CHANGE);
                break;
        }
    }

    @Override
    protected int getInitRegister() {
        return BUTTER_KNIFE | EVENT_BUS;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateUserInfo(UserInfoUpdateEvent userInfoUpdate) {
        setHeadPhoto();
    }

    /**
     * 设置头像
     */
    private void setHeadPhoto() {
        if (getContext() == null) {
            return;
        }

        TyUtils.initUserPhoto(
                this,
                getContext(),
                UserCache.getDefault().getHeadUrl(),
                ivPhoto);

    }
}

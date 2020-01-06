package com.tysq.ty_android.widget;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tysq.ty_android.R;

import vo.MenuItemVO;

/**
 * author       : frog
 * time         : 2019/4/11 下午6:41
 * desc         : 首页的导航项
 * version      : 1.3.0
 */
public class MainNavTabItem extends RelativeLayout {

    private TextView mText;
    private ImageView mImage;
    private View mVDot;

    private MenuItemVO mItemVO;

//    private BadgeView mBadgeView;

    public MainNavTabItem(Context context) {
        this(context, null);
    }

    public MainNavTabItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater
                .from(context)
                .inflate(R.layout.widget_main_tab_item,
                        this,
                        true);

        setGravity(Gravity.CENTER);
//        setOrientation(VERTICAL);

        mText = findViewById(R.id.text);
        mImage = findViewById(R.id.image);
        mVDot = findViewById(R.id.v_dot);

//        mBadgeView = new BadgeView(getContext(), mImage);
//        mBadgeView.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
//        mBadgeView.setBadgeMargin(0, 0);
//        mBadgeView.setTextSize(5);

    }

    /**
     * 初始化数据
     *
     * @return
     * @itemVO 视图数据
     */
    public MainNavTabItem initData(MenuItemVO itemVO) {
        mItemVO = itemVO;
        fillInData();
        return this;
    }

    /**
     * 刷新视图
     */
    public void refresh() {
        fillInData();
    }

    private void fillInData() {
        mText.setText(mItemVO.getTitleRes());

        if (mItemVO.isSelect()) {
            mImage.setImageResource(mItemVO.getSelIcon());
            mText.setTextColor(ContextCompat.getColor(getContext(), R.color.select_text_color));
        } else {
            mImage.setImageResource(mItemVO.getUnselIcon());
            mText.setTextColor(ContextCompat.getColor(getContext(), R.color.unselect_color));
        }
    }

    public void updateNotificationNum() {
        mItemVO.setNotifyNum(mItemVO.getNotifyNum());

        if (mItemVO.getNotifyNum() <= 0) {
//            mBadgeView.hide();
            mVDot.setVisibility(GONE);
        } else {
//            mBadgeView.setText("");
//            mBadgeView.show();
            mVDot.setVisibility(VISIBLE);
        }
    }

}

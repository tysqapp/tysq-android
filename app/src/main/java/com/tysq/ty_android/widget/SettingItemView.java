package com.tysq.ty_android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tysq.ty_android.R;

/**
 * author       : frog
 * time         : 2019-09-16 16:12
 * desc         : 设置项
 * version      : 1.5.0
 */
public class SettingItemView extends LinearLayout {

    private static final int NONE_ICON = -1;
    private static final boolean ARROW_DEFAULT = true;

    private int mIconId = NONE_ICON;
    private String mTitle;
    private String mTip;
    private boolean mIsShowArrow = ARROW_DEFAULT;

    private TextView mTvTip;

    public SettingItemView(Context context) {
        this(context, null, 0);
    }

    public SettingItemView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SettingItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {

        if (attrs != null) {

            TypedArray typedArray = context
                    .getTheme()
                    .obtainStyledAttributes(attrs,
                            R.styleable.SettingItemView,
                            defStyleAttr,
                            0);

            mIconId = typedArray.getResourceId(R.styleable.SettingItemView_setting_icon, NONE_ICON);
            mTitle = typedArray.getString(R.styleable.SettingItemView_setting_title);
            mTip = typedArray.getString(R.styleable.SettingItemView_setting_tip);
            mIsShowArrow = typedArray.getBoolean(R.styleable.SettingItemView_setting_arrow, ARROW_DEFAULT);

            // 回收
            typedArray.recycle();
        }

        LayoutInflater layoutInflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (layoutInflater == null) {
            return;
        }
        View view = layoutInflater.inflate(R.layout.widget_setting_item, this);

        ImageView ivIcon = view.findViewById(R.id.iv_icon);
        if (mIconId == NONE_ICON) {
            ivIcon.setVisibility(GONE);
        } else {
            ivIcon.setVisibility(VISIBLE);
            ivIcon.setImageDrawable(ContextCompat.getDrawable(context, mIconId));
        }

        TextView tvTitle = view.findViewById(R.id.tv_title);
        if (mTitle != null) {
            tvTitle.setText(mTitle);
        }

        mTvTip = view.findViewById(R.id.tv_tip);
        if (mTip != null) {
            mTvTip.setText(mTip);
        }

        ImageView ivArrow = view.findViewById(R.id.iv_arrow);
        ivArrow.setVisibility(mIsShowArrow ? VISIBLE : GONE);

    }

    public void setTip(String tip) {
        this.mTip = tip;
        mTvTip.setText(tip);
    }
}

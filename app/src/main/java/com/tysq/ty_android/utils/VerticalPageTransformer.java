package com.tysq.ty_android.utils;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.View;

public class VerticalPageTransformer implements ViewPager.PageTransformer {
    @Override
    public void transformPage(@NonNull View page, float position) {
        if (position < -1) { // [-Infinity,-1)
            page.setAlpha(0);

        } else if (position <= 1) { // [-1,1]
            page.setAlpha(1);

            page.setTranslationX(page.getWidth() * -position);

            float yPosition = position * page.getHeight();
            page.setTranslationY(yPosition);

        } else { // (1,+Infinity]
            page.setAlpha(0);
        }
    }
}

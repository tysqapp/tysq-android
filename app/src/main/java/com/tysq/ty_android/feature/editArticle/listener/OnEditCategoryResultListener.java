package com.tysq.ty_android.feature.editArticle.listener;

import response.home.SubCategory;
import response.home.TopCategory;

/**
 * author       : frog
 * time         : 2019/5/8 上午10:47
 * desc         :
 * version      :
 */
public interface OnEditCategoryResultListener {

    void onCategoryChoose(TopCategory topCategory, SubCategory subCategory);

}

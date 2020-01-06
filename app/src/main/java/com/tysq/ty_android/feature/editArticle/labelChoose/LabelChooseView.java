package com.tysq.ty_android.feature.editArticle.labelChoose;

import com.bit.view.IView;

import java.util.List;

import response.LabelResp;

public interface LabelChooseView extends IView {

    void onLoadLabel(List<LabelResp.LabelItem> labelList);

}
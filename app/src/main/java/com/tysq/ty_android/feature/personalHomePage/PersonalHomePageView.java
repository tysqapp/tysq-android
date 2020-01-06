package com.tysq.ty_android.feature.personalHomePage;

import com.bit.view.IView;

import response.personal.PersonalPageResp;

public interface PersonalHomePageView extends IView {

    void getPersonalPage(PersonalPageResp value);
    void postAttention();
}

package com.tysq.ty_android.feature.person.personInfoChange;

import com.bit.view.IView;

public interface PersonInfoChangeView extends IView {


    void onUpdatePersonError();

    void onUpdatePerson(int resId);
}

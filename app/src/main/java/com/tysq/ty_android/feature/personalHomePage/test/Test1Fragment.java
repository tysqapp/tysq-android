package com.tysq.ty_android.feature.personalHomePage.test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bit.view.fragment.BitLazyFragment;
import com.tysq.ty_android.R;

import java.util.ArrayList;

/**
 * author       : frog
 * time         : 2019-09-18 15:11
 * desc         :
 * version      :
 */
public class Test1Fragment extends BitLazyFragment {

    public static Test1Fragment newInstance() {

        Bundle args = new Bundle();

        Test1Fragment fragment = new Test1Fragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected View onCreateFragmentView(@Nullable LayoutInflater inflater,
                                        @Nullable ViewGroup container,
                                        @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_test, container, false);
    }

    @Override
    protected void initView(View view) {
        ArrayList<String> content = new ArrayList<>();
        for (int i = 100; i > 2; --i) {
            content.add("jjj" + i);
        }
        TestAdapter adapter = new TestAdapter(getContext(), content);

        RecyclerView recycleView = view.findViewById(R.id.recycle_view);
        recycleView.setAdapter(adapter);
        recycleView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

}

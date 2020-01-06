package com.tysq.ty_android.feature;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.tysq.ty_android.feature.dataSource.DataSourceActivity;
import com.tysq.ty_android.feature.launch.LaunchActivity;
import com.tysq.ty_android.local.sp.NetCache;
import com.tysq.ty_android.utils.DataSourceChangeUtils;

/**
 * author       : frog
 * time         : 2019/4/25 上午10:11
 * desc         : 入口 Activity
 * version      : 1.3.0
 */
public class DoorActivity extends AppCompatActivity {

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, DoorActivity.class);

        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (NetCache.isEmpty()) {
            DataSourceActivity.startActivity(this);
        } else {
            DataSourceChangeUtils.initHtml();
            LaunchActivity.startActivity(this);
        }

        finish();

    }

}

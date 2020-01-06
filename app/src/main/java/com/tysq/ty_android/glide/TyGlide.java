package com.tysq.ty_android.glide;

import android.content.Context;
import android.support.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;
import com.tysq.ty_android.net.OkHttpHelper;

import java.io.InputStream;

@GlideModule
public final class TyGlide extends AppGlideModule {

    private static final String TAG = "TyGlide";

    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {

    }

    @Override
    public void registerComponents(@NonNull Context context,
                                   @NonNull Glide glide,
                                   @NonNull Registry registry) {
        OkHttpUrlLoader.Factory factory = new OkHttpUrlLoader.Factory(OkHttpHelper.getOkHttpInstance());

        registry.replace(GlideUrl.class, InputStream.class, factory);
    }


}
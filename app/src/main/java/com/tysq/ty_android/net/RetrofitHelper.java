package com.tysq.ty_android.net;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class RetrofitHelper {

    private Retrofit mRetrofit;

    // 域名
    private String domain;

    /**
     * 通过 域名 获取retrofit
     */
    RetrofitHelper(String domain, OkHttpClient okHttpClient) {
        this.domain = domain;

        this.mRetrofit = new Retrofit.Builder()
                .baseUrl(domain)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public <T> T create(Class<? extends T> clazz) {
        return this.mRetrofit.create(clazz);
    }

    public String getDomain() {
        return domain;
    }
}

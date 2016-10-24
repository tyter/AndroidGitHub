package com.lazier.githubsdk.service;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by zhoukaifeng(zhoukaifeng@meituan.com) on 16/10/20.
 */

public class ServiceFactory {
    private Retrofit mRetrofit;

    public ServiceFactory() {
        mRetrofit = new Retrofit
                .Builder()
                .baseUrl("http://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public <T> T create(Class<T> clazz) {
        return mRetrofit.create(clazz);
    }
}

package com.lazier.githubsdk.service;

import com.lazier.githubsdk.model.ResultOfSearch;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by zhoukaifeng(zhoukaifeng@meituan.com) on 16/10/20.
 */

public interface ServiceSearchRepositories {
    /**
     * ?q={key}&sort={sort}&order={order}&page={page}&per_page={per_page}
     */
    @GET("/search/repositories")
    Call<ResultOfSearch> search(@QueryMap Map<String, String> parameters);
}

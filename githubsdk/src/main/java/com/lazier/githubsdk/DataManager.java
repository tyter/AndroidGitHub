package com.lazier.githubsdk;

import android.text.TextUtils;

import com.lazier.githubsdk.model.RepositoryForSearch;
import com.lazier.githubsdk.model.ResultOfSearch;
import com.lazier.githubsdk.service.ServiceFactory;
import com.lazier.githubsdk.service.ServiceSearchRepositories;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by zhoukaifeng(zhoukaifeng@meituan.com) on 16/10/20.
 */

public class DataManager {
    private static final int DEF_PAGE_SIZE = 50;
    private static DataManager sInstance;
    private DataManager() {
        mServiceFactory = new ServiceFactory();
    }

    public synchronized static DataManager getIns() {
        if (sInstance == null) {
            sInstance = new DataManager();
        }
        return sInstance;
    }

    private ServiceFactory mServiceFactory;

    public void search(String key, final int page, final SearchListener listener) {

        if (TextUtils.isEmpty(key)) {
            return;
        }

        ServiceSearchRepositories service = mServiceFactory.create(ServiceSearchRepositories.class);

        Map<String, String> parameterMap = new HashMap<>();
        parameterMap.put("q", key);
        parameterMap.put("sort", "stars");
        parameterMap.put("page", String.valueOf(page));
        parameterMap.put("per_page", String.valueOf(DEF_PAGE_SIZE));
        Call<ResultOfSearch> call = service.search(parameterMap);
        call.enqueue(new Callback<ResultOfSearch>() {
            @Override
            public void onResponse(Call<ResultOfSearch> call, Response<ResultOfSearch> response) {
                if (listener == null || response == null) {
                    return ;
                }
                ResultOfSearch resultOfSearch = response.body();
                boolean end = true;
                if (resultOfSearch != null) {
                    end = DEF_PAGE_SIZE * page >= resultOfSearch.mTotalCount;
                    listener.onResponse(resultOfSearch.mRepositoryList, end);
                } else {
                    listener.onResponse(Collections.<RepositoryForSearch>emptyList(), end);
                }
            }

            @Override
            public void onFailure(Call<ResultOfSearch> call, Throwable t) {
                if (listener == null) {
                    return ;
                }

                listener.onError(t);
            }
        });
    }


    public interface SearchListener {
        void onResponse(List<RepositoryForSearch> result, boolean end);
        void onError(Throwable t);
    }
}

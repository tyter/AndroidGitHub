package com.lazier.githubsdk.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by zhoukaifeng(zhoukaifeng@meituan.com) on 16/10/20.
 */

public class ResultOfSearch {
    @SerializedName(value="total_count")
    public int mTotalCount;

    @SerializedName(value="incomplete_results")
    public boolean mInCompleteResult;

    @SerializedName(value="items")
    public List<RepositoryForSearch> mRepositoryList;
}

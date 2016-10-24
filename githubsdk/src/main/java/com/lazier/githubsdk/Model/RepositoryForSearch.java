package com.lazier.githubsdk.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zhoukaifeng(zhoukaifeng@meituan.com) on 16/10/20.
 */

public class RepositoryForSearch extends Repository {
    @SerializedName(value="score")
    public double mScore;
}

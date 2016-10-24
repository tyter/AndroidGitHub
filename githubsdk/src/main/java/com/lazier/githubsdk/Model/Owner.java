package com.lazier.githubsdk.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zhoukaifeng(zhoukaifeng@meituan.com) on 16/10/20.
 */

public class Owner {
    @SerializedName(value="login")
    public String mName;

    @SerializedName(value="avatar_url")
    public String mImageUrl;
}

package com.lazier.androidgithub;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by zhoukaifeng(zhoukaifeng@meituan.com) on 16/10/24.
 */

public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
    }
}

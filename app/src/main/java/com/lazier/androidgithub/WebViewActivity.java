package com.lazier.androidgithub;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.webkit.JsPromptResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhoukaifeng(zhoukaifeng@meituan.com) on 16/10/20.
 */

public class WebViewActivity extends AppCompatActivity {
    public static final String KEY_TITLE = "key_title";

    @BindView(R.id.wv_web_view)
    WebView mWebContainer;

    @BindView(R.id.tool_bar)
    Toolbar mToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        ButterKnife.bind(this);
        initActionBar();
        initView();
        initData();
    }

    private void initActionBar() {
        mToolBar.setTitleMarginStart(1);
        setSupportActionBar(mToolBar);
        ActionBar bar = getSupportActionBar();
        if (bar == null) {
            return ;
        }

        bar.setHomeButtonEnabled(true);
        bar.setDisplayShowHomeEnabled(true);
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setDisplayShowTitleEnabled(true);

        String title = getIntentTitle();
        if (!TextUtils.isEmpty(title)) {
            bar.setTitle(title);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        WebSettings settings = mWebContainer.getSettings();
        if (settings != null) {
            settings.setJavaScriptEnabled(true);
        }
        mWebContainer.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });

        mWebContainer.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }

            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
                return super.onJsPrompt(view, url, message, defaultValue, result);
            }
        });
    }

    private String getIntentTitle() {
        Intent intent = getIntent();
        return intent.getStringExtra(KEY_TITLE);
    }

    private void initData() {
        Intent intent = getIntent();
        Uri uri = intent.getData();
        mWebContainer.loadUrl(uri.toString());
    }
}

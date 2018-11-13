package com.smartbutler.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.smartbutler.R;
import com.smartbutler.utils.L;

/**
 * 微信精选详情页
 */

public class WeChatDetailActivity extends BaseActivity {
    // 进度
    private ProgressBar mProgressBar;
    // 网页
    private WebView mWebView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_we_chat_detail);

        initView();
    }

    // 初始化View
    private void initView() {
        mProgressBar = findViewById(R.id.mProgressBar);
        mWebView = findViewById(R.id.mWebView);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        final String url = intent.getStringExtra("url");
        L.i("url: " + url);

        // 设置title
        getSupportActionBar().setTitle(title);

        // 支持js
        mWebView.getSettings().setJavaScriptEnabled(true);
        // 支持缩放
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setBuiltInZoomControls(true);

        mWebView.setWebChromeClient(new WebViewClient());

        // 加载网页
        mWebView.loadUrl(url);

        // 本地显示
        mWebView.setWebViewClient(new android.webkit.WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(url);
                return true;
            }
        });
    }

    public class WebViewClient extends WebChromeClient {
        // 加载进度变化监听
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                mProgressBar.setVisibility(View.GONE);
            }
            super.onProgressChanged(view, newProgress);
        }
    }
}

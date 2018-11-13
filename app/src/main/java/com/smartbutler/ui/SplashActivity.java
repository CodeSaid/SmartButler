package com.smartbutler.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.smartbutler.R;
import com.smartbutler.utils.ShareUtils;
import com.smartbutler.utils.StaticClass;
import com.smartbutler.utils.UtilTools;

/**
 * 闪屏页面
 */

public class SplashActivity extends AppCompatActivity {

    /**
     * 1. 延时2000ms
     * 2. 判断应用是否是第一次运行
     * 3.自定义字体
     * 4.Activity全屏主题
     */

    private TextView tv_splash;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case StaticClass.HANDLER_SPLASH:
                    // 判断程序是否是第一次运行
                    if (isFirst()) {
                        // 是第一次运行就跳转到引导页
                        startActivity(new Intent(SplashActivity.this, GuideActivity.class));
                    } else {
                        // 不是第一次运行就跳转到主页面
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    }
                    finish();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initView();
    }

    /**
     * 初始化View
     */
    private void initView() {
        // 延时2000ms
        mHandler.sendEmptyMessageDelayed(StaticClass.HANDLER_SPLASH, 2000);
        tv_splash = findViewById(R.id.tv_splash);

        //设置字体
        UtilTools.setFont(this, tv_splash);

    }

    /**
     * 判断程序是否是第一次运行
     *
     * @return
     */
    public boolean isFirst() {
        boolean isFirst = ShareUtils.getBoolean(this, StaticClass.SHARE_IS_FIRST, true);
        if (isFirst) {
            // 第一次运行之后把 标记位改为false
            ShareUtils.putBoolean(this, StaticClass.SHARE_IS_FIRST, false);
            // 是第一次运行
            return true;
        } else {
            return false;
        }
    }

    // 禁止返回键
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}

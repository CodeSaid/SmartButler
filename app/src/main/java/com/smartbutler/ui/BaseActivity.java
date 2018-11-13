package com.smartbutler.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

/**
 * Activity的基类
 * 主要做的事情：
 *  1.统一的属性
 *  2.统一的接口
 *  3.统一的方法
 */

public class BaseActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 显示返回键
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    // 菜单栏操作
    // 返回上一个页面
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}

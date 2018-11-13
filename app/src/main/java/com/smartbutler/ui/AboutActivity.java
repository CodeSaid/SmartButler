package com.smartbutler.ui;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.smartbutler.R;

import java.util.ArrayList;
import java.util.List;

public class AboutActivity extends BaseActivity {
    private ListView lv_about;
    private List<String> mList = new ArrayList<>();
    private ArrayAdapter<String> mAdapter;

    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        // 去除阴影
         getSupportActionBar().setElevation(0);

        initView();
    }

    private void initView() {
        lv_about = findViewById(R.id.lv_about);

        mList.add("应用名: " + getString(R.string.app_name));
        mList.add("版本号: " + getVersion());
        mList.add("官网:www.codesaid.com");


        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mList);
        lv_about.setAdapter(mAdapter);
    }

    /**
     * 获取版本号
     */
    private String getVersion() {
        PackageManager manager = getPackageManager();
        try {
            PackageInfo packageInfo = manager.getPackageInfo(getPackageName(), 0);
            String versionName = packageInfo.versionName;
            return versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "未知";
        }

    }
}

package com.smartbutler.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.smartbutler.R;
import com.smartbutler.service.SmsService;
import com.smartbutler.utils.ShareUtils;
import com.smartbutler.utils.StaticClass;
import com.xys.libzxing.zxing.activity.CaptureActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 设置中心
 */

public class SettingActivity extends BaseActivity implements View.OnClickListener {
    // 语音播报开关
    private Switch sw_speak;

    // 语音播报开关
    private Switch sw_sms;

    private LinearLayout ll_update;
    private TextView tv_version;
    private String mVersionName;
    private int mVersionCode;

    private String url;

    //扫一扫
    private LinearLayout ll_scan;
    //扫描的结果
    private TextView tv_scan_result;
    //生成二维码
    private LinearLayout ll_qr_code;

    // 我的位置
    private LinearLayout ll_lbs;

    // 关于软件
    private LinearLayout ll_about;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initView();
    }

    private void initView() {
        sw_speak = findViewById(R.id.sw_speak);
        sw_speak.setOnClickListener(this);

        sw_sms = findViewById(R.id.sw_sms);
        sw_sms.setOnClickListener(this);

        ll_update = findViewById(R.id.ll_update);
        ll_update.setOnClickListener(this);
        tv_version = findViewById(R.id.tv_version);
        ll_scan = findViewById(R.id.ll_scan);
        ll_scan.setOnClickListener(this);
        tv_scan_result = findViewById(R.id.tv_scan_result);
        ll_qr_code = findViewById(R.id.ll_qr_code);
        ll_qr_code.setOnClickListener(this);
        ll_lbs = findViewById(R.id.ll_lbs);
        ll_lbs.setOnClickListener(this);
        ll_about = findViewById(R.id.ll_about);
        ll_about.setOnClickListener(this);

        boolean isSpeak = ShareUtils.getBoolean(getApplicationContext(), "isSpeak", false);
        sw_speak.setChecked(isSpeak);

        boolean isSms = ShareUtils.getBoolean(getApplicationContext(), "isSms", false);
        sw_sms.setChecked(isSms);

        getVersionNameCode();
        tv_version.setText("检测版本: " + mVersionName);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sw_speak:
                //  切换语音播报功能
                sw_speak.setSelected(!sw_speak.isSelected());
                // 保存状态
                ShareUtils.putBoolean(getApplicationContext(), "isSpeak", sw_speak.isChecked());
                break;
            case R.id.sw_sms:
                // 切换相反状态
                sw_sms.setSelected(!sw_sms.isSelected());
                // 保存状态
                ShareUtils.putBoolean(getApplicationContext(), "isSms", sw_sms.isChecked());
                if (sw_sms.isChecked()) {
                    startService(new Intent(SettingActivity.this, SmsService.class));
                } else {
                    stopService(new Intent(SettingActivity.this, SmsService.class));
                }
                break;
            case R.id.ll_update:
                RxVolley.get(StaticClass.CHECK_UPDATE_URL, new HttpCallback() {
                    @Override
                    public void onSuccess(String t) {
                        processData(t);
                    }
                });
                break;
            case R.id.ll_scan:
                //打开扫描界面扫描条形码或二维码
                Intent openCameraIntent = new Intent(this, CaptureActivity.class);
                startActivityForResult(openCameraIntent, 0);
                break;
            case R.id.ll_qr_code:
                startActivity(new Intent(SettingActivity.this, QrCodeActivity.class));
                break;
            case R.id.ll_lbs:
                startActivity(new Intent(SettingActivity.this, LocationActivity.class));
                break;
            case R.id.ll_about:
                startActivity(new Intent(SettingActivity.this, AboutActivity.class));
                break;
        }
    }

    /**
     * 解析json数据
     *
     * @param t
     */
    private void processData(String t) {
        try {
            JSONObject jsonObject = new JSONObject(t);
            int code = jsonObject.getInt("versionCode");
            url = jsonObject.getString("url");
            if (code > mVersionCode) {
                showUpdateDialog(jsonObject.getString("content"));
            } else {
                Toast.makeText(this, "当前以是最新版本", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示更新
     *
     * @param content
     */
    private void showUpdateDialog(String content) {
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
        builder.setTitle("有新版本了");
        builder.setMessage(content);
        builder.setPositiveButton("更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 执行更新操作
                Intent intent = new Intent(SettingActivity.this, UpdateActivity.class);
                intent.putExtra("url", url);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }

    /**
     * 获取版本号/code
     */
    private void getVersionNameCode() {
        PackageManager manager = getPackageManager();
        try {
            PackageInfo packageInfo = manager.getPackageInfo(getPackageName(), 0);
            mVersionName = packageInfo.versionName;
            mVersionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("result");
            tv_scan_result.setText(scanResult);
        }
    }
}

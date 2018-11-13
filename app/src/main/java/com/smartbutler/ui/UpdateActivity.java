package com.smartbutler.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.TextView;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.client.ProgressListener;
import com.kymjs.rxvolley.http.VolleyError;
import com.kymjs.rxvolley.toolbox.FileUtils;
import com.smartbutler.R;

import java.io.File;


/**
 * 下载新版本
 */

public class UpdateActivity extends BaseActivity {
    private TextView tv_size;
    private String mUrl;

    private String path;

    //正在下载
    public static final int HANDLER_LODING = 10001;
    //下载完成
    public static final int HANDLER_OK = 10002;
    //下载失败
    public static final int HANDLER_NO = 10003;

    // 进度条
    private NumberProgressBar mNumberProgressBar;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HANDLER_LODING:
                    // 更新进度
                    Bundle bundle = msg.getData();
                    long transferredBytes = bundle.getLong("transferredBytes");
                    long totalSize = bundle.getLong("totalSize");
                    tv_size.setText(transferredBytes + "/" + totalSize);

                    //设置进度

                    // 30%  --- 100%   number_progress_bar.setpar(30)
                    //  5200.0 / 52000.0  10.0%  10 / 100  = 100%
                    mNumberProgressBar.setProgress((int) (((float) transferredBytes / (float) totalSize) * 100));
                    break;
                case HANDLER_OK:
                    tv_size.setText("下载成功");
                    // 启动安装程序
                    startInstallApk();
                    break;
                case HANDLER_NO:
                    tv_size.setText("下载失败");
                    break;
            }
        }
    };

    /**
     * 安装程序
     */
    private void startInstallApk() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setDataAndType(Uri.fromFile(new File(path)), "application/vnd.android.package-archive");
        startActivity(intent);
        finish();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        initView();
    }

    private void initView() {
        tv_size = findViewById(R.id.tv_size);
        mNumberProgressBar = findViewById(R.id.number_progressbar);
        mNumberProgressBar.setMax(100);

        path = FileUtils.getSDCardPath() + "/" + System.currentTimeMillis() + ".apk";

        //下载
        mUrl = getIntent().getStringExtra("url");
        if (!TextUtils.isEmpty(mUrl)) {
            //下载
            RxVolley.download(path, mUrl, new ProgressListener() {
                @Override
                public void onProgress(long transferredBytes, long totalSize) {
                    // L.i("transferredBytes:" + transferredBytes + "totalSize:" + totalSize);
                    Message message = new Message();
                    message.what = HANDLER_LODING;
                    Bundle bundle = new Bundle();
                    bundle.putLong("transferredBytes", transferredBytes);
                    bundle.putLong("totalSize", totalSize);
                    message.setData(bundle);
                    mHandler.sendMessage(message);
                }
            }, new HttpCallback() {
                @Override
                public void onSuccess(String t) {
                    mHandler.sendEmptyMessage(HANDLER_OK);
                }

                @Override
                public void onFailure(VolleyError error) {
                    mHandler.sendEmptyMessage(HANDLER_NO);
                }
            });
        }

    }
}

package com.smartbutler.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.IBinder;
import android.telephony.SmsMessage;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.smartbutler.R;
import com.smartbutler.utils.L;
import com.smartbutler.utils.StaticClass;
import com.smartbutler.view.DispatchLinearLayout;

/**
 * 短信提醒服务
 */

public class SmsService extends Service implements View.OnClickListener {

    private static final String SYSTEM_DIALOGS_REASON_KEY = "reason";
    private static final String SYSTEM_DIALOGS_HOME_KEY = "homekey";

    private SmsReceiver mSmsReceiver;
    private HomeWatchReceiver mHomeWatchReceiver;

    // 发件人号码
    private String smsPhone;
    // 短信内容
    private String smsContent;

    private WindowManager mWindowManager;
    // 布局参数
    private WindowManager.LayoutParams mLayoutParams;

    private DispatchLinearLayout mView;

    private TextView tv_phone;
    private TextView tv_content;
    private Button btn_send_sms;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        // 注册广播
        mSmsReceiver = new SmsReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(StaticClass.SMS_ACTION);
        filter.setPriority(Integer.MAX_VALUE);
        registerReceiver(mSmsReceiver, filter);

        mHomeWatchReceiver = new HomeWatchReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        registerReceiver(mHomeWatchReceiver, intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 注销广播
        unregisterReceiver(mSmsReceiver);
        unregisterReceiver(mHomeWatchReceiver);
    }

    // 短信广播
    public class SmsReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (StaticClass.SMS_ACTION.equals(action)) {
                // 接受到短信
                L.i("来短信了");
                // 获取短信内容返回一个Object数组
                Object[] objects = (Object[]) intent.getExtras().get("pdus");
                for (Object object : objects) {
                    // 把数组元素转换为短信对象
                    SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) object);
                    smsPhone = smsMessage.getDisplayOriginatingAddress();
                    smsContent = smsMessage.getMessageBody();
                    L.i("发件人: " + smsPhone + "短信内容: " + smsContent);
                    showWindow();
                }
            }
        }
    }

    // 窗口提示
    private void showWindow() {
        // 获取管理者
        mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        // 获取布局参数
        mLayoutParams = new WindowManager.LayoutParams();
        // 定义宽高
        mLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        mLayoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        // 定义标记
        mLayoutParams.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
        // 定义格式
        mLayoutParams.format = PixelFormat.TRANSLUCENT;
        // 定义类型
        mLayoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        // 加载布局
        mView = (DispatchLinearLayout) View.inflate(getApplicationContext(), R.layout.sms_item, null);

        // 初始化控件
        tv_phone = mView.findViewById(R.id.tv_phone);
        tv_content = mView.findViewById(R.id.tv_content);
        btn_send_sms = mView.findViewById(R.id.btn_send_sms);
        btn_send_sms.setOnClickListener(this);

        // 设置数据
        tv_phone.setText("发件人: " + smsPhone);
        tv_content.setText(smsContent);

        // 添加view到窗口
        mWindowManager.addView(mView, mLayoutParams);
        mView.setDispatchEventListener(mDispatchEventListener);
    }

    private DispatchLinearLayout.DispatchEventListener mDispatchEventListener = new DispatchLinearLayout.DispatchEventListener() {
        @Override
        public boolean dispatchEvent(KeyEvent event) {
            // 判断是否是 返回键
            if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                L.i("按下back键");
                if (mView.getParent() != null) {
                    mWindowManager.removeView(mView);
                }
                return true;
            }
            return false;
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send_sms:
                sendSms();
                // 隐藏窗口
                if (mView.getParent() != null) {
                    mWindowManager.removeView(mView);
                }
                break;
        }
    }

    /**
     * 回复短信
     */
    private void sendSms() {
        Uri uri = Uri.parse("smsto:" + smsPhone);
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        // 设置启动模式
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("sms_body", "");
        startActivity(intent);
    }

    // 监听Home键的广播
    class HomeWatchReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action == Intent.ACTION_CLOSE_SYSTEM_DIALOGS) {
                String reason = intent.getStringExtra(SYSTEM_DIALOGS_REASON_KEY);
                if (SYSTEM_DIALOGS_HOME_KEY.equals(reason)) {
                    L.i("我点击了home键");
                    if (mView.getParent() != null) {
                        mWindowManager.removeView(mView);
                    }
                }
            }
        }
    }
}

package com.smartbutler.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.smartbutler.MainActivity;
import com.smartbutler.R;
import com.smartbutler.entity.MyUser;
import com.smartbutler.utils.ShareUtils;
import com.smartbutler.view.CustomDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * 登录界面
 */

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.et_username)
    EditText et_username;
    @BindView(R.id.et_password)
    EditText et_password;
    @BindView(R.id.cb_remember_password)
    CheckBox cb_remember_password;
    @BindView(R.id.btn_login)
    Button btn_login;
    @BindView(R.id.btn_register)
    Button btn_register;
    @BindView(R.id.tv_forget)
    TextView tv_forget;

    private CustomDialog mCustomDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        // 注册按钮点击事件
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        // 登录按钮点击事件
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获取账号密码
                String username = et_username.getText().toString().trim();
                String password = et_password.getText().toString().trim();
                // 判断是否为空
                if (!TextUtils.isEmpty(username) & !TextUtils.isEmpty(password)) {
                    mCustomDialog.show();
                    //登录
                    final MyUser user = new MyUser();
                    user.setUsername(username);
                    user.setPassword(password);
                    user.login(new SaveListener<MyUser>() {
                        @Override
                        public void done(MyUser myUser, BmobException e) {
                            mCustomDialog.dismiss();
                            // 判断结果
                            if (e == null) {
                                // 登录成功
                                // 判断邮箱是否验证
                                if (user.getEmailVerified()) {
                                    // 跳转
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    finish();
                                } else {
                                    // 让用户前往邮箱验证
                                    Toast.makeText(LoginActivity.this, "请先前往邮箱验证", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            } else {
                                // 登录失败
                                Toast.makeText(LoginActivity.this, "登录失败: " + e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(LoginActivity.this, "账号密码不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 设置CheckBox选中的状态
        boolean isCheck = ShareUtils.getBoolean(this, "remember_password", false);
        cb_remember_password.setChecked(isCheck);
        // 判断是否是选中状态
        if (isCheck) {
            // 设置账号密码
            String username = ShareUtils.getString(this, "username", "");
            String password = ShareUtils.getString(this, "password", "");
            et_username.setText(username);
            et_password.setText(password);
        } else {
            et_username.setText(" ");
            et_password.setText(" ");
        }

        // 忘记密码按钮的点击事件
        tv_forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ForgetPasswordActivity.class));
            }
        });

        // Dialog
        mCustomDialog = new CustomDialog(this, 100,100,R.layout.dialog_loading,
                R.style.Theme_dialog, Gravity.CENTER, R.style.pop_anim_style);
        // 设置屏幕外点击无效
        mCustomDialog.setCancelable(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 保存状态
        ShareUtils.putBoolean(this, "remember_password", cb_remember_password.isChecked());

        // 是否记住密码
        if (cb_remember_password.isChecked()) {
            // 记住密码
            ShareUtils.putString(this, "username", et_username.getText().toString().trim());
            ShareUtils.putString(this, "password", et_password.getText().toString().trim());
        } else {
            // 不记住密码
            ShareUtils.deleteShare(this, "username");
            ShareUtils.deleteShare(this, "password");
        }
    }
}

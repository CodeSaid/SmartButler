package com.smartbutler.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.smartbutler.R;
import com.smartbutler.entity.MyUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 忘记密码重置界面
 */

public class ForgetPasswordActivity extends BaseActivity {
    @BindView(R.id.et_old_password)
    EditText mEtOldPassword;
    @BindView(R.id.et_new_password)
    EditText mEtNewPassword;
    @BindView(R.id.et_new_password_two)
    EditText mEtNewPasswordTwo;
    @BindView(R.id.et_email)
    EditText mEtEmail;
    @BindView(R.id.btn_forget_password)
    Button mBtnForgetPassword;
    @BindView(R.id.et_update_password)
    Button et_update_password;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        ButterKnife.bind(this);

        initView();
    }

    /**
     * 初始化View
     */
    private void initView() {
        mBtnForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = mEtEmail.getText().toString().trim();
                if (!TextUtils.isEmpty(email)) {
                    // 判断邮箱格式是否正确
                    if (emailValidation(email)) {
                        // 发送邮件
                        MyUser.resetPasswordByEmail(email, new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    Toast.makeText(ForgetPasswordActivity.this, "邮件已发送至: " + email, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ForgetPasswordActivity.this, "邮件发送失败", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(ForgetPasswordActivity.this, "邮箱格式输入有误", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ForgetPasswordActivity.this, "邮箱不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });

        et_update_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPassword = mEtOldPassword.getText().toString().trim();
                String newPassword = mEtNewPassword.getText().toString().trim();
                String newPasswordTwo = mEtNewPasswordTwo.getText().toString().trim();
                if (!TextUtils.isEmpty(oldPassword) & !TextUtils.isEmpty(newPassword) & !TextUtils.isEmpty(newPasswordTwo)) {
                    // 判断两次输入的密码是否一致
                    if (newPassword.equals(newPasswordTwo)) {
                        MyUser.updateCurrentUserPassword(oldPassword, newPassword, new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    Toast.makeText(ForgetPasswordActivity.this, "重置密码成功", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(ForgetPasswordActivity.this, "重置密码失败: " + e.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        // 两次输入的密码不一致
                    }
                } else {
                    Toast.makeText(ForgetPasswordActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 验证邮箱格式是否正确
     *
     * @param email
     * @return
     */
    public boolean emailValidation(String email) {
        String regex = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
        return email.matches(regex);
    }
}

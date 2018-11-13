package com.smartbutler.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.smartbutler.R;
import com.smartbutler.entity.MyUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * 注册界面
 */

public class RegisterActivity extends BaseActivity {


    @BindView(R.id.et_username)
    EditText et_username;
    @BindView(R.id.et_age)
    EditText et_age;
    @BindView(R.id.et_desc)
    EditText et_desc;
    @BindView(R.id.mRadioGroup)
    RadioGroup mRadioGroup;
    @BindView(R.id.et_password)
    EditText et_password;
    @BindView(R.id.et_password_two)
    EditText et_passwordTwo;
    @BindView(R.id.et_email)
    EditText et_email;
    @BindView(R.id.btn_registered)
    Button btn_registered;

    // 性别
    private boolean isGender = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        btn_registered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获取用户输入的的信息
                String username = et_username.getText().toString().trim();
                String age = et_age.getText().toString().trim();
                String desc = et_desc.getText().toString().trim();
                String password = et_password.getText().toString().trim();
                String passwordTwo = et_passwordTwo.getText().toString().trim();
                String email = et_email.getText().toString().trim();

                // 判断是否为空
                if (!TextUtils.isEmpty(username) & !TextUtils.isEmpty(age)
                        & !TextUtils.isEmpty(password) & !TextUtils.isEmpty(passwordTwo)
                        & !TextUtils.isEmpty(email)) {

                    // 判断两次输入的密码是否一致
                    if (password.equals(passwordTwo)) {
                        // 判断性别
                        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(RadioGroup group, int checkedId) {
                                if (checkedId == R.id.rb_boy) {
                                    isGender = true;
                                } else if (checkedId == R.id.rb_girl) {
                                    isGender = false;
                                }
                            }
                        });
                        // 判断简介是否为空
                        if (TextUtils.isEmpty(desc)) {
                            desc = "这个人很懒，什么都没有留下!";
                        }

                        // 注册
                        MyUser myUser = new MyUser();
                        myUser.setUsername(username);
                        myUser.setPassword(password);
                        myUser.setEmail(email);
                        myUser.setDesc(desc);
                        myUser.setAge(Integer.parseInt(age));
                        myUser.setSex(isGender);

                        myUser.signUp(new SaveListener<MyUser>() {
                            @Override
                            public void done(MyUser myUser, BmobException e) {
                                if (e == null) {
                                    Toast.makeText(RegisterActivity.this, "注册成功!", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(RegisterActivity.this, "注册失败" + e.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(RegisterActivity.this, "两次输入的密码不一致,请重新输入!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "输入框不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

package com.smartbutler.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.smartbutler.R;
import com.smartbutler.entity.MyUser;
import com.smartbutler.ui.AddressActivity;
import com.smartbutler.ui.CourierActivity;
import com.smartbutler.ui.LoginActivity;
import com.smartbutler.utils.L;
import com.smartbutler.utils.ShareUtils;
import com.smartbutler.utils.UtilTools;
import com.smartbutler.view.CustomDialog;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 个人中心
 */

public class UserFragment extends Fragment implements View.OnClickListener {
    private Button btn_exit_user;
    private TextView tv_user;

    private EditText et_username;
    private EditText et_sex;
    private EditText et_age;
    private EditText et_desc;
    //更新按钮
    private Button btn_update_user;
    //圆形头像
    private CircleImageView profile_image;

    private CustomDialog mCustomDialog;

    private Button btn_camera;
    private Button btn_picture;
    private Button btn_cancel;

    // 快递查询
    private TextView tv_courier;

    private TextView tv_address;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, null);
        findView(view);
        return view;
    }

    private void findView(View view) {
        btn_exit_user = view.findViewById(R.id.btn_exit_user);
        tv_user = view.findViewById(R.id.tv_user);
        et_username = view.findViewById(R.id.et_username);
        et_sex = view.findViewById(R.id.et_sex);
        et_age = view.findViewById(R.id.et_age);
        et_desc = view.findViewById(R.id.et_desc);

        btn_update_user = view.findViewById(R.id.btn_update_user);
        profile_image = view.findViewById(R.id.profile_image);
        tv_courier = view.findViewById(R.id.tv_courier);
        tv_address = view.findViewById(R.id.tv_address);

        btn_exit_user.setOnClickListener(this);
        tv_user.setOnClickListener(this);
        btn_update_user.setOnClickListener(this);
        profile_image.setOnClickListener(this);
        tv_courier.setOnClickListener(this);
        tv_address.setOnClickListener(this);

        // 还原图像
        UtilTools.getImageToShare(getActivity(), profile_image);

        //初始化dialog
        mCustomDialog = new CustomDialog(getActivity(), 0, 0,
                R.layout.dialog_photo, R.style.pop_anim_style, Gravity.BOTTOM, 0);

        // 设置屏幕外点击无效
        mCustomDialog.setCancelable(false);
        btn_camera = mCustomDialog.findViewById(R.id.btn_camera);
        btn_picture = mCustomDialog.findViewById(R.id.btn_picture);
        btn_cancel = mCustomDialog.findViewById(R.id.btn_cancel);

        btn_camera.setOnClickListener(this);
        btn_picture.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);

        // 默认EditText是不可点击的
        setEnable(false);

        // 设置具体的值
        MyUser user = BmobUser.getCurrentUser(MyUser.class);
        et_username.setText(user.getUsername());
        if (user.isSex()) {
            et_sex.setText("男");
        } else {
            et_sex.setText("女");
        }
        et_age.setText(user.getAge() + "");
        et_desc.setText(user.getDesc());
    }

    /**
     * 控制焦点
     *
     * @param enable
     */
    private void setEnable(boolean enable) {
        et_username.setEnabled(enable);
        et_sex.setEnabled(enable);
        et_age.setEnabled(enable);
        et_desc.setEnabled(enable);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 退出登录
            case R.id.btn_exit_user:
                // 清楚缓存用户对象
                MyUser.logOut();
                // 现在的currentUser是 null
                BmobUser currentUser = MyUser.getCurrentUser();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
                break;
            // 编辑资料
            case R.id.tv_user:
                btn_update_user.setVisibility(View.VISIBLE);
                setEnable(true);
                break;
            // 修改资料按钮
            case R.id.btn_update_user:
                // 获取输入框的值
                String username = et_username.getText().toString().trim();
                String sex = et_sex.getText().toString().trim();
                String age = et_age.getText().toString().trim();
                String desc = et_desc.getText().toString().trim();

                // 判断是否为空
                if (!TextUtils.isEmpty(username) & !TextUtils.isEmpty(sex) & !TextUtils.isEmpty(age)) {
                    // 更新属性
                    MyUser user = new MyUser();
                    user.setUsername(username);
                    // 性别
                    if (sex.equals("男")) {
                        user.setSex(true);
                    } else {
                        user.setSex(false);
                    }
                    user.setAge(Integer.parseInt(age));
                    if (!TextUtils.isEmpty(desc)) {
                        user.setDesc(desc);
                    } else {
                        user.setDesc("这个人很懒,什么都没有留下");
                    }
                    BmobUser bmobUser = BmobUser.getCurrentUser();
                    user.update(bmobUser.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                // 修改成功
                                setEnable(false);
                                btn_update_user.setVisibility(View.GONE);
                                Toast.makeText(getActivity(), "资料修改成功", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), "资料修改失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(getActivity(), "输入框不能为空", Toast.LENGTH_SHORT).show();
                }
                break;
            // 圆形图像
            case R.id.profile_image:
                mCustomDialog.show();
                break;
            case R.id.btn_camera:
                toCamera();
                break;
            case R.id.btn_picture:
                toPicture();
                break;
            case R.id.btn_cancel:
                mCustomDialog.dismiss();
                break;
            // 快递查询
            case R.id.tv_courier:
                startActivity(new Intent(getActivity(), CourierActivity.class));
                break;
            // 归属地查询
            case R.id.tv_address:
                startActivity(new Intent(getActivity(), AddressActivity.class));
                break;
        }
    }

    public static final String PHOTO_IMAGE_FILE_NAME = "fileImage.jpg";
    public static final int CAMERA_REQUEST_CODE = 101;
    public static final int PICTURE_REQUEST_CODE = 102;
    public static final int RESULT_REQUEST_CODE = 103;
    private File tempFile = null;

    /**
     * 跳转到相机
     */
    private void toCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 判断内存卡是否可用，可用就进行存储
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(new File(Environment.getExternalStorageDirectory(), PHOTO_IMAGE_FILE_NAME)));
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
        mCustomDialog.dismiss();
    }

    /**
     * 跳转到相册
     */
    private void toPicture() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICTURE_REQUEST_CODE);
        mCustomDialog.dismiss();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != getActivity().RESULT_CANCELED) {
            switch (requestCode) {
                //相册数据
                case PICTURE_REQUEST_CODE:
                    startPhotoZoom(data.getData());
                    break;
                //相机数据
                case CAMERA_REQUEST_CODE:
                    tempFile = new File(Environment.getExternalStorageDirectory(), PHOTO_IMAGE_FILE_NAME);
                    startPhotoZoom(Uri.fromFile(tempFile));
                    break;
                case RESULT_REQUEST_CODE:
                    //有可能点击舍弃
                    if (data != null) {
                        //拿到图片设置
                        setImageToView(data);
                        //既然已经设置了图片，我们原先的就应该删除
                        if (tempFile != null) {
                            tempFile.delete();
                        }
                    }
                    break;
            }
        }
    }


    //裁剪
    private void startPhotoZoom(Uri uri) {
        if (uri == null) {
            L.e("uri == null");
            return;
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        //设置裁剪
        intent.putExtra("crop", "true");
        //裁剪宽高比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        //裁剪图片的质量
        intent.putExtra("outputX", 320);
        intent.putExtra("outputY", 320);
        //发送数据
        intent.putExtra("return-data", true);
        startActivityForResult(intent, RESULT_REQUEST_CODE);
    }


    //设置图片
    private void setImageToView(Intent data) {
        Bundle bundle = data.getExtras();
        if (bundle != null) {
            Bitmap bitmap = bundle.getParcelable("data");
            profile_image.setImageBitmap(bitmap);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 保存图像
        UtilTools.putImageToShare(getActivity(), profile_image);
    }
}

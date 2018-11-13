package com.smartbutler.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.smartbutler.R;
import com.smartbutler.utils.L;
import com.smartbutler.utils.StaticClass;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

/**
 * 归属地页面
 */

public class AddressActivity extends BaseActivity implements View.OnClickListener {
    // 输入框
    private EditText et_number;

    // 公司logo
    private ImageView iv_company;

    // 结果
    private TextView tv_result;

    private Button btn_0;
    private Button btn_1;
    private Button btn_2;
    private Button btn_3;
    private Button btn_4;
    private Button btn_5;
    private Button btn_6;
    private Button btn_7;
    private Button btn_8;
    private Button btn_9;
    private Button btn_del;
    private Button btn_query;

    // 标记位
    private boolean flag = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        initView();
    }

    private void initView() {
        et_number = findViewById(R.id.et_number);
        iv_company = findViewById(R.id.iv_company);
        tv_result = findViewById(R.id.tv_result);

        btn_0 = findViewById(R.id.btn_0);
        btn_0.setOnClickListener(this);
        btn_1 = findViewById(R.id.btn_1);
        btn_1.setOnClickListener(this);
        btn_2 = findViewById(R.id.btn_2);
        btn_2.setOnClickListener(this);
        btn_3 = findViewById(R.id.btn_3);
        btn_3.setOnClickListener(this);
        btn_4 = findViewById(R.id.btn_4);
        btn_4.setOnClickListener(this);
        btn_5 = findViewById(R.id.btn_5);
        btn_5.setOnClickListener(this);
        btn_6 = findViewById(R.id.btn_6);
        btn_6.setOnClickListener(this);
        btn_7 = findViewById(R.id.btn_7);
        btn_7.setOnClickListener(this);
        btn_8 = findViewById(R.id.btn_8);
        btn_8.setOnClickListener(this);
        btn_9 = findViewById(R.id.btn_9);
        btn_9.setOnClickListener(this);
        btn_del = findViewById(R.id.btn_del);
        btn_del.setOnClickListener(this);
        btn_query = findViewById(R.id.btn_query);
        btn_query.setOnClickListener(this);

        // 长按事件
        btn_del.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                et_number.setText("");
                return false;
            }
        });
    }

    // 点击事件
    @Override
    public void onClick(View v) {
        // 获取输入框内容
        String str = et_number.getText().toString().trim();

        switch (v.getId()) {
            case R.id.btn_0:
            case R.id.btn_1:
            case R.id.btn_2:
            case R.id.btn_3:
            case R.id.btn_4:
            case R.id.btn_5:
            case R.id.btn_6:
            case R.id.btn_7:
            case R.id.btn_8:
            case R.id.btn_9:
                if (flag) {
                    str = "";
                    et_number.setText("");
                    flag = false;
                }
                et_number.setText(str + ((Button) v).getText());
                //  移动光标
                et_number.setSelection(str.length() + 1);
                break;
            case R.id.btn_del:
                if (!TextUtils.isEmpty(str) && str.length() > 0) {
                    // 删除数据
                    et_number.setText(str.substring(0, str.length() - 1));
                    //  移动光标
                    et_number.setSelection(str.length() - 1);
                }
                break;
            case R.id.btn_query:
                if (!TextUtils.isEmpty(str)) {
                    getPhone(str);
                }
                break;
        }

    }

    // 获取归属地
    private void getPhone(String str) {
        // 请求示例：http://apis.juhe.cn/mobile/get?phone=13429667914&key=您申请的KEY
        String url = "http://apis.juhe.cn/mobile/get?phone=" + str + "&key=" + StaticClass.ADDRESS_KEY;
        RxVolley.get(url, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                //Toast.makeText(AddressActivity.this, t, Toast.LENGTH_SHORT).show();
                L.i(t);
                processData(t);
            }
        });
    }

    /**
     * 解析数据
     *
     * @param t
     */
    @SuppressLint("SetTextI18n")
    private void processData(String t) {
        try {
            JSONObject jsonObject = new JSONObject(t);
            JSONObject jsonResult = jsonObject.getJSONObject("result");
            String province = jsonResult.getString("province");
            String city = jsonResult.getString("city");
            String areacode = jsonResult.getString("areacode");
            String zip = jsonResult.getString("zip");
            String company = jsonResult.getString("company");
            String card = jsonResult.getString("card");

            tv_result.setText("归属地:" + province + city + "\n"
                    + "区号:" + areacode + "\n"
                    + "邮编:" + zip + "\n"
                    + "运营商:" + company + "\n"
                    + "类型:" + card);

            //图片显示
            switch (company) {
                case "移动":
                    iv_company.setBackgroundResource(R.drawable.china_mobile);
                    break;
                case "联通":
                    iv_company.setBackgroundResource(R.drawable.china_unicom);
                    break;
                case "电信":
                    iv_company.setBackgroundResource(R.drawable.china_telecom);
                    break;
            }
            flag = true;
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}

package com.smartbutler.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.smartbutler.R;
import com.smartbutler.adapter.CourierAdapter;
import com.smartbutler.entity.CourierData;
import com.smartbutler.utils.L;
import com.smartbutler.utils.StaticClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 快递页面
 */

public class CourierActivity extends BaseActivity {

    private Button btn_query;
    private EditText et_name;
    private EditText et_number;

    private ListView mListView;

    private List<CourierData> mList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courier);

        initView();
    }

    private void initView() {
        btn_query = findViewById(R.id.btn_query);
        et_name = findViewById(R.id.et_name);
        et_number = findViewById(R.id.et_number);
        mListView = findViewById(R.id.mListView);

        /**
         * 查询按钮点击事件
         */
        btn_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获取用户输入的数据
                String name = et_name.getText().toString().trim();
                String number = et_number.getText().toString().trim();

                // 请求示例：http://v.juhe.cn/exp/index?key=key&com=sf&no=575677355677
                String url = "http://v.juhe.cn/exp/index?key=" + StaticClass.COURIER_KEY
                        + "&com= " + name + "&no=" + number;

                // 判断是否为空
                if (!TextUtils.isEmpty(name) & !TextUtils.isEmpty(number)) {
                    // 请求数据接口返回Json数据
                    RxVolley.get(url, new HttpCallback() {
                        @Override
                        public void onSuccess(String t) {
                            //Toast.makeText(CourierActivity.this, t, Toast.LENGTH_SHORT).show();
                            // L.i("json: " + t);

                            // 解析数据
                            processData(t);
                        }
                    });
                } else {
                    Toast.makeText(CourierActivity.this, "输入框不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 解析Json数据
     *
     * @param json
     */
    private void processData(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONObject jsonResult = jsonObject.getJSONObject("result");
            JSONArray jsonArray = jsonResult.getJSONArray("list");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsons = (JSONObject) jsonArray.get(i);

                CourierData data = new CourierData();
                data.setDatetime(jsons.getString("datetime"));
                data.setRemark(jsons.getString("remark"));
                data.setZone(jsons.getString("zone"));
                mList.add(data);
            }
            // 倒序
            Collections.reverse(mList);
            CourierAdapter adapter = new CourierAdapter(CourierActivity.this, mList);
            // 设置adapter
            mListView.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

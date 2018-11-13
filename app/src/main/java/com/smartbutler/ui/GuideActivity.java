package com.smartbutler.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.smartbutler.MainActivity;
import com.smartbutler.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 引导页
 */

public class GuideActivity extends AppCompatActivity {

    private ViewPager mViewPager;

    // 容器
    private List<View> mViewList = new ArrayList<>();
    private View viewOne, viewTwo, viewThree;

    // 小圆点指示器
    private ImageView iv_point_one, iv_point_two, iv_point_three;

    // 跳过
    private ImageView iv_black;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        initView();
    }

    private void initView() {
        mViewPager = findViewById(R.id.mViewPager);
        iv_point_one = findViewById(R.id.iv_point_one);
        iv_point_two = findViewById(R.id.iv_point_two);
        iv_point_three = findViewById(R.id.iv_point_three);
        iv_black = findViewById(R.id.iv_black);

        // 跳过按钮点击事件
        iv_black.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GuideActivity.this, MainActivity.class));
                finish();
            }
        });

        // 设置默认图片
        setPointImage(true, false, false);

        viewOne = View.inflate(getApplicationContext(), R.layout.pager_item_one, null);
        viewTwo = View.inflate(getApplicationContext(), R.layout.pager_item_two, null);
        viewThree = View.inflate(getApplicationContext(), R.layout.pager_item_three, null);

        Button btn_start_main = viewThree.findViewById(R.id.btn_start_main);
        // 进入主页按钮点击事件
        btn_start_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GuideActivity.this, MainActivity.class));
                finish();
            }
        });

        mViewList.add(viewOne);
        mViewList.add(viewTwo);
        mViewList.add(viewThree);

        // 设置适配器
        mViewPager.setAdapter(new GuideAdapter());

        // 监听ViewPager滑动
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        setPointImage(true, false, false);
                        iv_black.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        setPointImage(false, true, false);
                        iv_black.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        setPointImage(false, false, true);
                        iv_black.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private class GuideAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return mViewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ((ViewPager) container).addView(mViewList.get(position));
            return mViewList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView(mViewList.get(position));
        }
    }

    /**
     * 设置小圆点的选中效果
     *
     * @param isCheckOne
     * @param isCheckTwo
     * @param isCheckThree
     */
    private void setPointImage(boolean isCheckOne, boolean isCheckTwo, boolean isCheckThree) {
        if (isCheckOne) {
            iv_point_one.setBackgroundResource(R.drawable.point_on);
        } else {
            iv_point_one.setBackgroundResource(R.drawable.point_off);
        }
        if (isCheckTwo) {
            iv_point_two.setBackgroundResource(R.drawable.point_on);
        } else {
            iv_point_two.setBackgroundResource(R.drawable.point_off);
        }

        if (isCheckThree) {
            iv_point_three.setBackgroundResource(R.drawable.point_on);
        } else {
            iv_point_three.setBackgroundResource(R.drawable.point_off);
        }

    }
}

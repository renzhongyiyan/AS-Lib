package com.iyuba.core.iyumooc.teacher.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.iyuba.core.common.util.DensityUtil;
import com.iyuba.core.common.util.TakePictureUtil;
import com.iyuba.core.iyumooc.teacher.adapter.TabFragmentPagerAdapter;
import com.iyuba.core.teacher.activity.FilterPayQuesType;
import com.iyuba.lib.R;

import java.io.File;
import java.lang.reflect.Field;

/**
 * 功能：付费问答列表
 * 作者：renzhy on 17/6/18 10:14
 * 邮箱：renzhongyigoo@gmail.com
 */
public class PayQListContainerFragment extends Fragment {
    private static final String TAG = "PayQListContainerFragment";
    private static final int QUESTION_FILTER_REQUEST_TYPE = 1;
    private Context mContext;

    ImageView ivQuesNotification;
    ImageView ivQuesSelectType;
    TabLayout tabLayout;
    ViewPager viewPager;
    TabFragmentPagerAdapter pagerAdapter;

    int qSortType = 0;

    public static PayQListContainerFragment newInstance(){
        PayQListContainerFragment fragment = new PayQListContainerFragment();
        return fragment;
    }

    public void initWidget(View view){
        ivQuesNotification = (ImageView) view.findViewById(R.id.iv_ques_state_notifica);
        ivQuesSelectType = (ImageView) view.findViewById(R.id.iv_qlist_more);
        tabLayout = (TabLayout) view.findViewById(R.id.tablayout_payqlist);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager_payqlist);
    }

    public void setViews(){
        pagerAdapter = new TabFragmentPagerAdapter(getActivity().getSupportFragmentManager(),mContext);
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        setUpIndicatorWidth(tabLayout,50,50);

        ivQuesNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        ivQuesSelectType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(FilterPayQuesType.getIntent2Me(mContext),QUESTION_FILTER_REQUEST_TYPE);
            }
        });

        if(pagerAdapter != null){
            pagerAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payqlist_container,container,false);
        initWidget(view);
        setViews();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if(pagerAdapter != null){
            pagerAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch ( requestCode ) {
            case QUESTION_FILTER_REQUEST_TYPE:
                pagerAdapter.notifyDataSetChanged();
//                if (data != null) {
//                    Bundle quesBundle = data.getExtras();
//                    qSortType = quesBundle.getInt("qSortType", -1);
//                }
//                Toast.makeText(mContext, "qSortType:"+qSortType, Toast.LENGTH_SHORT).show();

                break;
        }
    }

    private void setUpIndicatorWidth(TabLayout tabLayout, int marginLeft, int marginRight) {
        Class<?> tabLayoutClass = tabLayout.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayoutClass.getDeclaredField("mTabStrip");
            tabStrip.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        LinearLayout layout = null;
        try {
            if (tabStrip != null) {
                layout = (LinearLayout) tabStrip.get(tabLayout);
            }
            for (int i = 0; i < layout.getChildCount(); i++) {
                View child = layout.getChildAt(i);
                child.setPadding(0, 0, 0, 0);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    params.setMarginStart(DensityUtil.dip2px(getActivity(), marginLeft));
                    params.setMarginEnd(DensityUtil.dip2px(getActivity(), marginRight));
                }
                child.setLayoutParams(params);
                child.invalidate();
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}

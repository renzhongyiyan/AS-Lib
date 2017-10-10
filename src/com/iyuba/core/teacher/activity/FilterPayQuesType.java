package com.iyuba.core.teacher.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.iyuba.configation.ConfigManager;
import com.iyuba.core.common.widget.flowtaglayout.FlowTagLayout;
import com.iyuba.core.common.widget.flowtaglayout.OnTagSelectListener;
import com.iyuba.core.iyumooc.teacher.adapter.TagAdapter;
import com.iyuba.lib.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：renzhy on 17/7/3 14:07
 * 邮箱：renzhongyigoo@gmail.com
 */
public class FilterPayQuesType extends Activity{

    private LinearLayout mLlBack;
//    private ImageView mIvBack;
    private FlowTagLayout mAppTypeTagLayout;
    private FlowTagLayout mAbilityTagLayout;
    private FlowTagLayout mSortTagLayout;

    private TagAdapter<String> mAppTypeAdapter;
    private TagAdapter<String> mAbilityAdapter;
    private TagAdapter<String> mSortAdapter;

    private int qAbilityType = 0;//问题的类型qtype
    private int qAppType = 0;
    private int qSortType = 0;

    public static Intent getIntent2Me(Context context) {
        Intent intent = new Intent(context, FilterPayQuesType.class);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_payques_type);

        mLlBack = (LinearLayout) findViewById(R.id.ll_iv_back);
//        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mAppTypeTagLayout = (FlowTagLayout) findViewById(R.id.app_type_flow_layout);
        mAbilityTagLayout = (FlowTagLayout) findViewById(R.id.ability_type_flow_layout);
        mSortTagLayout = (FlowTagLayout) findViewById(R.id.sort_type_flow_layout);

        mLlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("qAbilityType",qAbilityType);
                intent.putExtra("qAppType",qAppType);
                intent.putExtra("qSortType",qSortType);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        mAppTypeAdapter = new TagAdapter<>(this);
        mAppTypeTagLayout.setTagCheckedMode(FlowTagLayout.FLOW_TAG_CHECKED_SINGLE);
        mAppTypeTagLayout.setAdapter(mAppTypeAdapter);
        mAppTypeTagLayout.setOnTagSelectListener(new OnTagSelectListener() {
            @Override
            public void onItemSelect(FlowTagLayout parent, List<Integer> selectedList) {
                if (selectedList != null && selectedList.size() > 0) {
                    StringBuilder sb = new StringBuilder();
                    for (int i : selectedList) {
                        sb.append(parent.getAdapter().getItem(i));
                        qAppType = i;
                        if(i == 0){
                            ConfigManager.Instance().putInt("quesAppType", 0);
                        }else{
                            ConfigManager.Instance().putInt("quesAppType", i+100);
                        }
                    }
                    Snackbar.make(parent, "应用类型:" + sb.toString(), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }else{
                    Snackbar.make(parent, "没有选择标签", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    qAppType = 0;
                }
            }
        });

        mAbilityAdapter = new TagAdapter<>(this);
        mAbilityTagLayout.setTagCheckedMode(FlowTagLayout.FLOW_TAG_CHECKED_SINGLE);
        mAbilityTagLayout.setAdapter(mAbilityAdapter);
        mAbilityTagLayout.setOnTagSelectListener(new OnTagSelectListener() {
            @Override
            public void onItemSelect(FlowTagLayout parent, List<Integer> selectedList) {
                if (selectedList != null && selectedList.size() > 0) {
                    StringBuilder sb = new StringBuilder();
                    for (int i : selectedList) {
                        sb.append(parent.getAdapter().getItem(i));
                        qAbilityType = i;
                        ConfigManager.Instance().putInt("quesAbilityType", i);
                    }
                    Snackbar.make(parent, "考察能力:" + sb.toString(), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }else{
                    Snackbar.make(parent, "没有选择标签", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    qAbilityType = 0;
                }
            }
        });

        mSortAdapter = new TagAdapter<>(this);
        mSortTagLayout.setTagCheckedMode(FlowTagLayout.FLOW_TAG_CHECKED_SINGLE);
        mSortTagLayout.setAdapter(mSortAdapter);
        mSortTagLayout.setOnTagSelectListener(new OnTagSelectListener() {
            @Override
            public void onItemSelect(FlowTagLayout parent, List<Integer> selectedList) {
                if (selectedList != null && selectedList.size() > 0) {
                    StringBuilder sb = new StringBuilder();
                    for (int i : selectedList) {
                        sb.append(parent.getAdapter().getItem(i));
                        qSortType = i;
                        ConfigManager.Instance().putInt("quesSort",i);
                    }
                    Snackbar.make(parent, "排序方式:" + sb.toString(), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }else{
                    Snackbar.make(parent, "没有选择标签", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    qSortType = 0;
                }
            }
        });

        initAppTypeData();
        initAbilityTypeData();
        initSortTypeData();

    }



//    private void getQuesTypeData(){
//        qAbilityType= ConfigManager.Instance().loadInt("quesAbilityType");
//        qSortType = ConfigManager.Instance().loadInt("quesSort");
//
//        if(ConfigManager.Instance().loadInt("quesAppType") !=0 ){
//            qAppType=ConfigManager.Instance().loadInt("quesAppType") - 100;
//        }else{
//            qAppType=ConfigManager.Instance().loadInt("quesAppType");
//        }
//
//    }
//
//    private void setSortState(){
//        getQuesTypeData();
//    }

    private void initAppTypeData() {
        List<String> dataSource = new ArrayList<>();
        dataSource.add("全部");
        dataSource.add("VOA");
        dataSource.add("BBC");
        dataSource.add("听歌");
        dataSource.add("CET4");
        dataSource.add("CET6");
        dataSource.add("托福");
        dataSource.add("N1");
        dataSource.add("N2");
        dataSource.add("N3");
        dataSource.add("微课");
        dataSource.add("雅思");
        dataSource.add("初中");
        dataSource.add("高中");
        dataSource.add("考研");
        dataSource.add("新概念");
        dataSource.add("走遍美国");
        dataSource.add("英语头条");
        mAppTypeAdapter.onlyAddAll(dataSource);
    }

    private void initAbilityTypeData() {
        List<String> dataSource = new ArrayList<>();
        dataSource.add("全部");
        dataSource.add("口语");
        dataSource.add("听力");
        dataSource.add("阅读");
        dataSource.add("写作");
        dataSource.add("翻译");
        dataSource.add("单词");
        dataSource.add("语法");
        dataSource.add("其他");
        mAbilityAdapter.onlyAddAll(dataSource);
    }

    private void initSortTypeData(){
        List<String> dataSource = new ArrayList<>();
        dataSource.add("精华");
        mSortAdapter.onlyAddAll(dataSource);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("qAbilityType",qAbilityType);
        intent.putExtra("qAppType",qAppType);
        intent.putExtra("qSortType",qSortType);
        setResult(RESULT_OK, intent);
        super.onBackPressed();

    }
}

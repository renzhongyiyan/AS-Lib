package com.iyuba.core.me.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.iyuba.configation.ConfigManager;
import com.iyuba.core.common.widget.NoScrollViewPager;
import com.iyuba.core.me.adapter.RankingPagerAdapter;
import com.iyuba.lib.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：renzhy on 17/1/5 14:20
 * 邮箱：renzhongyigoo@gmail.com
 */
public class StudyRankingActivity extends AppCompatActivity {

	private TabLayout mTabLayout;
	private NoScrollViewPager mViewPager;
	private RankingPagerAdapter mPagerAdapter;

	private TabLayout.Tab studyRanking;
	private TabLayout.Tab testingRanking;

	private Button backBtn;
	private TextView tvRankingCycle;
	private Spinner spRankingCycle;
	private int rankingCycle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_study_ranking);

		//初始化视图
		initViews();
		loadDataForSpinner();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private void initViews() {

		backBtn = (Button) findViewById(R.id.ranking_button_back);
		spRankingCycle = (Spinner) findViewById(R.id.spinner_ranking_cycle);
		tvRankingCycle = (TextView) findViewById(R.id.tv_ranking_cycle);
		//使用适配器将ViewPager与Fragment绑定在一起
		mViewPager= (NoScrollViewPager) findViewById(R.id.viewPager);
		mPagerAdapter = new RankingPagerAdapter(getSupportFragmentManager());
		mViewPager.setAdapter(mPagerAdapter);

		//将TabLayout与ViewPager绑定在一起
		mTabLayout = (TabLayout) findViewById(R.id.tabLayout);
		mTabLayout.setupWithViewPager(mViewPager);

		//指定Tab的位置
		studyRanking = mTabLayout.getTabAt(0);
		testingRanking = mTabLayout.getTabAt(1);

		backBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		spRankingCycle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				rankingCycle = position;
				switch (rankingCycle){
					case 0:

						handler.sendEmptyMessage(0);
						break;
					case 1:

						handler.sendEmptyMessage(1);
						break;
					case 2:

						handler.sendEmptyMessage(2);
						break;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	private void loadDataForSpinner(){
		List<String> spinnerList = getSpinnerList();
		ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(
				this,R.layout.spinner_ranking_style,R.id.tv_ranking_spinner_style,spinnerList);
		mAdapter.setDropDownViewResource(R.layout.spinner_ranking_dropdown_style);
		spRankingCycle.setAdapter(mAdapter);
	}

	public static List<String> getSpinnerList(){
		List<String> list = new ArrayList<>();
		list.add("今天");
		list.add("本周");
		list.add("本月");
		return list;
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what){
				case 0:
					tvRankingCycle.setText("当日数据0时重计");
					mPagerAdapter.setTimeType(0);
					mPagerAdapter.notifyDataSetChanged();
					break;
				case 1:
					tvRankingCycle.setText("本周数据周末0时重计");
					mPagerAdapter.setTimeType(1);
					mPagerAdapter.notifyDataSetChanged();
					break;
				case 2:
					tvRankingCycle.setText("本月数据月末0时重计");
					mPagerAdapter.setTimeType(2);
					mPagerAdapter.notifyDataSetChanged();
					break;
			}
		}
	};


}

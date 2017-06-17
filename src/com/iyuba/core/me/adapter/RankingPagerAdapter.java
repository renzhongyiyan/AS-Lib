package com.iyuba.core.me.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.iyuba.core.me.fragment.LearnRankFragment;
import com.iyuba.core.me.fragment.TestRankFragment;

/**
 * 作者：renzhy on 17/1/5 15:15
 * 邮箱：renzhongyigoo@gmail.com
 */
public class RankingPagerAdapter extends FragmentPagerAdapter {

	private int timeType = 0;
	private String[] mTitles = new String[]{"学习排行", "做题排行"};

	public RankingPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		if (position == 1) {
			return new TestRankFragment();
		}
		return new LearnRankFragment();
	}

	@Override
	public int getCount() {
		return mTitles.length;
	}

	@Override
	public int getItemPosition(Object object) {
		if (object instanceof LearnRankFragment) {
			((LearnRankFragment) object).updateLearnRank(timeType);
		} else if (object instanceof TestRankFragment) {
			((TestRankFragment) object).updateTestRank(timeType);
		}
		return super.getItemPosition(object);
//		return POSITION_NONE;
	}

	public void setTimeType(int type){
		timeType = type;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return mTitles[position];
	}
}

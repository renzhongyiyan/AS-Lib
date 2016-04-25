package com.iyuba.core.teacher.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.iyuba.core.teacher.fragment.HelpFragment;
import com.iyuba.lib.R;

public class HelpFragmentAdapter extends FragmentPagerAdapter {
	protected static final int[] CONTENT = new int[] { R.drawable.iyubaclient_help1,
			R.drawable.iyubaclient_help2, R.drawable.iyubaclient_help3,
			R.drawable.iyubaclient_help4, R.drawable.iyubaclient_help5 };

	public HelpFragmentAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		return HelpFragment.newInstance(CONTENT[position]);
	}

	@Override
	public int getCount() {
		return CONTENT.length;
	}
}

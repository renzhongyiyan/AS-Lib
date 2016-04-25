package com.iyuba.core.microclass.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.iyuba.core.microclass.fragment.HelpFragment;
import com.iyuba.lib.R;

public class HelpFragmentAdapter extends FragmentPagerAdapter {
	protected static final int[] CONTENT = new int[] { R.drawable.help1,
			R.drawable.help2, R.drawable.help3, R.drawable.help4, R.drawable.help5 };

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

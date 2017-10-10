package com.iyuba.core.iyumooc.teacher.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.iyuba.core.iyumooc.teacher.fragment.PayQListFragment;
import com.iyuba.lib.R;

import static com.iyuba.configation.RuntimeManager.getString;

/**
 * 作者：renzhy on 17/6/18 11:36
 * 邮箱：renzhongyigoo@gmail.com
 */
public class TabFragmentPagerAdapter extends FragmentPagerAdapter{

    final int PAGE_COUNT = 2;
    private Context mContext;
    private static final int REWARD_TYPE = 0;
    private static final int DISCUSS_TYPE = 1;

    private String[] tabTitles = {
            getString(R.string.reward_area),
            getString(R.string.discuss_area)
    };

    public TabFragmentPagerAdapter(FragmentManager fm, Context mContext) {
        super(fm);
        this.mContext = mContext;
    }

    @Override
    public Fragment getItem(int position) {
        int adapterType = 0;
        switch (position){
            case 0:
                adapterType = REWARD_TYPE;
                break;
            case 1:
                adapterType = DISCUSS_TYPE;
                break;
        }
        return PayQListFragment.newInstance(adapterType);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        PayQListFragment payQListFragment = (PayQListFragment) super.instantiateItem(container, position);
        return payQListFragment;
    }
}

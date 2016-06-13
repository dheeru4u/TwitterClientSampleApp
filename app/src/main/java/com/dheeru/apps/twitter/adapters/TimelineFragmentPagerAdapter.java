package com.dheeru.apps.twitter.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.dheeru.apps.twitter.fragments.TimelineFrag;

/**
 * Created by dkthaku on 6/8/16.
 */
public class TimelineFragmentPagerAdapter extends SmartFragmentStatePagerAdapter {

    private static final int PAGE_COUNT = 2;
    private static final String TAB_TITLES[] = new String[]{"HOME", "MENTIONS"};
    private Context mContext;

    public TimelineFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;

    }

    @Override
    public Fragment getItem(int position) {
        String timeline = TAB_TITLES[position];
        return TimelineFrag.newInstance(timeline, -1);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TAB_TITLES[position];
    }
}


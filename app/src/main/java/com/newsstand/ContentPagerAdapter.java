package com.newsstand;


import androidx.collection.SparseArrayCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.cornerstonehospice.android.fragments.BMIFragment;
import com.cornerstonehospice.android.fragments.MELDFragment;
import com.cornerstonehospice.android.fragments.ScalesFragment;

/**
 * Created by ramesh on 7/20/15.
 */
public class ContentPagerAdapter extends FragmentPagerAdapter {

    private SparseArrayCompat<ScrollTabHolder> mScrollTabHolders;
    private final String[] TITLES = { "BMI", "MELD", "SCALES"};
    private ScrollTabHolder mListener;

    public ContentPagerAdapter(FragmentManager fm) {
        super(fm);
        mScrollTabHolders = new SparseArrayCompat<ScrollTabHolder>();
    }

    public void setTabHolderScrollingContent(ScrollTabHolder listener) {
        mListener = listener;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLES[position];
    }

    @Override
    public int getCount() {
        return TITLES.length;
    }

    @Override
    public Fragment getItem(int position) {
        ScrollTabHolderFragment fragment;
        fragment = (ScrollTabHolderFragment) SampleListFragment.newInstance(position);
        switch (position) {
            case 0:
                fragment = (ScrollTabHolderFragment) BMIFragment.newInstance(position);
                break;
            case 1:
                fragment = (ScrollTabHolderFragment) MELDFragment.newInstance(position);
                break;

        }

        if(position == 2) {
            return ScalesFragment.newInstance();
        }

        mScrollTabHolders.put(position, fragment);
        if (mListener != null) {
            fragment.setScrollTabHolder(mListener);
        }

        return fragment;
    }

    public SparseArrayCompat<ScrollTabHolder> getScrollTabHolders() {
        return mScrollTabHolders;
    }

}

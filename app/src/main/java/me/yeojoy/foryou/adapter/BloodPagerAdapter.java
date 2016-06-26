package me.yeojoy.foryou.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import me.yeojoy.foryou.BloodPressureFragment;
import me.yeojoy.foryou.BloodSugarFragment;

/**
 * Created by yeojoy on 2016. 6. 26..
 */
public class BloodPagerAdapter extends FragmentStatePagerAdapter {
    public BloodPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        Fragment fragment;
        switch (i) {
            default:
                fragment = new BloodPressureFragment();
                break;
            case 1:
                fragment = new BloodSugarFragment();
                break;
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            default:
                return BloodPressureFragment.VIEW_TAG;

            case 1:
                return BloodSugarFragment.VIEW_TAG;

        }
    }
}

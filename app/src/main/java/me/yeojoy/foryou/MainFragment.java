package me.yeojoy.foryou;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.yeojoy.foryou.view.SlidingTabLayout;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainFragment extends Fragment {

    public static final String TAG = MainFragment.class.getSimpleName();

    private Context mContext;

    private ViewPager mVpTabs;
    private SlidingTabLayout mStlTabTitle;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mVpTabs = (ViewPager) view.findViewById(R.id.vp_pager);
        DemoCollectionPagerAdapter adapter
                = new DemoCollectionPagerAdapter(
                ((MainActivity)mContext).getSupportFragmentManager());
        mVpTabs.setAdapter(adapter);

        mStlTabTitle = (SlidingTabLayout) view.findViewById(R.id.stl_title_strip);
        mStlTabTitle.setViewPager(mVpTabs);
    }

    public class DemoCollectionPagerAdapter extends FragmentStatePagerAdapter {
        public DemoCollectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            Fragment fragment = null;
            switch (i) {
                default:
                    fragment = new BloodPressureFragment();
                    break;
            }

            // TODO
            // arguments를 보낼 필요가 있다면.
            // 여기서 fragment.setArguments(bundle); 해준다.

            return fragment;
        }

        @Override
        public int getCount() {
            return 1;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                default:
                    return BloodPressureFragment.VIEW_TAG;

            }
        }
    }
}

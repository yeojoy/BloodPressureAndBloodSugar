package me.yeojoy.foryou;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.parse.ParseUser;

import me.yeojoy.foryou.backup.BackupActivity;
import me.yeojoy.foryou.config.Consts;
import me.yeojoy.foryou.info.InfoActivity;
import me.yeojoy.foryou.input.InputActivity;
import me.yeojoy.foryou.view.SlidingTabLayout;
import me.yeojoy.library.log.MyLog;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainFragment extends Fragment implements Consts {

    public static final String TAG = MainFragment.class.getSimpleName();

    private Context mContext;

    private ViewPager mVpTabs;
    private SlidingTabLayout mStlTabTitle;

    private Toolbar mToolbar;

    private FloatingActionMenu mFamInputMenu;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent = null;
        if (id == R.id.action_info) {
            intent = new Intent(mContext, InfoActivity.class);
        } else if (id == R.id.action_backup) {
            intent = new Intent(mContext, BackupActivity.class);
        }

        if (intent != null) {
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((MainActivity) getActivity()).setSupportActionBar(mToolbar);

        mVpTabs = (ViewPager) view.findViewById(R.id.vp_pager);
        DemoCollectionPagerAdapter adapter
                = new DemoCollectionPagerAdapter(
                ((MainActivity)mContext).getSupportFragmentManager());
        mVpTabs.setAdapter(adapter);

        mStlTabTitle = (SlidingTabLayout) view.findViewById(R.id.stl_title_strip);
        mStlTabTitle.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tab_indicator);
            }

            @Override
            public int getDividerColor(int position) {
                return 0;
            }
        });
        mStlTabTitle.setViewPager(mVpTabs);

        mFamInputMenu = (FloatingActionMenu) view.findViewById(R.id.fam_input_menu);
        mFamInputMenu.showMenuButton(true);
        mFamInputMenu.setClosedOnTouchOutside(true);

        FloatingActionButton fabInputBloodPressure = (FloatingActionButton)
                mFamInputMenu.findViewById(R.id.fab_input_blood_pressure);
        FloatingActionButton fabInputBloodSugar = (FloatingActionButton)
                mFamInputMenu.findViewById(R.id.fab_input_blood_sugar);
        fabInputBloodPressure.setOnClickListener(mBtnClickListener);
        fabInputBloodSugar.setOnClickListener(mBtnClickListener);

        MyLog.d(TAG, "+++++++++++++++++++++++++++++++++++++++++++++++++++");
        MyLog.d(TAG, "USER Object ID : " + ParseUser.getCurrentUser()
                .getObjectId());
        MyLog.d(TAG, "+++++++++++++++++++++++++++++++++++++++++++++++++++");
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

    private View.OnClickListener mBtnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.fab_input_blood_pressure: {

                    Intent intent = new Intent(mContext, InputActivity.class);
                    intent.putExtra(KEY_INPUT_TYPE, INPUT_TYPE_BLOOD_PRESSURE);
                    startActivity(intent);
                    break;
                }
                case R.id.fab_input_blood_sugar: {
                    Intent intent = new Intent(mContext, InputActivity.class);
                    intent.putExtra(KEY_INPUT_TYPE, INPUT_TYPE_BLOOD_SUGAR);
                    startActivity(intent);
                    break;
                }
            }

            mFamInputMenu.close(false);
        }
    };
}

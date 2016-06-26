package me.yeojoy.foryou;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.parse.ParseUser;

import me.yeojoy.foryou.adapter.BloodPagerAdapter;
import me.yeojoy.foryou.backup.BackupActivity;
import me.yeojoy.foryou.info.InfoActivity;
import me.yeojoy.foryou.input.InputActivity;
import me.yeojoy.foryou.view.SlidingTabLayout;
import me.yeojoy.library.log.MyLog;

public class MainActivity extends BaseActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private Context mContext;

    private ViewPager mVpTabs;
    private SlidingTabLayout mStlTabTitle;

    private Toolbar mToolbar;

    private FloatingActionMenu mFamInputMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mVpTabs = (ViewPager) findViewById(R.id.vp_pager);
        BloodPagerAdapter adapter
                = new BloodPagerAdapter(
                ((MainActivity)mContext).getSupportFragmentManager());
        mVpTabs.setAdapter(adapter);

        mStlTabTitle = (SlidingTabLayout) findViewById(R.id.stl_title_strip);
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

        mFamInputMenu = (FloatingActionMenu) findViewById(R.id.fam_input_menu);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
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

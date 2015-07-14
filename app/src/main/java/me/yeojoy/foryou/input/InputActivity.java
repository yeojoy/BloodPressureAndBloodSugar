package me.yeojoy.foryou.input;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import me.yeojoy.foryou.BaseActivity;
import me.yeojoy.foryou.R;

/**
 * Created by yeojoy on 15. 7. 3..
 */
public class InputActivity extends BaseActivity {

    private static final String TAG = InputActivity.class.getSimpleName();

    private int mCurrentInputType = 0;

    private Context mContext;
    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        mContext = this;
        mFragmentManager = getFragmentManager();

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent() != null) {

            mCurrentInputType = getIntent().getIntExtra(KEY_INPUT_TYPE, 0);

            if (mCurrentInputType == 0) {
                finish();
                return;
            }

            FragmentTransaction transaction = mFragmentManager.beginTransaction();
            switch (mCurrentInputType) {
                case INPUT_TYPE_BLOOD_PRESSURE: {
                    InputBloodPressureFragment fragment = new InputBloodPressureFragment();

                    transaction.add(R.id.container, fragment).commit();
                    getSupportActionBar().setTitle(getResources()
                            .getString(R.string.title_input_blood_pressure));
                    break;
                }
                case INPUT_TYPE_BLOOD_SUGAR: {
                    InputBloodSugarFragment fragment = new InputBloodSugarFragment();

                    transaction.add(R.id.container, fragment).commit();
                    getSupportActionBar().setTitle(getResources()
                            .getString(R.string.title_input_blood_sugar));
                    break;
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home :
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

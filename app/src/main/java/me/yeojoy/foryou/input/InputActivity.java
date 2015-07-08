package me.yeojoy.foryou.input;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import me.yeojoy.foryou.R;
import me.yeojoy.foryou.config.Consts;

/**
 * Created by yeojoy on 15. 7. 3..
 */
public class InputActivity extends AppCompatActivity implements Consts {

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

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
                    break;
                }
                case INPUT_TYPE_BLOOD_SUGAR: {
                    InputBloodSugarFragment fragment = new InputBloodSugarFragment();

                    transaction.add(R.id.container, fragment).commit();
                    break;
                }
            }
        }

    }
}

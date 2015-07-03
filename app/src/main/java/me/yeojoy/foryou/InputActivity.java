package me.yeojoy.foryou;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by yeojoy on 15. 7. 3..
 */
public class InputActivity extends AppCompatActivity {

    private static final String TAG = InputActivity.class.getSimpleName();

    public static final String KEY_INPUT_TYPE = "input_type";

    public static final int INPUT_TYPE_BLOOD_PRESSURE   = 0x000001;
    public static final int INPUT_TYPE_BLOOD_SUGAR      = 0x000002;

    private int mCurrentInputType = 0;

    private Context mContext;
    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        mFragmentManager = getFragmentManager();

        if (getIntent() != null) {

            mCurrentInputType = getIntent().getIntExtra(KEY_INPUT_TYPE, 0);

            if (mCurrentInputType == 0) {
                finish();
                return;
            }

            FragmentTransaction transaction = mFragmentManager.beginTransaction();
            switch (mCurrentInputType) {
                case INPUT_TYPE_BLOOD_PRESSURE:
                    // transaction.add(framgnet).commit();
                    break;

                case INPUT_TYPE_BLOOD_SUGAR:
                    // transaction.add(framgnet).commit();
                    break;
            }
        }

    }
}

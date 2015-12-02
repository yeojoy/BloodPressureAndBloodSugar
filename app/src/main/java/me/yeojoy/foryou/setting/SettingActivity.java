package me.yeojoy.foryou.setting;

import android.os.Bundle;

import me.yeojoy.foryou.BaseActivity;
import me.yeojoy.foryou.R;

/**
 * Created by yeojoy on 15. 9. 10..
 */
public class SettingActivity extends BaseActivity {
    private static final String TAG = SettingActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
    }
}

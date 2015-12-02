package me.yeojoy.foryou.setting;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;

import me.yeojoy.foryou.ForYouApplication;
import me.yeojoy.foryou.R;

/**
 * Created by yeojoy on 15. 9. 10..
 */
public class SettingFragment extends PreferenceFragment
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = SettingFragment.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);

    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.pref_key_setting_default_category))) {
            if (sharedPreferences.getBoolean(key, false)) {
                ForYouApplication.mIsPregnant = true;
            } else {
                ForYouApplication.mIsPregnant = false;
            }
        }
    }
}


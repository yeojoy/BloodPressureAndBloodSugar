package me.yeojoy.foryou.input;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;

import me.yeojoy.foryou.config.Consts;
import me.yeojoy.foryou.R;
import me.yeojoy.foryou.utils.CommonUtils;

/**
 * A placeholder fragment containing a simple view.
 */
public class InputBloodPressureFragment extends Fragment implements Consts {

    private static final String TAG = InputBloodPressureFragment.class.getSimpleName();

    private Context mContext;

    private EditText mEtBloodPressureMax, mEtBloodPressureMin, mEtBloodPulse;
    private Button mBtnSave;

    private float mBloodPressureMax = 0f;
    private float mBloodPressureMin = 0f;
    private float mBloodPulse = 0f;

    private View.OnClickListener mButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (verifyEditText()) {
                saveBloodPressure();
            } else {
                Toast.makeText(mContext, R.string.toast_warning_data_not_valid,
                        Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void saveBloodPressure() {
        SavingAsyncTask task = new SavingAsyncTask();
        task.execute(mBloodPressureMax, mBloodPressureMin, mBloodPulse);
    }

    private boolean verifyEditText() {
        boolean isValid = true;

        try {
            mBloodPressureMax
                    = Float.parseFloat(mEtBloodPressureMax.getText().toString());
        } catch (NumberFormatException e) {
            Log.e(TAG, mEtBloodPressureMax.getText().toString() +
                    " is not number type.");
            isValid = false;
        }

        try {
            mBloodPressureMin
                    = Float.parseFloat(mEtBloodPressureMin.getText().toString());
        } catch (NumberFormatException e) {
            Log.e(TAG, mEtBloodPressureMin.getText().toString() +
                    " is not number type.");
            isValid = false;
        }

        try {
            mBloodPulse
                    = Float.parseFloat(mEtBloodPulse.getText().toString());
        } catch (NumberFormatException e) {
            Log.e(TAG, mEtBloodPulse.getText().toString() +
                    " is not number type.");
            isValid = false;
        }

        return isValid;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_input_blood_pressure, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mEtBloodPressureMax = (EditText) view.findViewById(R.id.et_blood_pressure_max);
        mEtBloodPressureMin = (EditText) view.findViewById(R.id.et_blood_pressure_min);
        mEtBloodPulse = (EditText) view.findViewById(R.id.et_blood_pulse);

        mBtnSave = (Button) view.findViewById(R.id.btn_save_blood_pressure);
        mBtnSave.setOnClickListener(mButtonClickListener);

    }

    private class SavingAsyncTask extends AsyncTask<Float, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Float... params) {
            ParseObject pressure = new ParseObject(PARSE_BLOOD_PRESSURE_TABLE);
            pressure.put(PARSE_BP_COLUMN_MAX, params[0]);
            pressure.put(PARSE_BP_COLUMN_MIN, params[1]);
            pressure.put(PARSE_BP_COLUMN_PULSE, params[2]);

            Boolean isSuccessful = true;
            try {
                pressure.save();
            } catch (ParseException e) {
                Log.e(TAG, e.getMessage());
                isSuccessful = false;
            }

            return isSuccessful;
        }

        @Override
        protected void onPostExecute(Boolean isSuccessful) {
            super.onPostExecute(isSuccessful);

            if (isSuccessful) {
                // 초기화
                mEtBloodPressureMax.setText("");
                mEtBloodPressureMin.setText("");
                mEtBloodPulse.setText("");

                mBloodPressureMax = 0f;
                mBloodPressureMin = 0f;
                mBloodPulse = 0f;

                CommonUtils.hideKeyboard(mContext, mEtBloodPulse);

                ((InputActivity) mContext).finish();
            } else {
                Toast.makeText(mContext,
                        R.string.toast_warning_fail_save_blood_pressure,
                        Toast.LENGTH_SHORT).show();
            }
        }
    }


}

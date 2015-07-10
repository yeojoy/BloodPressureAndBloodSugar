package me.yeojoy.foryou.input;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import me.yeojoy.foryou.R;
import me.yeojoy.foryou.config.Consts;
import me.yeojoy.foryou.config.ParseConsts;
import me.yeojoy.foryou.model.BloodSugar;
import me.yeojoy.foryou.utils.CommonUtils;
import my.lib.MyLog;

/**
 * A placeholder fragment containing a simple view.
 */
public class InputBloodSugarFragment extends Fragment implements Consts, ParseConsts {

    private static final String TAG = InputBloodSugarFragment.class.getSimpleName();

    private Context mContext;

    private EditText mEtBloodSugar, mEtWeight;
    private Button mBtnSave, mBtnDate, mBtnTime;

    private RadioGroup mRgMeasureTime;

    private int mBloodSugar = 0;
    private float mWeight = 0f;
    private int mMeasureTime = 1;

    private String mDateTime;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_input_blood_sugar, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mEtBloodSugar = (EditText) view.findViewById(R.id.et_blood_sugar);
        mEtWeight = (EditText) view.findViewById(R.id.et_weight);

        mBtnSave = (Button) view.findViewById(R.id.btn_save_blood_sugar);
        mBtnSave.setOnClickListener(mButtonClickListener);

        mRgMeasureTime = (RadioGroup) view.findViewById(R.id.rg_measure_time);
        mRgMeasureTime.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                MyLog.d(TAG, "RadioButton checked ID : " + checkedId);
                switch (checkedId) {
                    case R.id.rb_after_two_hours:
                        mMeasureTime = 2;
                        break;
                    case R.id.rb_empty:
                        mMeasureTime = 3;
                        break;
                    case R.id.rb_immediatly:
                        mMeasureTime = 0;
                        break;
                    default:
                        mMeasureTime = 1;
                        break;
                }
            }
        });

        Date date = new Date();

        mBtnDate = (Button) view.findViewById(R.id.btn_date);
        mBtnDate.setText(new SimpleDateFormat(DATE_FORMAT).format(date));
        mBtnTime = (Button) view.findViewById(R.id.btn_time);
        mBtnTime.setText(new SimpleDateFormat(TIME_FORMAT).format(date));

        mBtnDate.setOnClickListener(mButtonClickListener);
        mBtnTime.setOnClickListener(mButtonClickListener);
    }


    private class SavingAsyncTask extends AsyncTask<Float, Void, Boolean> {
        private ProgressDialog mProgressDialog = new ProgressDialog(mContext);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mBtnSave.setEnabled(false);
            mProgressDialog.setMessage(getString(R.string.progress_saving));
            mProgressDialog.show();
        }

        @Override
        protected Boolean doInBackground(Float... params) {
            BloodSugar sugar = ParseObject.create(BloodSugar.class);
            sugar.setBloodSugar(mBloodSugar);
            sugar.setMeasureTime(mMeasureTime);
            sugar.setWeight(mWeight);

            try {
                sugar.setRegisteredDate(new SimpleDateFormat(DATE_TIME_FORMAT).parse(mDateTime));
            } catch (java.text.ParseException e) {
                MyLog.e(TAG, e.getMessage());
            }

            Boolean isSuccessful = true;
            try {
                sugar.save();
            } catch (ParseException e) {
                MyLog.e(TAG, e.getMessage());
                isSuccessful = false;
            }

            return isSuccessful;
        }

        @Override
        protected void onPostExecute(Boolean isSuccessful) {
            super.onPostExecute(isSuccessful);

            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();

            if (isSuccessful) {
                // 초기화
                mEtBloodSugar.setText("");
                mEtWeight.setText("");

                mBloodSugar = 0;
                mWeight = 0f;

                CommonUtils.hideKeyboard(mContext, mEtBloodSugar);

                ((InputActivity) mContext).finish();
            } else {
                Toast.makeText(mContext,
                        R.string.toast_warning_fail_save_blood_pressure,
                        Toast.LENGTH_SHORT).show();
            }

            mBtnSave.setEnabled(true);
        }
    }


    private View.OnClickListener mButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.btn_save_blood_sugar:
                    if (verifyEditText()) {
                        saveBloodSugar();
                    } else {
                        Toast.makeText(mContext, R.string.toast_warning_data_not_valid,
                                Toast.LENGTH_SHORT).show();
                    }
                    break;

                case R.id.btn_date: {
                    Calendar c = getCalendarByString(mBtnDate);

                    if (c == null) return;

                    DatePickerDialog dialog = new DatePickerDialog(mContext,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                    StringBuilder sb = new StringBuilder();
                                    sb.append(year).append("-");
                                    if (monthOfYear < 9)
                                        sb.append("0");
                                    sb.append(monthOfYear + 1).append("-");

                                    if (dayOfMonth < 10)
                                        sb.append("0");
                                    sb.append(dayOfMonth);

                                    mBtnDate.setText(sb);
                                }
                            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH),
                            c.get(Calendar.DAY_OF_MONTH));
                    dialog.show();

                    break;
                }

                case R.id.btn_time: {

                    String[] time = mBtnTime.getText().toString().split(":");

                    if (time == null || time.length != 2) return;

                    TimePickerDialog dialog = new TimePickerDialog(mContext,
                            new TimePickerDialog.OnTimeSetListener() {

                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                    StringBuilder sb = new StringBuilder();
                                    if (hourOfDay < 10)
                                        sb.append("0");
                                    sb.append(hourOfDay).append(":");
                                    if (minute < 10)
                                        sb.append("0");
                                    sb.append(minute);
                                    mBtnTime.setText(sb);
                                }
                            }, Integer.parseInt(time[0]), Integer.parseInt(time[1]), false);
                    dialog.show();

                    break;
                }
            }

        }
    };

    private void saveBloodSugar() {
        SavingAsyncTask task = new SavingAsyncTask();
        task.execute();
    }

    private boolean verifyEditText() {
        boolean isValid = true;

        try {
            mBloodSugar
                    = Integer.parseInt(mEtBloodSugar.getText().toString());
        } catch (NumberFormatException e) {
            MyLog.e(TAG, mEtBloodSugar.getText().toString() +
                    " is not the number type.");
            isValid = false;
        }

        String weight = mEtWeight.getText().toString();
        if (weight != null && !weight.isEmpty()) {
            mWeight = Float.parseFloat(weight);
            if (mWeight > 120f || mWeight < 20f) {
                Toast.makeText(mContext, R.string.toast_warning_not_valid_weight,
                        Toast.LENGTH_SHORT).show();
                isValid = false;
            }
        }

        String date = mBtnDate.getText().toString();

        if (date == null || date.isEmpty()) {
            MyLog.e(TAG, "Doesn't set date string.");
            isValid = false;
        }

        String time = mBtnTime.getText().toString();

        if (time == null || time.isEmpty()) {
            MyLog.e(TAG, "Doesn't set time string.");
            isValid = false;
        }

        mDateTime = new StringBuilder().append(date).append(" ").append(time).toString();

        return isValid;
    }

    private Calendar getCalendarByString(Button button) {

        Date d = null;
        try {
            d = new SimpleDateFormat(DATE_FORMAT).parse(button.getText().toString());
        } catch (java.text.ParseException e) {
            MyLog.e(TAG, e.getMessage());
        }

        if (d == null) return null;

        Calendar c = Calendar.getInstance();
        c.setTime(d);

        return c;
    }

}

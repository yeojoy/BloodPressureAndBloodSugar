package me.yeojoy.foryou.input;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import me.yeojoy.foryou.BuildConfig;
import me.yeojoy.foryou.R;
import me.yeojoy.foryou.config.Consts;
import me.yeojoy.foryou.model.BloodPressure;
import me.yeojoy.foryou.utils.CommonUtils;
import me.yeojoy.library.log.MyLog;

/**
 * A placeholder fragment containing a simple view.
 */
public class InputBloodPressureFragment extends Fragment implements Consts {

    private static final String TAG = InputBloodPressureFragment.class.getSimpleName();

    private Context mContext;

    private EditText mEtBloodPressureMax, mEtBloodPressureMin, mEtBloodPulse;
    private Button mBtnSave, mBtnDate, mBtnTime;

    private int mBloodPressureMax = 0;
    private int mBloodPressureMin = 0;
    private int mBloodPulse = 0;

    private String mDateTime;

    private String mObjectId = null;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MyLog.i(TAG);
        return inflater.inflate(R.layout.fragment_input_blood_pressure, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MyLog.i(TAG);

        if (BuildConfig.DEBUG) {
            // Test data 추가.
            view.findViewById(R.id.tv_fragment_title).setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    showTestDataDialog();
                    return true;
                }
            });
        }

        mEtBloodPressureMax = (EditText) view.findViewById(R.id.et_blood_pressure_max);
        mEtBloodPressureMin = (EditText) view.findViewById(R.id.et_blood_pressure_min);
        mEtBloodPulse = (EditText) view.findViewById(R.id.et_blood_pulse);

        mBtnSave = (Button) view.findViewById(R.id.btn_save_blood_pressure);
        mBtnSave.setOnClickListener(mButtonClickListener);

        Date date = new Date();

        mBtnDate = (Button) view.findViewById(R.id.btn_date);
        mBtnDate.setText(new SimpleDateFormat(DATE_FORMAT).format(date));
        mBtnTime = (Button) view.findViewById(R.id.btn_time);
        mBtnTime.setText(new SimpleDateFormat(TIME_FORMAT).format(date));

        mBtnDate.setOnClickListener(mButtonClickListener);
        mBtnTime.setOnClickListener(mButtonClickListener);

        Bundle b = getArguments();

        if (b != null) {
            bindDataToView(b);

            mObjectId = b.getString(KEY_OBJECT_ID);
        }

    }

    private void bindDataToView(Bundle b) {
        MyLog.i(TAG);

        if (b.getInt(KEY_PRESSURE_MAX) < 0 ||
                b.getInt(KEY_PRESSURE_MIN) < 0) {
            return;
        }

        MyLog.d(TAG, b.toString());

        mEtBloodPressureMax.setText(String.valueOf(b.getInt(KEY_PRESSURE_MAX)));
        mEtBloodPressureMin.setText(String.valueOf(b.getInt(KEY_PRESSURE_MIN)));
        mEtBloodPulse.setText(String.valueOf(b.getInt(KEY_PRESSURE_PULSE)));

        Calendar c = Calendar.getInstance();
        c.setTime(new Date(b.getLong(KEY_DATE_TIME)));


        StringBuilder sb = new StringBuilder();
        sb.append(c.get(Calendar.YEAR)).append("-");
        if (c.get(Calendar.MONTH) < 9)
            sb.append("0");
        sb.append(c.get(Calendar.MONTH) + 1).append("-");

        if (c.get(Calendar.DAY_OF_MONTH) < 10)
            sb.append("0");
        sb.append(c.get(Calendar.DAY_OF_MONTH));

        mBtnDate.setText(sb);

        sb = new StringBuilder();
        if (c.get(Calendar.HOUR_OF_DAY) < 10)
            sb.append("0");
        sb.append(c.get(Calendar.HOUR_OF_DAY)).append(":");
        if (c.get(Calendar.MINUTE) < 10)
            sb.append("0");
        sb.append(c.get(Calendar.MINUTE));
        mBtnTime.setText(sb);

    }

    @Override
    public void onStart() {
        super.onStart();
        MyLog.i(TAG);
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
            MyLog.i(TAG);

            if (mObjectId == null || mObjectId.isEmpty()) {
                BloodPressure pressure = ParseObject.create(BloodPressure.class);
                pressure.setBloodPressureMax(mBloodPressureMax);
                pressure.setBloodPressureMin(mBloodPressureMin);
                pressure.setBloodPulse(mBloodPulse);

                try {
                    pressure.setRegisteredDate(new SimpleDateFormat(DATE_TIME_FORMAT).parse(mDateTime));
                    pressure.save();
                } catch (java.text.ParseException e) {
                    MyLog.e(TAG, e);
                    return false;
                } catch (ParseException e) {
                    MyLog.e(TAG, e);
                    return false;
                }

            } else {
                MyLog.d(TAG, "Object ID >>>> " + mObjectId);
                ParseQuery<BloodPressure> query = ParseQuery.getQuery(BloodPressure.class);
                try {
                    BloodPressure pressure = query.get(mObjectId);

                    if (pressure != null) {
                        pressure.setBloodPressureMax(mBloodPressureMax);
                        pressure.setBloodPressureMin(mBloodPressureMin);
                        pressure.setBloodPulse(mBloodPulse);

                        pressure.setRegisteredDate(new SimpleDateFormat(DATE_TIME_FORMAT).parse(mDateTime));

                        pressure.save();
                    }
                } catch (ParseException e) {
                    MyLog.e(TAG, e);
                    return false;
                } catch (java.text.ParseException e) {
                    MyLog.e(TAG, e);
                    return false;
                }

                mObjectId = null;
            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean isSuccessful) {
            super.onPostExecute(isSuccessful);

            MyLog.i(TAG);
            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();

            if (isSuccessful) {
                // 초기화
                mEtBloodPressureMax.setText("");
                mEtBloodPressureMin.setText("");
                mEtBloodPulse.setText("");

                mBloodPressureMax = 0;
                mBloodPressureMin = 0;
                mBloodPulse = 0;

                CommonUtils.hideKeyboard(mContext, mEtBloodPulse);

                ((Activity) mContext).finish();
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
                case R.id.btn_save_blood_pressure:
                    if (verifyEditText()) {
                        saveBloodPressure();
                    } else {
                        Toast.makeText(mContext, R.string.toast_warning_data_not_valid,
                                Toast.LENGTH_SHORT).show();
                    }
                    break;

                case R.id.btn_date: {
                    Calendar c = CommonUtils.getCalendarByString(mBtnDate);

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

    private void saveBloodPressure() {
        SavingAsyncTask task = new SavingAsyncTask();
        task.execute();
    }

    private boolean verifyEditText() {
        boolean isValid = true;

        try {
            mBloodPressureMax
                    = Integer.parseInt(mEtBloodPressureMax.getText().toString());
        } catch (NumberFormatException e) {
            MyLog.e(TAG, mEtBloodPressureMax.getText().toString() +
                    " is not number type.");
            isValid = false;
        }

        try {
            mBloodPressureMin
                    = Integer.parseInt(mEtBloodPressureMin.getText().toString());
        } catch (NumberFormatException e) {
            MyLog.e(TAG, mEtBloodPressureMin.getText().toString() +
                    " is not number type.");
            isValid = false;
        }

        try {
            mBloodPulse
                    = Integer.parseInt(mEtBloodPulse.getText().toString());
        } catch (NumberFormatException e) {
            MyLog.e(TAG, mEtBloodPulse.getText().toString() +
                    " is not number type.");
            isValid = false;
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


    private void showTestDataDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage(R.string.dialog_test_data);

        builder.setPositiveButton("생성", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                SavingTestDataAsyncTask task = new SavingTestDataAsyncTask();
                task.execute();
                dialog.dismiss();
            }
        });

        builder.setNeutralButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("삭제", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DeletingTestDataAsyncTask task = new DeletingTestDataAsyncTask();
                task.execute();
                dialog.dismiss();
            }
        });

        builder.create().show();

    }


    private class SavingTestDataAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            Calendar c = Calendar.getInstance();

            for (int i = 0; i < 100; i++) {
                BloodPressure pressure = ParseObject.create(BloodPressure.class);

                Random random = new Random();

                pressure.setBloodPressureMax(random.nextInt(50) + 100);
                pressure.setBloodPressureMin(random.nextInt(30) + 70);
                pressure.setBloodPulse(random.nextInt(40) + 50);

                c.add(Calendar.HOUR_OF_DAY, 12);

                pressure.setRegisteredDate(c.getTime());

                MyLog.d(TAG, "BloodPressure >>>>>>> " + pressure.toString());

                pressure.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null)
                            MyLog.e(TAG, e);
                        else
                            MyLog.d(TAG, "Object is saved successfully.");
                    }
                });
            }

            return null;
        }
    }


    private class DeletingTestDataAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            ParseQuery<BloodPressure> query = ParseQuery.getQuery(BloodPressure.class);
            query.findInBackground(new FindCallback<BloodPressure>() {
                @Override
                public void done(List<BloodPressure> list, ParseException e) {

                    if (e != null) {
                        MyLog.e(TAG, e);
                        return;
                    }

                    for (BloodPressure b : list) {
                        b.deleteInBackground(new DeleteCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e != null)
                                    MyLog.e(TAG, e);
                                else
                                    MyLog.d(TAG, "The Object delete successfully.");

                            }
                        });
                    }

                }
            });

            return null;
        }
    }
}

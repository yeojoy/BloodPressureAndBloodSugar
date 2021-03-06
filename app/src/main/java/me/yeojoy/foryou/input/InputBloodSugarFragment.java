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
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
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
import me.yeojoy.foryou.config.ParseConsts;
import me.yeojoy.foryou.model.BloodPressure;
import me.yeojoy.foryou.model.BloodSugar;
import me.yeojoy.foryou.utils.CommonUtils;
import me.yeojoy.library.log.MyLog;

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

    private String mObjectId = null;

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
                    case R.id.rb_immediatly:
                        mMeasureTime = 0;
                        break;
                    case R.id.rb_after_two_hours:
                        mMeasureTime = 2;
                        break;
                    case R.id.rb_empty:
                        mMeasureTime = 3;
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

        Bundle b = getArguments();

        if (b != null) {
            bindDataToView(b);

            mObjectId = b.getString(KEY_OBJECT_ID);
        }
    }

    private void bindDataToView(Bundle b) {
        MyLog.i(TAG);
        if (b.getInt(KEY_SUGAR) < 1) {
            return;
        }

        MyLog.d(TAG, b.toString());

        mEtBloodSugar.setText(String.valueOf(b.getInt(KEY_SUGAR)));
        if (b.getFloat(KEY_SUGAR_WEIGHT) < 20f) {
            mEtWeight.setText("");
        } else {
            mEtWeight.setText(String.format("%.1f", b.getFloat(KEY_SUGAR_WEIGHT)));
        }

        int itemId;
        switch (b.getInt(KEY_SUGAR_MEASURED_TIME)) {
            case 0:
                itemId = R.id.rb_immediatly;
                break;
            case 2:
                itemId = R.id.rb_after_two_hours;
                break;
            case 3:
                itemId = R.id.rb_empty;
                break;
            default:
                itemId = R.id.rb_after_one_hour;
                break;
        }
        mRgMeasureTime.check(itemId);

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

                try {
                    BloodSugar sugar = ParseObject.create(BloodSugar.class);
                    sugar.setBloodSugar(mBloodSugar);
                    sugar.setMeasureTime(mMeasureTime);
                    sugar.setWeight(mWeight);
                    sugar.setRegisteredDate(new SimpleDateFormat(DATE_TIME_FORMAT).parse(mDateTime));
                    sugar.save();
                } catch (java.text.ParseException e) {
                    MyLog.e(TAG, e);
                    return false;
                } catch (ParseException e) {
                    MyLog.e(TAG, e);
                    return false;
                }

            } else {
                MyLog.d(TAG, "Object ID >>>> " + mObjectId);
                ParseQuery<BloodSugar> query = ParseQuery.getQuery(BloodSugar.class);
                try {
                    BloodSugar sugar = query.get(mObjectId);

                    if (sugar != null) {
                        sugar.setBloodSugar(mBloodSugar);
                        sugar.setWeight(mWeight);
                        sugar.setMeasureTime(mMeasureTime);

                        sugar.setRegisteredDate(new SimpleDateFormat(DATE_TIME_FORMAT).parse(mDateTime));

                        sugar.save();
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
        protected void onPostExecute(Boolean isSuccessful){
            super.onPostExecute(isSuccessful);
            MyLog.i(TAG);

            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();

            if (isSuccessful) {
                // 초기화
                mEtBloodSugar.setText("");
                mEtWeight.setText("");

                mBloodSugar = 0;
                mWeight = 0f;

                CommonUtils.hideKeyboard(mContext, mEtBloodSugar);

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
                case R.id.btn_save_blood_sugar:
                    if (verifyEditText()) {
                        saveBloodSugar();
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

                    if (time.length != 2) return;

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
        if (!weight.isEmpty()) {
            mWeight = Float.parseFloat(weight);
            if (mWeight > 120f || mWeight < 20f) {
                Toast.makeText(mContext, R.string.toast_warning_not_valid_weight,
                        Toast.LENGTH_SHORT).show();
                isValid = false;
            }
        }

        String date = mBtnDate.getText().toString();

        if (date.isEmpty()) {
            MyLog.e(TAG, "Doesn't set date string.");
            isValid = false;
        }

        String time = mBtnTime.getText().toString();

        if (time.isEmpty()) {
            MyLog.e(TAG, "Doesn't set time string.");
            isValid = false;
        }

        mDateTime = date + " " + time;

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
                BloodSugar sugar = ParseObject.create(BloodSugar.class);

                Random random = new Random();

                sugar.setBloodSugar(random.nextInt(50) + 100);
                float weight = random.nextFloat();
                sugar.setWeight(weight > 0.3f ? (weight * 100f) + 30f : (weight * 100f) + 50f);
                sugar.setMeasureTime(random.nextInt(3));

                c.add(Calendar.HOUR_OF_DAY, 12);

                sugar.setRegisteredDate(c.getTime());

                MyLog.d(TAG, "BloodSugar >>>>>>> " + sugar.toString());

                sugar.saveInBackground(new SaveCallback() {
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

            ParseQuery<BloodSugar> query = ParseQuery.getQuery(BloodSugar.class);
            query.findInBackground(new FindCallback<BloodSugar>() {
                @Override
                public void done(List<BloodSugar> list, ParseException e) {

                    if (e != null) {
                        MyLog.e(TAG, e);
                        return;
                    }

                    for (BloodSugar b : list) {
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

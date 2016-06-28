package me.yeojoy.foryou.utils;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.content.ContextCompat;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import me.yeojoy.foryou.R;
import me.yeojoy.foryou.config.Consts;
import me.yeojoy.library.log.MyLog;

/**
 * Created by yeojoy on 15. 6. 19..
 */
public class CommonUtils implements Consts {

    private static final String TAG = CommonUtils.class.getSimpleName();

    public static void hideKeyboard(Context context, EditText et) {
        InputMethodManager imm =
                (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        //txtName is a reference of an EditText Field
        imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
    }

    public static void showKeyboard(Context context, EditText et) {
        InputMethodManager imm =
                (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT);
        imm.showSoftInputFromInputMethod(et.getWindowToken(), InputMethodManager.SHOW_IMPLICIT);
    }

    public static int[] getTextColorOfBloodPressure(Context context, float max, float min) {

        int[] colors = new int[2];
        if (max > 120f || max <= 90f) {
            colors[0] = ContextCompat.getColor(context, R.color.text_color_danger);
        } else {
            colors[0] = ContextCompat.getColor(context, R.color.primary_text);
        }

        if (min > 80f || min <= 60f) {
            colors[1] = ContextCompat.getColor(context, R.color.text_color_danger);
        } else {
            colors[1] = ContextCompat.getColor(context, R.color.primary_text);
        }

        return colors;
    }

    public static int getTextColorOfBloodSugar(Context context, int bloodSugar, int time) {
        int color;

        switch (time) {
            case 0:
                // 식사 직후
                if (bloodSugar < 200)
                    color = ContextCompat.getColor(context, R.color.primary_text);
                else
                    color = ContextCompat.getColor(context, R.color.text_color_danger);
                break;
            case 2:
                // 식 후 2시간 경과
                if (bloodSugar < 140)
                    color = ContextCompat.getColor(context, R.color.primary_text);
                else
                    color = ContextCompat.getColor(context, R.color.text_color_danger);
                break;

            case 3:
                // 아침 공복
                if (bloodSugar < 100)
                    color = ContextCompat.getColor(context, R.color.primary_text);
                else
                    color = ContextCompat.getColor(context, R.color.text_color_danger);
                break;

            default:
                if (bloodSugar < 160)
                    color = ContextCompat.getColor(context, R.color.primary_text);
                else
                    color = ContextCompat.getColor(context, R.color.text_color_danger);
                break;

        }

        return color;
    }

    public static Calendar getCalendarByString(Button button) {
        MyLog.i(TAG, "getCalendarByString()");

        Date d = null;
        try {
            d = new SimpleDateFormat(DATE_FORMAT).parse(button.getText().toString());
        } catch (java.text.ParseException e) {
            MyLog.e(TAG, e);
        }

        if (d == null) return null;

        Calendar c = Calendar.getInstance();
        c.setTime(d);

        return c;
    }
}

package me.yeojoy.foryou.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.Date;

import me.yeojoy.foryou.config.Consts;

/**
 * Created by yeojoy on 15. 7. 7..
 */
@ParseClassName("BloodPressure")
public class BloodPressure extends ParseObject implements Consts {

    public float getBloodPressureMax() {
        return getNumber(PARSE_BP_COLUMN_MAX).floatValue();
    }

    public void setBloodPressureMax(float max) {
        put(PARSE_BP_COLUMN_MAX, max);
    }

    public float getBloodPressureMin() {
        return getNumber(PARSE_BP_COLUMN_MIN).floatValue();
    }

    public void setBloodPressureMin(float min) {
        put(PARSE_BP_COLUMN_MIN, min);
    }

    public int getBloodPulse() {
        return getNumber(PARSE_BP_COLUMN_PULSE).intValue();
    }

    public void setBloodPulse(int pulse) {
        put(PARSE_BP_COLUMN_PULSE, pulse);
    }

    public Date getRegisteredDate() {
        return getDate(PARSE_COMMON_COLUMN_REGISTERED_AT);
    }

    public void setRegisteredDate(Date date) {
        put(PARSE_COMMON_COLUMN_REGISTERED_AT, date);
    }

}

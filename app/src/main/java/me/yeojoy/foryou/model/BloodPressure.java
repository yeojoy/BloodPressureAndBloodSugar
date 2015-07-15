package me.yeojoy.foryou.model;

import com.parse.ParseClassName;

import me.yeojoy.foryou.config.ParseConsts;

/**
 * Created by yeojoy on 15. 7. 7..
 */
@ParseClassName(ParseConsts.PARSE_BLOOD_PRESSURE_TABLE)
public class BloodPressure extends BaseParseObject {

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

    @Override
    public String toString() {
        return new StringBuilder()
                .append("[ Max : ").append(getBloodPressureMax()).append(", ")
                .append("Min : ").append(getBloodPressureMin()).append(", ")
                .append("Pulse : ").append(getBloodPulse()).append(", ")
                .append("Registered Date : ").append(getRegisteredDate()).append(" ]")
                .toString();
    }
}

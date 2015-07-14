package me.yeojoy.foryou.model;

import com.parse.ParseClassName;

import me.yeojoy.foryou.config.ParseConsts;

/**
 * Created by yeojoy on 15. 7. 7..
 */
@ParseClassName(ParseConsts.PARSE_BLOOD_SUGAR_TABLE)
public class BloodSugar extends BaseParseObject {

    public int getBloodSugar() {
        return getNumber(PARSE_BS_COLUMN_SUGAR).intValue();
    }

    public void setBloodSugar(int max) {
        put(PARSE_BS_COLUMN_SUGAR, max);
    }

    public int getMeasureTime() {
        return getNumber(PARSE_BS_COLUMN_MEASURE_TIME).intValue();
    }

    /**
     * 측정시각을 나타낸다.
     * time은 {0, 1, 2} 중 하나로,
     * 0일때는 즉시 측정, 1일때는 1시간 후 측정,
     * 2일때는 2시간 후 측정을 의미한다.
     * @param time
     */
    public void setMeasureTime(int time) {
        put(PARSE_BS_COLUMN_MEASURE_TIME, time);
    }

    public float getWeight() {
        if (getNumber(PARSE_BS_COLUMN_WEIGHT) == null)
            return 0f;
        return getNumber(PARSE_BS_COLUMN_WEIGHT).floatValue();
    }

    public void setWeight(float min) {
        put(PARSE_BS_COLUMN_WEIGHT, min);
    }

}

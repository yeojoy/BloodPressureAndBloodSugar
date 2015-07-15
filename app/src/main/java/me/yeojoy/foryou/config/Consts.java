package me.yeojoy.foryou.config;

/**
 * Created by yeojoy on 15. 7. 6..
 */
public interface Consts {
    /** Activity 이동 */
    String KEY_INPUT_TYPE = "input_type";

    int INPUT_TYPE_BLOOD_PRESSURE   = 0x000001;
    int INPUT_TYPE_BLOOD_SUGAR      = 0x000002;

    /** date format */
    String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm";
    String DATE_FORMAT = "yyyy-MM-dd";
    String TIME_FORMAT = "HH:mm";

    String IMMEDIATELY = "직 후";
    String AFTER_ONE_HOUR  = "1시간 후";
    String AFTER_TWO_HOURS = "2시간 후";
    String EMPTY = "공복";

    String BLOOD_PRESSURE_MAX_X_LABEL = "Max";
    String BLOOD_PRESSURE_MIN_X_LABEL = "Min";
    String BLOOD_PRESSURE_PULSE_X_LABEL = "Pulse";

}

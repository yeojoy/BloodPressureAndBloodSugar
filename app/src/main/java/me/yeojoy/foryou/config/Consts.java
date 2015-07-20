package me.yeojoy.foryou.config;

/**
 * Created by yeojoy on 15. 7. 6..
 */
public interface Consts {
    /** Activity 이동 */
    String KEY_INPUT_TYPE = "input_type";
    String KEY_GRAPH_TYPE = "graph_type";
    String KEY_GRAPH_ITEM_POSITON = "position";

    String KEY_PRESSURE_MAX     = "pressure_max";
    String KEY_PRESSURE_MIN     = "pressure_min";
    String KEY_PRESSURE_PULSE   = "pressure_pulse";

    String KEY_SUGAR                = "sugar";
    String KEY_SUGAR_MEASURED_TIME  = "sugar_measured_time";
    String KEY_SUGAR_WEIGHT         = "sugar_weight";

    String KEY_DATE_TIME            = "date_time";

    int INPUT_TYPE_BLOOD_PRESSURE   = 0x000001;
    int INPUT_TYPE_BLOOD_SUGAR      = 0x000002;

    int GRAPH_TYPE_BLOOD_PRESSURE   = 0x000003;
    int GRAPH_TYPE_BLOOD_SUGAR      = 0x000004;

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

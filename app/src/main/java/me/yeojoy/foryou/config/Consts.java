package me.yeojoy.foryou.config;

/**
 * Created by yeojoy on 15. 7. 6..
 */
public interface Consts {
    String PARSE_APPLICATION_ID = "GnUFqSTV7R3HKBy8CueZ6yLF2dpRPrEvmFNySFhg";
    String PARSE_CLIENT_KEY = "WXwuObRQOJVDtWvejGu1a6H0CL1KbjfbzCD0xCW3";

    /** 혈압 parse data and key */
    String PARSE_BLOOD_PRESSURE_TABLE = "BloodPressure";

    String PARSE_BP_COLUMN_MAX = "bloodPressureMax";
    String PARSE_BP_COLUMN_MIN = "bloodPressureMin";
    String PARSE_BP_COLUMN_PULSE = "bloodPulse";

    /** 혈당 parse data and key */
    String PARSE_BLOOD_SUGAR_TABLE = "BloodSugar";

    String PARSE_BS_COLUMN_SUGAR = "bloodSugar";

    /** 공통 key */
    String PARSE_COMMON_COLUMN_CREATED_AT = "registeredAt";

    String PARSE_COMMON_COLUMN_REGISTERED_AT = "registeredAt";

    /** Activity 이동 */
    String KEY_INPUT_TYPE = "input_type";

    int INPUT_TYPE_BLOOD_PRESSURE   = 0x000001;
    int INPUT_TYPE_BLOOD_SUGAR      = 0x000002;

    /** date format */
    String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm";
    String DATE_FORMAT = "yyyy-MM-dd";
    String TIME_FORMAT = "HH:mm";

}

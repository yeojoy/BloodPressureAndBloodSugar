package me.yeojoy.foryou.config;

/**
 * Created by yeojoy on 15. 7. 6..
 */
public interface ParseConsts {
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
    String PARSE_BS_COLUMN_MEASURE_TIME = "measureTime";
    String PARSE_BS_COLUMN_WEIGHT = "weight";

    /** 공통 key */
    String PARSE_COMMON_COLUMN_REGISTERED_AT = "registeredAt";

}

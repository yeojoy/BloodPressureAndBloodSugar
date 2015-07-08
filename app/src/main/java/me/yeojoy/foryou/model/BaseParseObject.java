package me.yeojoy.foryou.model;

import com.parse.ParseObject;

import java.util.Date;

import me.yeojoy.foryou.config.ParseConsts;

/**
 * Created by yeojoy on 15. 7. 8..
 */
public class BaseParseObject extends ParseObject implements ParseConsts {

    public Date getRegisteredDate() {
        return getDate(PARSE_COMMON_COLUMN_REGISTERED_AT);
    }

    public void setRegisteredDate(Date date) {
        put(PARSE_COMMON_COLUMN_REGISTERED_AT, date);
    }

}

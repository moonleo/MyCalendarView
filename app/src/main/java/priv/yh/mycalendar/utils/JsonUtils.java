package priv.yh.mycalendar.utils;

import android.util.Log;

import com.alibaba.fastjson.JSON;

import priv.yh.mycalendar.model.HolidayMode;

/**
 * Json Utils
 *
 * @author yanhan
 * @date 2019-05-12
 */
public class JsonUtils {

    private static final String TAG = "JsonUtils";

    /**
     * parse a string and cast to a HolidayMode object
     * @param jsonString a string with a json format
     * @return a HolidayMode object
     */
    public static HolidayMode parseHolidayMode(String jsonString) {
        Log.d(TAG, "jsonString = " + jsonString);
        HolidayMode holidayMode = null;
        try {
            holidayMode = JSON.parseObject(jsonString, HolidayMode.class);
        } catch (Exception e) {
            Log.e(TAG, "error occured: "+ e.toString());
        }
        if(holidayMode != null) {
            return holidayMode;
        } else {
            return new HolidayMode(0, 0);
        }
    }
}

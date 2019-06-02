package priv.yh.mycalendar.utils;

/**
 * Constants in Project
 *
 * @author moonleo
 * @date 2018/08/18
 */
public class Constants {
    /**
     * max date cells in a month
     */
    public static final int MAX_DATE_CELL_NUM = 6 * 7;

    /**
     * date format string
     */
    public static final String DATE_FORMAT_STRING = "MMM yyyy";

    /**
     * enough man hour every day
     */
    public static final int ENOUGH_MAN_HOUR = 8;

    public static final double DOUBLE_ACCURACY = 0.001;

    public static final int DOUBLE_SCALE = 2;

    public static final String CHECK_HOLIDAY_URL = "http://api.goseek.cn/Tools/holiday";

    public static final int ERROR_CODE = -1;

    class CalendarTable {
        public static final String TABLE_NAME = "day_event";
        public static final String KEY_YEAR = "year";
        public static final String KEY_MONTH = "month";
        public static final String KEY_DAY = "day";
        public static final String KEY_MAN_HOUR = "man_hour";
        public static final String KEY_DAY_TYPE = "day_type";
    }
}

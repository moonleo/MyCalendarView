package priv.yh.mycalendar.utils;

public class Constants {
    //max date cells in a month
    public static final int MAX_DATE_CELL_NUM = 6 * 7;
    public static final String DATE_FORMAT_STRING = "MMM yyyy";
    public static final int ENOUGH_MAN_HOUR = 8;

    class CalendarTable {
        public static final String TABLE_NAME = "day_event";
        public static final String KEY_YEAR = "year";
        public static final String KEY_MONTH = "month";
        public static final String KEY_DAY = "day";
        public static final String KEY_MAN_HOUR = "man_hour";
    }
}

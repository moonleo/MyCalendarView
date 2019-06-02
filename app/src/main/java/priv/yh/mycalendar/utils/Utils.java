package priv.yh.mycalendar.utils;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Utils
 *
 * @author moonleo
 * @date 2018/12/21
 */
public class Utils {

    /**
     * format a double, and keep decimal places with the given scale
     * @param d
     * @param scale
     * @return
     */
    public static double formatDouble(double d, int scale) {
        BigDecimal bd = new BigDecimal(d);
        return bd.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * whether manHour is enough for a day
     * @param manHour
     * @return
     */
    public static boolean isEnoughManHour(double manHour) {
        if(manHour - Constants.ENOUGH_MAN_HOUR > Constants.DOUBLE_ACCURACY) {
            return true;
        }
        return false;
    }

    public static String formatDayOrMonthToTwoCharString(int dayOrMonth) {
        return dayOrMonth < 10 ? "0" + dayOrMonth : "" + dayOrMonth;
    }
}

package priv.yh.mycalendar.utils;

import java.math.BigDecimal;

/**
 * Utils
 *
 * @author moonleo
 * @date 2018/12/21
 */
public class Utils {

    public static double formatDouble(double d) {
        BigDecimal bd = new BigDecimal(d);
        return bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static boolean isEnoughManHour(double manHour) {
        if(manHour - Constants.ENOUGH_MAN_HOUR > Constants.DOUBLE_ACCURACY) {
            return true;
        }
        return false;
    }
}

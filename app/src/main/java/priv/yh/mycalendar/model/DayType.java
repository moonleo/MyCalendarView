package priv.yh.mycalendar.model;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author yanhan
 * @date 2019-06-02
 */
public enum DayType {
    DAY_TYPE_WORKDAY(0),
    DAY_TYPE_HOLIDAY(1),
    DAY_TYPE_DUTY_FOR_HOLIDAY(2),
    DAY_TYPE_WEEKEND(3),
    DAY_TYPE_NULL(-1);

    private static final Map<Integer, DayType> map = new HashMap<Integer, DayType>();
    static {
        Iterator iterator = EnumSet.allOf(DayType.class).iterator();
        while (iterator.hasNext()) {
            DayType dayType = (DayType) iterator.next();
            map.put(dayType.getValue(), dayType);
        }
    }

    private int value;

    DayType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static DayType get(int typeValue) {
        if(map.isEmpty()) {
            return DayType.DAY_TYPE_NULL;
        }
        return map.get(typeValue);
    }
}

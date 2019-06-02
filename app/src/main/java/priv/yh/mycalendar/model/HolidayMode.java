package priv.yh.mycalendar.model;

import priv.yh.mycalendar.utils.Constants;

/**
 *
 *
 * @author yanhan
 * @date 2019-05-12
 */
public class HolidayMode {

    private int code;
    private int data;

    public HolidayMode() {
        this.code = Constants.ERROR_CODE;
        this.data = DayType.DAY_TYPE_NULL.getValue();
    }

    public HolidayMode(int code, int data) {
        this.code = code;
        this.data = DayType.DAY_TYPE_NULL.getValue();
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }
}

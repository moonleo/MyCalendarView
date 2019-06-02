package priv.yh.mycalendar.model;

/**
 * day model
 *
 * @author moonleo
 * @date 2018/08/18
 */
public class DayEvent {
    private int year;
    private int month;
    private int day;
    private double manHour;
    /**
     * same as DayType value
     */
    private int dayType = DayType.DAY_TYPE_NULL.getValue();

    private String tag;

    public DayEvent() {

    }

    public DayEvent(int year, int month, int day) {
        this(year, month, day, 0, DayType.DAY_TYPE_NULL.getValue());
    }

    public DayEvent(int year, int month, int day, double manHour) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.manHour = manHour;
        this.dayType = DayType.DAY_TYPE_NULL.getValue();
    }

    public DayEvent(int year, int month, int day, double manHour, int dayType) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.manHour = manHour;
        this.dayType = dayType;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public double getManHour() {
        return manHour;
    }

    public void setManHour(double manHour) {
        this.manHour = manHour;
    }

    public int getDayType() {
        return dayType;
    }

    public void setDayType(int dayType) {
        this.dayType = dayType;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return year + "/" + month + "/" + day;
    }
}

package priv.yh.mycalendar.model;

public class DayEvent {
    private int year;
    private int month;
    private int day;
    private double manHour;

    public DayEvent() {

    }

    public DayEvent(int year, int month, int day, double manHour) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.manHour = manHour;
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
}

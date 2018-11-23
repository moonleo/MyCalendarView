package priv.yh.mycalendar.utils;

import java.util.List;

import priv.yh.mycalendar.model.DayEvent;

public interface DBManagerInterface {
    void insertManHour(DayEvent dayEvent);
    void updateManHour(DayEvent dayEvent);
    List<DayEvent> queryDayEvents(int year, int month, int day);
}

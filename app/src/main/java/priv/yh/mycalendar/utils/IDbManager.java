package priv.yh.mycalendar.utils;

import java.util.List;

import priv.yh.mycalendar.model.DayEvent;

/**
 * database interface
 *
 * @author moonleo
 * @date 2018/12/21
 */
public interface IDbManager {
    /**
     * insert day event to database
     * @param dayEvent day model
     */
    void insertManHour(DayEvent dayEvent);

    /**
     * update day event in database
     * @param dayEvent day model
     */
    void updateManHour(DayEvent dayEvent);

    /**
     * query day event in database
     * @param year
     * @param month
     * @param day
     * @return list of day event
     */
    List<DayEvent> queryDayEvents(int year, int month, int day);
}

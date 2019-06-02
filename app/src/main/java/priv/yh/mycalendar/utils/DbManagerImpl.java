package priv.yh.mycalendar.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import priv.yh.mycalendar.utils.Constants.CalendarTable;


import priv.yh.mycalendar.model.DayEvent;

/**
 * DB Manager
 *
 * @author moonleo
 * @date 2018/12/21
 */
public class DbManagerImpl implements IDbManager {

    public static DbManagerImpl singleton = null;

    private SQLiteDatabase readableDatabase = null;
    private SQLiteDatabase writableDatabase = null;

    public static synchronized DbManagerImpl getInstance(Context context) {
        if(singleton == null) {
            MyDbOpenHelper dbHelper = MyDbOpenHelper.getInstance(context);
            singleton = new DbManagerImpl();
            singleton.readableDatabase = dbHelper.getReadableDatabase();
            singleton.writableDatabase = dbHelper.getWritableDatabase();
        }
        return singleton;
    }

    private DbManagerImpl() {}

    @Override
    public void insertManHour(DayEvent dayEvent) {
        String insertSql = "REPLACE INTO " + CalendarTable.TABLE_NAME +
                " VALUES(?, ?, ?, ?)";
        writableDatabase.beginTransaction();
        try {
            writableDatabase.execSQL(insertSql, new Object[]{dayEvent.getYear(), dayEvent.getMonth(),
                    dayEvent.getDay(), dayEvent.getManHour()});
            writableDatabase.setTransactionSuccessful();
        } finally {
            writableDatabase.endTransaction();
        }
    }

    @Override
    public void updateManHour(DayEvent dayEvent) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CalendarTable.KEY_MAN_HOUR, dayEvent.getManHour());
        writableDatabase.update(CalendarTable.TABLE_NAME, contentValues,
                CalendarTable.KEY_YEAR + "=? AND" +
                            CalendarTable.KEY_MONTH + "=? AND" +
                            CalendarTable.KEY_DAY + "=?",
                                new String[]{String.valueOf(dayEvent.getYear()),
                                            String.valueOf(dayEvent.getMonth()),
                                            String.valueOf(dayEvent.getDay())});
    }

    @Override
    public List<DayEvent> queryDayEvents(int year, int month, int day) {
        Cursor cursor = readableDatabase.query(CalendarTable.TABLE_NAME,
                new String[]{CalendarTable.KEY_YEAR, CalendarTable.KEY_MONTH,
                        CalendarTable.KEY_DAY, CalendarTable.KEY_MAN_HOUR},
                CalendarTable.KEY_YEAR + "=?" +" AND " +
                        CalendarTable.KEY_MONTH + "=?" + " AND " +
                        CalendarTable.KEY_DAY + "=?",
                new String[]{String.valueOf(year), String.valueOf(month), String.valueOf(day)},
                null, null, null);
        List<DayEvent> result = new ArrayList<>();
        if(cursor.moveToFirst()) {
            do {
                DayEvent dayEvent = new DayEvent();
                dayEvent.setYear(year);
                dayEvent.setMonth(month);
                dayEvent.setDay(day);
                dayEvent.setManHour(cursor.getDouble(cursor.getColumnIndex(CalendarTable.KEY_MAN_HOUR)));
                result.add(dayEvent);
            } while(cursor.moveToNext());
        }
        return result;
    }

    @Override
    public void updateDayType(DayEvent dayEvent) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CalendarTable.KEY_DAY_TYPE, dayEvent.getDayType());
        writableDatabase.update(CalendarTable.TABLE_NAME, contentValues,
                CalendarTable.KEY_YEAR + "=? AND " +
                        CalendarTable.KEY_MONTH + "=? AND " +
                        CalendarTable.KEY_DAY + "=?",
                        new String[]{
                                String.valueOf(dayEvent.getYear()),
                                String.valueOf(dayEvent.getMonth()),
                                String.valueOf(dayEvent.getDay())});
    }

}

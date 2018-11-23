package priv.yh.mycalendar.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import priv.yh.mycalendar.utils.Constants.CalendarTable;


import priv.yh.mycalendar.model.DayEvent;

public class MyDBManager implements DBManagerInterface{

    public static MyDBManager singleton = null;

    private SQLiteDatabase readableDatabase = null;
    private SQLiteDatabase writableDatabase = null;

    public static synchronized MyDBManager getInstance(Context context) {
        if(singleton == null) {
            MyDBOpenHelper dbHelper = MyDBOpenHelper.getInstance(context);
            singleton = new MyDBManager();
            singleton.readableDatabase = dbHelper.getReadableDatabase();
            singleton.writableDatabase = dbHelper.getWritableDatabase();
        }
        return singleton;
    }

    private MyDBManager() {}

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

}

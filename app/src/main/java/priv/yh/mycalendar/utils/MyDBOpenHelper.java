package priv.yh.mycalendar.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import priv.yh.mycalendar.utils.Constants.CalendarTable;

public class MyDBOpenHelper extends SQLiteOpenHelper {

    private static MyDBOpenHelper singleton = null;

    private static final String DB_NAME = "mycalendar.db";

    private static final String CREATE_TABLE = "create table if not exists " + CalendarTable.TABLE_NAME +
            "(" + CalendarTable.KEY_YEAR + " integer(4)," +
            CalendarTable.KEY_MONTH + " integer(4)," +
            CalendarTable.KEY_DAY + " integer(4)," +
            CalendarTable.KEY_MAN_HOUR + " double(5)," +
            "primary key ("+CalendarTable.KEY_YEAR+","+CalendarTable.KEY_MONTH+","+CalendarTable.KEY_DAY+"))";
    private static final String DROP_TABLE = "drop table if exists " + CalendarTable.TABLE_NAME;

    private MyDBOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    private void deleteTable(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DROP_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        deleteTable(sqLiteDatabase);
        onCreate(sqLiteDatabase);
    }

    public static synchronized MyDBOpenHelper getInstance(Context context) {
        if(singleton == null) {
            singleton = new MyDBOpenHelper(context, DB_NAME, null, 1);
        }
        return singleton;
    }
}

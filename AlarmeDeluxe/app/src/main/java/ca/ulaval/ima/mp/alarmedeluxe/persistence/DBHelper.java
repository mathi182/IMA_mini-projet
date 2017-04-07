package ca.ulaval.ima.mp.alarmedeluxe.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;

import ca.ulaval.ima.mp.alarmedeluxe.domain.Alarm;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "AlarmDeluxe.db";
    public static final String ALARMS_TABLE_NAME = "alarms";
    public static final String ALARMS_COLUMN_ID = "id";
    public static final String ALARMS_COLUMN_TITLE = "title";
    public static final String ALARMS_COLUMN_DESCRIPTION = "description";
    public static final String ALARMS_COLUMN_ISACTIVE = "isActive";
    public static final String ALARMS_COLUMN_ISREPEATING = "isRepeating";
    public static final String ALARMS_COLUMN_CALENDAR = "calendar";
    public static final String ALARMS_COLUMN_DAYS = "days";
    public static final String ALARMS_COLUMN_TYPE = "type";
    private HashMap hashMap;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table " + ALARMS_TABLE_NAME + " (" +
                        ALARMS_COLUMN_ID + " integer primary key autoincrement, " +
                        ALARMS_COLUMN_TITLE + " text, " +
                        ALARMS_COLUMN_DAYS + " text, " +
                        ALARMS_COLUMN_ISACTIVE + " integer, " +
                        ALARMS_COLUMN_ISREPEATING + " integer, " +
                        ALARMS_COLUMN_CALENDAR + " text, " +
                        ALARMS_COLUMN_DAYS + " text, " +
                        ALARMS_COLUMN_TYPE + " integer)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ALARMS_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertAlarm(Alarm alarm) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put(ALARMS_COLUMN_TITLE, alarm.getTitle());
        content.put(ALARMS_COLUMN_DESCRIPTION, alarm.getDescription());
        content.put(ALARMS_COLUMN_CALENDAR, alarm.getStringTime());
        content.put(ALARMS_COLUMN_ISACTIVE, alarm.isActive());
        content.put(ALARMS_COLUMN_ISREPEATING, alarm.isRepeating());
        content.put(ALARMS_COLUMN_DAYS, alarm.getStringDays());

        db.insert(ALARMS_TABLE_NAME, null, content);
        return true;
    }
}

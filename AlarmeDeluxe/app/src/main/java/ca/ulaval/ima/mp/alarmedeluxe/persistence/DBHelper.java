package ca.ulaval.ima.mp.alarmedeluxe.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

import java.util.ArrayList;

import ca.ulaval.ima.mp.alarmedeluxe.domain.Alarm;
import ca.ulaval.ima.mp.alarmedeluxe.types.AlarmType;
import ca.ulaval.ima.mp.alarmedeluxe.types.AlarmTypeFactory;

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

    public static final String ALARM_TYPE_TABLE_NAME = "alarmType";
    public static final String ALARM_TYPE_COLUMN_ID = "id";
    public static final String ALARM_TYPE_COLUMN_NAME = "name";
    public static final String ALARM_TYPE_COLUMN_DURATION = "duration";
    public static final String ALARM_TYPE_COLUMN_STRENGTH = "strength";
    public static final String ALARM_TYPE_COLUMN_URL = "url";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table " + ALARMS_TABLE_NAME + " (" +
                        ALARMS_COLUMN_ID + " integer primary key autoincrement, " +
                        ALARMS_COLUMN_TITLE + " text, " +
                        ALARMS_COLUMN_ISACTIVE + " integer, " +
                        ALARMS_COLUMN_ISREPEATING + " integer, " +
                        ALARMS_COLUMN_CALENDAR + " integer, " +
                        ALARMS_COLUMN_DAYS + " text, " +
                        ALARMS_COLUMN_DESCRIPTION + " text, " +
                        ALARMS_COLUMN_TYPE + " integer)" //" integer references " + ALARM_TYPE_TABLE_NAME + " on delete cascade)"
        );

        db.execSQL(
                "create table " + ALARM_TYPE_TABLE_NAME + " (" +
                        ALARM_TYPE_COLUMN_ID + " integer primary key autoincrement, " +
                        ALARM_TYPE_COLUMN_NAME + " text, " +
                        ALARM_TYPE_COLUMN_DURATION + " integer, " +
                        ALARM_TYPE_COLUMN_STRENGTH + " real, " +
                        ALARM_TYPE_COLUMN_URL + " text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ALARMS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ALARM_TYPE_TABLE_NAME);
        onCreate(db);
    }
    /* If we want delete to be in cascade
    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }*/

    public long insertAlarm(Alarm alarm) {
        long alarmTypeId = insertAlarmType(alarm.getType());

        if (alarmTypeId < 0) {
            return -1;
        }
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put(ALARMS_COLUMN_TITLE, alarm.getTitle());
        content.put(ALARMS_COLUMN_DESCRIPTION, alarm.getDescription());
        content.put(ALARMS_COLUMN_CALENDAR, alarm.getTime().getTimeInMillis());
        content.put(ALARMS_COLUMN_ISACTIVE, alarm.isActive());
        content.put(ALARMS_COLUMN_ISREPEATING, alarm.isRepeating());
        content.put(ALARMS_COLUMN_DAYS, alarm.getStringDays());
        content.put(ALARMS_COLUMN_TYPE, alarmTypeId);

        return db.insert(ALARMS_TABLE_NAME, null, content);
    }

    public long insertAlarmType(AlarmType alarmType) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put(ALARM_TYPE_COLUMN_NAME, alarmType.toString());
        content.put(ALARM_TYPE_COLUMN_DURATION, alarmType.getDuration());
        content.put(ALARM_TYPE_COLUMN_STRENGTH, alarmType.getStrength());
        content.put(ALARM_TYPE_COLUMN_URL, alarmType.getURL());

        return db.insert(ALARM_TYPE_TABLE_NAME, null, content);
    }

    public Cursor getAlarm(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from " + ALARMS_TABLE_NAME + " where id="+id+"", null );
        return res;
    }

    public Cursor getAlarmType(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from " + ALARM_TYPE_TABLE_NAME + " where id="+id+"", null );
        return res;
    }

    public Integer deleteAlarm(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(ALARMS_TABLE_NAME, "id = ? ", new String[] { Integer.toString(id) });
    }

    public Integer deleteAlarmType(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(ALARM_TYPE_TABLE_NAME, "id = ? ", new String[] { Integer.toString(id) });
    }

    public ArrayList<Alarm> getAllAlarms() {
        ArrayList<Alarm> array_list = new ArrayList();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorAlarm =  db.rawQuery( "select * from " + ALARMS_TABLE_NAME + " a, " + ALARM_TYPE_TABLE_NAME + " at where a." + ALARMS_COLUMN_TYPE + " = at." + ALARM_TYPE_COLUMN_ID, null );
        cursorAlarm.moveToFirst();

        while(cursorAlarm.isAfterLast() == false){
            AlarmType alarmType = AlarmTypeFactory.getByName(cursorAlarm.getString(cursorAlarm.getColumnIndex(ALARM_TYPE_COLUMN_NAME)));
            Bundle alarmTypeBundle = new Bundle();
            alarmTypeBundle.putInt("id", cursorAlarm.getInt(cursorAlarm.getColumnIndex(ALARM_TYPE_COLUMN_ID)));
            alarmTypeBundle.putString("name", cursorAlarm.getString(cursorAlarm.getColumnIndex(ALARM_TYPE_COLUMN_NAME)));
            alarmTypeBundle.putInt("duration", cursorAlarm.getInt(cursorAlarm.getColumnIndex(ALARM_TYPE_COLUMN_DURATION)));
            alarmTypeBundle.putDouble("strength", cursorAlarm.getDouble(cursorAlarm.getColumnIndex(ALARM_TYPE_COLUMN_STRENGTH)));
            alarmTypeBundle.putString("url", cursorAlarm.getString(cursorAlarm.getColumnIndex(ALARM_TYPE_COLUMN_URL)));
            alarmType.buildFromBundle(alarmTypeBundle);

            Alarm alarm = new Alarm();
            Bundle alarmBundle = new Bundle();
            alarmBundle.putInt("id", cursorAlarm.getInt(cursorAlarm.getColumnIndex(ALARMS_COLUMN_ID)));
            alarmBundle.putString("title", cursorAlarm.getString(cursorAlarm.getColumnIndex(ALARMS_COLUMN_TITLE)));
            alarmBundle.putString("description", cursorAlarm.getString(cursorAlarm.getColumnIndex(ALARMS_COLUMN_DESCRIPTION)));
            alarmBundle.putString("days", cursorAlarm.getString(cursorAlarm.getColumnIndex(ALARMS_COLUMN_DAYS)));
            alarmBundle.putInt("isActive", cursorAlarm.getInt(cursorAlarm.getColumnIndex(ALARMS_COLUMN_ISACTIVE)));
            alarmBundle.putInt("isRepeating", cursorAlarm.getInt(cursorAlarm.getColumnIndex(ALARMS_COLUMN_ISREPEATING)));
            alarmBundle.putLong("calendar", cursorAlarm.getLong(cursorAlarm.getColumnIndex(ALARMS_COLUMN_CALENDAR)));
            alarmBundle.putParcelable("type", alarmType);
            alarm.buildFromBundle(alarmBundle);

            array_list.add(alarm);

            cursorAlarm.moveToNext();
        }
        return array_list;
    }
}

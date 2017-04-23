package ca.ulaval.ima.mp.alarmedeluxe.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

import java.util.ArrayList;

import ca.ulaval.ima.mp.alarmedeluxe.domain.Alarm;
import ca.ulaval.ima.mp.alarmedeluxe.domain.types.AccelerometerAlarmType;
import ca.ulaval.ima.mp.alarmedeluxe.domain.types.AlarmType;
import ca.ulaval.ima.mp.alarmedeluxe.domain.types.AlarmTypeFactory;
import ca.ulaval.ima.mp.alarmedeluxe.domain.types.GeolocationAlarmType;
import ca.ulaval.ima.mp.alarmedeluxe.domain.types.LuminosityAlarmType;
import ca.ulaval.ima.mp.alarmedeluxe.domain.types.MathsAlarmType;
import ca.ulaval.ima.mp.alarmedeluxe.domain.types.StandardAlarmType;
import ca.ulaval.ima.mp.alarmedeluxe.domain.types.YoutubeAlarmType;

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
    public static final String ALARM_TYPE_COLUMN_DESCRIPTION = "description";
    public static final String ALARM_TYPE_COLUMN_DURATION = "duration";
    public static final String ALARM_TYPE_COLUMN_DEFAULT = "isDefault";
    public static final String ALARM_TYPE_COLUMN_STRENGTH = "strength";
    public static final String ALARM_TYPE_COLUMN_URL = "url";

    public static final String SETTINGS = "settings";
    public static final String SETTINGS_COLUMN_NAME = "name";
    public static final String SETTINGS_COLUMN_VALUE = "value";

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
                        ALARMS_COLUMN_TYPE + " integer)"
        );

        db.execSQL(
                "create table " + ALARM_TYPE_TABLE_NAME + " (" +
                        ALARM_TYPE_COLUMN_ID + " integer primary key autoincrement, " +
                        ALARM_TYPE_COLUMN_NAME + " text, " +
                        ALARM_TYPE_COLUMN_DESCRIPTION + " text, " +
                        ALARM_TYPE_COLUMN_DURATION + " real, " +
                        ALARM_TYPE_COLUMN_DEFAULT + " integer, " +
                        ALARM_TYPE_COLUMN_STRENGTH + " real, " +
                        ALARM_TYPE_COLUMN_URL + " text)"
        );

        db.execSQL(
                "create table " + SETTINGS + " (" +
                        SETTINGS_COLUMN_NAME + " text primary key, " +
                        SETTINGS_COLUMN_VALUE + " text)"
        );

        insertAlarmType(new StandardAlarmType(), db);
        insertAlarmType(new AccelerometerAlarmType(), db);
        insertAlarmType(new LuminosityAlarmType(), db);
        insertAlarmType(new YoutubeAlarmType(), db);
        insertAlarmType(new MathsAlarmType(), db);
        insertAlarmType(new GeolocationAlarmType(), db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ALARMS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ALARM_TYPE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + SETTINGS);
        onCreate(db);
    }

    public void updateOrInsertSettings(SQLiteDatabase db, String value, String name){
        if (db == null) {
            db = this.getWritableDatabase();
        }
        ContentValues cv = new ContentValues();
        cv.put(SETTINGS_COLUMN_VALUE,value);
        try{
            db.update(SETTINGS,cv,SETTINGS_COLUMN_NAME+" = '" + name + "'",null);
        }catch (SQLiteException e){
            cv.put(SETTINGS_COLUMN_NAME,name);
            db.insert(SETTINGS,null,cv);
        }
    }

    public String getSettings(String name){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select " + SETTINGS_COLUMN_VALUE + " from " + SETTINGS + " where " + SETTINGS_COLUMN_NAME + " = '" + name + "'", null );
        res.moveToFirst();

        if(res.getCount() == 0){
            return "";
        }

        String s = res.getString(res.getColumnIndex(SETTINGS_COLUMN_VALUE));

        return s;
    }
    /*public Cursor getRingtone(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select "+SETTINGS_COLUMN_VALUE+" from " + SETTINGS + " where "+SETTINGS_COLUMN_NAME+"=ringtone", null );
        String s = res.getString(0);
        return res;
    }*/


    public long insertAlarm(Alarm alarm, SQLiteDatabase db) {
        if (db == null) {
            db = this.getWritableDatabase();
        }
        if (alarm.getType().getAlarmId() == -1) {
            long alarmTypeId = insertAlarmType(alarm.getType(), null);

            if (alarmTypeId < 0) {
                return -1;
            }

            alarm.getType().setAlarmId((int)alarmTypeId);
        }

        ContentValues content = new ContentValues();
        content.put(ALARMS_COLUMN_TITLE, alarm.getTitle());
        content.put(ALARMS_COLUMN_DESCRIPTION, alarm.getDescription());
        content.put(ALARMS_COLUMN_CALENDAR, alarm.getTime().getTimeInMillis());
        content.put(ALARMS_COLUMN_ISACTIVE, alarm.isActive());
        content.put(ALARMS_COLUMN_ISREPEATING, alarm.isRepeating());
        content.put(ALARMS_COLUMN_DAYS, alarm.getStringDays());
        content.put(ALARMS_COLUMN_TYPE, alarm.getType().getAlarmId());

        return db.insert(ALARMS_TABLE_NAME, null, content);
    }

    public long insertAlarmType(AlarmType alarmType, SQLiteDatabase db) {
        if (db == null) {
            db = this.getWritableDatabase();
        }
        ContentValues content = new ContentValues();
        content.put(ALARM_TYPE_COLUMN_NAME, alarmType.toString());
        content.put(ALARM_TYPE_COLUMN_DESCRIPTION, alarmType.getDescription());
        content.put(ALARM_TYPE_COLUMN_DURATION, alarmType.getDuration());
        content.put(ALARM_TYPE_COLUMN_DEFAULT, alarmType.isDefaultAlarm() ? 1 : 0);
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

    public Integer deleteAlarm(Alarm alarm) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(ALARMS_TABLE_NAME, "id = ? ", new String[] { Integer.toString(alarm.getId()) });
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
            alarmTypeBundle.putString("description", cursorAlarm.getString(cursorAlarm.getColumnIndex(ALARM_TYPE_COLUMN_DESCRIPTION)));
            alarmTypeBundle.putDouble("duration", cursorAlarm.getDouble(cursorAlarm.getColumnIndex(ALARM_TYPE_COLUMN_DURATION)));
            alarmTypeBundle.putInt("default", cursorAlarm.getInt(cursorAlarm.getColumnIndex(ALARM_TYPE_COLUMN_DEFAULT)));
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

    public ArrayList<AlarmType> getAllAlarmTypes(String name) {
        ArrayList<AlarmType> array_list = new ArrayList();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorAlarm =  db.rawQuery( "select * from " + ALARM_TYPE_TABLE_NAME + " at where at." + ALARM_TYPE_COLUMN_NAME + " = '" + name + "'", null );
        cursorAlarm.moveToFirst();

        while(cursorAlarm.isAfterLast() == false) {
            AlarmType alarmType = AlarmTypeFactory.getByName(cursorAlarm.getString(cursorAlarm.getColumnIndex(ALARM_TYPE_COLUMN_NAME)));
            Bundle alarmTypeBundle = new Bundle();
            alarmTypeBundle.putInt("id", cursorAlarm.getInt(cursorAlarm.getColumnIndex(ALARM_TYPE_COLUMN_ID)));
            alarmTypeBundle.putString("name", cursorAlarm.getString(cursorAlarm.getColumnIndex(ALARM_TYPE_COLUMN_NAME)));
            alarmTypeBundle.putString("description", cursorAlarm.getString(cursorAlarm.getColumnIndex(ALARM_TYPE_COLUMN_DESCRIPTION)));
            alarmTypeBundle.putDouble("duration", cursorAlarm.getDouble(cursorAlarm.getColumnIndex(ALARM_TYPE_COLUMN_DURATION)));
            alarmTypeBundle.putInt("default", cursorAlarm.getInt(cursorAlarm.getColumnIndex(ALARM_TYPE_COLUMN_DEFAULT)));
            alarmTypeBundle.putDouble("strength", cursorAlarm.getDouble(cursorAlarm.getColumnIndex(ALARM_TYPE_COLUMN_STRENGTH)));
            alarmTypeBundle.putString("url", cursorAlarm.getString(cursorAlarm.getColumnIndex(ALARM_TYPE_COLUMN_URL)));
            alarmType.buildFromBundle(alarmTypeBundle);

            array_list.add(alarmType);

            cursorAlarm.moveToNext();
        }

        return array_list;
    }
}

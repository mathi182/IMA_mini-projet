package ca.ulaval.ima.mp.alarmedeluxe.domain;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ca.ulaval.ima.mp.alarmedeluxe.types.AlarmType;
import ca.ulaval.ima.mp.alarmedeluxe.types.StandardAlarmType;

import static ca.ulaval.ima.mp.alarmedeluxe.MyAlarmManager.updateAlarmManager;

public class Alarm implements Parcelable {

    private int id;
    private String title;
    private String description;
    private AlarmType type;
    private boolean isActive;
    private boolean isRepeating;
    private Calendar calendar;
    private boolean[] days;

    public Alarm() {
        //TODO : Devra être incrémenté dans la BD et non setté
        id = 0;
        title = "Wake up my dear";
        description = "I'm a description.";
        type = new StandardAlarmType();
        isActive = true;
        isRepeating = false;
        calendar = Calendar.getInstance();
        days = new boolean[]{false, false, false, false, false, false, false};
    }

    public Alarm(Parcel parcel){
        calendar = Calendar.getInstance();

        id = parcel.readInt();
        title = parcel.readString();
        description = parcel.readString();
        calendar.setTimeInMillis(parcel.readLong());
        isActive = parcel.readByte() != 0;
        isRepeating = parcel.readByte() != 0;
        days = parcel.createBooleanArray();
        type = parcel.readParcelable(AlarmType.class.getClassLoader());
    }

    public static final Parcelable.Creator<Alarm> CREATOR = new Parcelable.Creator<Alarm>() {
        @Override
        public Alarm createFromParcel(Parcel in) {
            return new Alarm(in);
        }

        @Override
        public Alarm[] newArray(int size) {
            return new Alarm[size];
        }
    };

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getStringTime() {
        if (calendar.get(Calendar.MINUTE) < 10) {
            return calendar.get(Calendar.HOUR_OF_DAY) + ":0" + calendar.get(Calendar.MINUTE);
        }
        return calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);
    }

    public Calendar getTime() {
        return calendar;
    }

    public void setTime(int hours, int minutes) {
        calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hours);
        calendar.set(Calendar.MINUTE, minutes);

        if (calendar.compareTo(Calendar.getInstance()) <= 0) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }
    }

    public boolean[] getDays() {
        return days;
    }
    public void setDays(boolean[] days) {
        this.days = days;

        for (boolean b : days) {
            if (b) {
                isRepeating = true;
                break;
            }
        }
        isRepeating = false;
    }

    public AlarmType getType() { return type; }
    public void setType(AlarmType alarmType) { this.type = alarmType; }

    public boolean isActive() { return isActive; }

    public void setActive(boolean active) {
        isActive = active;

        if (calendar.compareTo(Calendar.getInstance()) <= 0) {
            setTime(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
        }

        updateAlarmManager(this);
    }

    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeLong(calendar.getTimeInMillis());
        dest.writeByte((byte)(isActive ? 1 : 0));
        dest.writeByte((byte)(isRepeating ? 1 : 0));
        dest.writeBooleanArray(days);
        dest.writeParcelable(type, flags);
    }
}

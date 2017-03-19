package ca.ulaval.ima.mp.alarmedeluxe.domain;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Time;
import java.util.Calendar;

import ca.ulaval.ima.mp.alarmedeluxe.types.AlarmType;
import ca.ulaval.ima.mp.alarmedeluxe.types.StandardAlarmType;

public class Alarm implements Parcelable {

    private int id;
    private String title;
    private String description;
    private AlarmType type;
    private boolean isActive;
    private boolean isRepeating;
    private Calendar calendar;

    public Alarm() {
        //TODO : Devra être incrémenté dans la BD et non setté
        id = 0;
        title = "Wake up my dear";
        description = "I'm a description.";
        type = new StandardAlarmType();
        isActive = true;
        isRepeating = false;
        calendar = Calendar.getInstance();
    }

    public Alarm(Parcel parcel){
        calendar = Calendar.getInstance();

        id = parcel.readInt();
        title = parcel.readString();
        description = parcel.readString();
        calendar.setTimeInMillis(parcel.readLong());
        isActive = parcel.readByte() != 0;
        isRepeating = parcel.readByte() != 0;
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
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
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

    public int getId() {
        return id;
    }

    public AlarmType getType() { return type; }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTime(Calendar calendar) {
        this.calendar = calendar;
    }

    public void setTime(int hours, int minutes) {
        calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hours);
        calendar.set(Calendar.MINUTE, minutes);

        if (calendar.compareTo(Calendar.getInstance()) <= 0) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }
    }

    public void setType(AlarmType alarmType) { this.type = alarmType; }

    public boolean isActive() { return isActive; }

    public void setActive(boolean active) {
        //TODO : Update alarm manager
        isActive = active;
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
        dest.writeParcelable(type, flags);
    }
}

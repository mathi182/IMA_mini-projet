package ca.ulaval.ima.mp.alarmedeluxe.domain;

import android.os.Parcel;
import android.os.Parcelable;

import ca.ulaval.ima.mp.alarmedeluxe.types.AlarmType;
import ca.ulaval.ima.mp.alarmedeluxe.types.StandardAlarmType;

public class Alarm implements Parcelable {

    private int id;
    private String title;
    private String description;
    private int hours;
    private int minutes;
    private AlarmType type;
    private boolean isActive;
    private boolean isRepeating;

    public Alarm() {
        //TODO : Devra être incrémenté dans la BD et non setté
        id = 0;
        title = "Wake up my dear";
        description = "I'm a description.";
        hours = 7;
        minutes = 30;
        type = new StandardAlarmType();
        isActive = true;
        isRepeating = false;
    }

    public Alarm(Parcel parcel){
        id = parcel.readInt();
        title = parcel.readString();
        description = parcel.readString();
        hours = parcel.readInt();
        minutes = parcel.readInt();
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

    public String getTime() {
        if (minutes < 10) {
            return String.valueOf(hours) + ":0" + String.valueOf(minutes);
        }
        return String.valueOf(hours) + ":" + String.valueOf(minutes);
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

    public void setHours(int hours) {
        this.hours = hours;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
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
    public int getHours() {
        return hours;
    }

    public int getMinutes() {
        return minutes;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeInt(hours);
        dest.writeInt(minutes);
        dest.writeByte((byte)(isActive ? 1 : 0));
        dest.writeByte((byte)(isRepeating ? 1 : 0));
        dest.writeParcelable(type, flags);
    }
}

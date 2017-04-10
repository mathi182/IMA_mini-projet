package ca.ulaval.ima.mp.alarmedeluxe.domain;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

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

        /*if (calendar.compareTo(Calendar.getInstance()) <= 0) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }*/
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

    public String getStringDays() {
        String str = "";

        for (boolean b : days) {
            if (b) {
                str += "1";
            } else {
                str += "0";
            }
        }

        return str;
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

    public boolean isRepeating() { return isRepeating; }

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

    public void stop() {
        type.stop();
    }

    public void buildFromBundle(Bundle bundle) {
        id = bundle.getInt("id");
        title = bundle.getString("title");
        description = bundle.getString("description");
        isActive = bundle.getInt("isActive") == 1 ? true : false;
        isRepeating = bundle.getInt("isRepeating") == 1 ? true : false;
        type = bundle.getParcelable("type");
        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(bundle.getLong("calendar"));

        for (int i = 0; i < bundle.getString("days").length(); i++) {
            days[i] = bundle.getString("days").charAt(i) == '1';
        }
    }
}

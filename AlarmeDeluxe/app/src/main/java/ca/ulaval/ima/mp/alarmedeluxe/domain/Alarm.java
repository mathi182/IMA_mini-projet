package ca.ulaval.ima.mp.alarmedeluxe.domain;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ca.ulaval.ima.mp.alarmedeluxe.domain.types.AlarmType;
import ca.ulaval.ima.mp.alarmedeluxe.domain.types.StandardAlarmType;

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
        id = 0;
        title = "Wake up my dear";
        description = "";
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
    public void setId(long id) {
        this.id = (int)id;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        updateDescription();
        return description;
    }
    public void updateDescription() {
        description = "";

        if (days[0] && !days[1] && !days[2] && !days[3] && !days[4] && !days[5] && days[6]) {
            description = "Week-ends only";
        } else if (!days[0] && days[1] && days[2] && days[3] && days[4] && days[5] && !days[6]) {
            description = "All days of week";
        } else if (days[0] && days[1] && days[2] && days[3] && days[4] && days[5] && days[6]) {
            description = "Everyday";
        } else {
            List<String> selectedDays = new ArrayList<>();
            if (days[0]) {
                selectedDays.add("Sun.");
            }
            if (days[1]) {
                selectedDays.add("Mon.");
            }
            if (days[2]) {
                selectedDays.add("Tue.");
            }
            if (days[3]) {
                selectedDays.add("Wed.");
            }
            if (days[4]) {
                selectedDays.add("Thu.");
            }
            if (days[5]) {
                selectedDays.add("Fry.");
            }
            if (days[6]) {
                selectedDays.add("Sat.");
            }

            if (!selectedDays.isEmpty()) {
                description = "On : ";
                for (int i = 0; i < selectedDays.size() - 1; i++) {
                    description += selectedDays.get(i) + ", ";
                }
                description += selectedDays.get(selectedDays.size()-1);
            }
        }
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
        calendar.set(Calendar.SECOND, 0);
    }

    public boolean[] getDays() {
        return days;
    }
    public void setDays(boolean[] days) {
        this.days = days;

        for (boolean b : days) {
            if (b) {
                isRepeating = true;
                return;
            }
        }
        isRepeating = false;
        updateDescription();
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
        updateDescription();

        for (int i = 0; i < bundle.getString("days").length(); i++) {
            days[i] = bundle.getString("days").charAt(i) == '1';
        }
    }
}

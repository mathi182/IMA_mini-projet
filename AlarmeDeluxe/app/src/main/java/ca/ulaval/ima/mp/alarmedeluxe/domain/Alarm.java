package ca.ulaval.ima.mp.alarmedeluxe.domain;

import android.os.Parcel;
import android.os.Parcelable;

public class Alarm implements Parcelable {




    private String title;
    private String description;



    private int hours;
    private int minutes;

    public Alarm() {
        title = "Wake up my dear";
        description = "I'm a description.";
        hours = 7;
        minutes = 30;
    }
    public Alarm(Parcel parcel){
        title = parcel.readString();
        description = parcel.readString();
        hours = parcel.readInt();
        minutes = parcel.readInt();
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
        return String.valueOf(hours) + ":" + String.valueOf(minutes);
    }

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
        dest.writeString(title);
        dest.writeString(description);
        dest.writeInt(hours);
        dest.writeInt(minutes);
    }
}

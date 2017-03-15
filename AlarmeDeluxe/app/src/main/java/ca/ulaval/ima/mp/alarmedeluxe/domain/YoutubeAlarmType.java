package ca.ulaval.ima.mp.alarmedeluxe.domain;

import android.os.Parcel;
import android.os.Parcelable;

public class YoutubeAlarmType implements AlarmType {

    private String name;
    private String url;

    public YoutubeAlarmType() {
        name = "YouTube video";
    }

    public YoutubeAlarmType(Parcel in) {

    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    public static final Parcelable.Creator<YoutubeAlarmType> CREATOR = new Parcelable.Creator<YoutubeAlarmType>() {

        @Override
        public YoutubeAlarmType createFromParcel(Parcel source) {
            return new YoutubeAlarmType(source);
        }

        @Override
        public YoutubeAlarmType[] newArray(int size) {
            return new YoutubeAlarmType[size];
        }
    };
}

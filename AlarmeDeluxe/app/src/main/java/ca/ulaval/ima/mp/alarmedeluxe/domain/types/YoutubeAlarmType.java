package ca.ulaval.ima.mp.alarmedeluxe.domain.types;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import ca.ulaval.ima.mp.alarmedeluxe.R;
import ca.ulaval.ima.mp.alarmedeluxe.YoutubeAlarmActivity_;

public class YoutubeAlarmType extends Fragment implements AlarmType {

    private int id;
    private String description;
    private String name;
    private String url;
    private boolean isDefault = true;
    private int logoResource;

    public YoutubeAlarmType() {
        id = -1;
        name = "YouTube video";
        description = "Default";
        logoResource = R.mipmap.ic_youtube_dark;
    }

    public void buildFromParcel(Parcel in) {
        isDefault = false;
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
            YoutubeAlarmType alarmType = new YoutubeAlarmType();
            alarmType.buildFromParcel(source);

            return alarmType;
        }

        @Override
        public YoutubeAlarmType[] newArray(int size) {
            return new YoutubeAlarmType[size];
        }
    };

    @Override
    public Fragment getFragment() {
        return this;
    }

    @Override
    public Activity getAlarmActivity(){
        return new YoutubeAlarmActivity_();
    }

    @Override
    public int getLogoResource() {
        return logoResource;
    }

    @Override
    public int getAlarmId() {
        return id;
    }

    @Override
    public void stop() {

    }

    @Override
    public boolean isDefaultAlarm() {
        return isDefault;
    }

    @Override
    public double getDuration() {
        return 0;
    }

    @Override
    public double getStrength() {
        return 0;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getURL() {
        return url;
    }

    @Override
    public void buildFromBundle(Bundle bundle) {
        id = bundle.getInt("id");
        name = bundle.getString("name");
        url = bundle.getString("url");
        isDefault = bundle.getBoolean("default");
    }

    @Override
    public void setAlarmId(int id) {
        this.id = id;
    }
}

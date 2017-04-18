package ca.ulaval.ima.mp.alarmedeluxe.domain.types;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import ca.ulaval.ima.mp.alarmedeluxe.R;
import ca.ulaval.ima.mp.alarmedeluxe.youtube.YoutubeAlarmActivity;

public class YoutubeAlarmType extends Fragment implements AlarmType {

    private int id;
    private String description;
    private String name;
    private String url;
    private boolean isDefault = true;
    private int logoResource;

    public YoutubeAlarmType() {
        id = -1;
        name = AlarmTypeFactory.YOUTUBE_ALARM_TYPE_NAME;
        description = "Default";
        logoResource = R.mipmap.ic_youtube_dark;
    }

    public void buildFromParcel(Parcel in) {
        id = in.readInt();
        description = in.readString();
        url = in.readString();
        isDefault = in.readInt() == 1;
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
        dest.writeInt(id);
        dest.writeString(description);
        dest.writeString(url);
        dest.writeInt(isDefault ? 1 : 0);
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
        return new YoutubeAlarmActivity();
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
        id = bundle.getInt("id", -1);
        description = bundle.getString("description");
        url = bundle.getString("url", "wZZ7oFKsKzY");
        isDefault = bundle.getInt("default", 0) == 1;
    }

    @Override
    public void setAlarmId(int id) {
        this.id = id;
    }
}

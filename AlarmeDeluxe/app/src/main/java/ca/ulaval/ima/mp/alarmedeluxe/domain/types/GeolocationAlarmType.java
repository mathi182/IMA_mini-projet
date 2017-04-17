package ca.ulaval.ima.mp.alarmedeluxe.domain.types;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import ca.ulaval.ima.mp.alarmedeluxe.AlarmRingingActivity;
import ca.ulaval.ima.mp.alarmedeluxe.R;

public class GeolocationAlarmType extends Fragment implements AlarmType {

    private int id;
    private String name;
    private double longitude;
    private double latitude;
    private int logoResource;

    public GeolocationAlarmType() {
        id = -1;
        name = "Geolocalisation";
        logoResource = R.mipmap.ic_geolocation_dark;
    }

    public void buildFromParcel(Parcel in) {

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

    public static final Parcelable.Creator<GeolocationAlarmType> CREATOR = new Parcelable.Creator<GeolocationAlarmType>() {

        @Override
        public GeolocationAlarmType createFromParcel(Parcel source) {
            GeolocationAlarmType alarmType = new GeolocationAlarmType();
            alarmType.buildFromParcel(source);

            return alarmType;
        }

        @Override
        public GeolocationAlarmType[] newArray(int size) {
            return new GeolocationAlarmType[size];
        }
    };

    @Override
    public Fragment getFragment() {
        return this;
    }

    @Override
    public Activity getAlarmActivity() {
        return new AlarmRingingActivity();
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
        return true;
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
        return null;
    }

    @Override
    public String getURL() {
        return null;
    }

    @Override
    public void buildFromBundle(Bundle bundle) {

    }

    @Override
    public void setAlarmId(int id) {
        this.id = id;
    }
}

package ca.ulaval.ima.mp.alarmedeluxe.types;

import android.app.Fragment;
import android.os.Parcel;
import android.os.Parcelable;

public class GeolocationAlarmType extends Fragment implements AlarmType {

    private String name;
    private double longitude;
    private double latitude;

    public GeolocationAlarmType() {
        name = "Geolocalisation";
    }

    public GeolocationAlarmType(Parcel in) {

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
            return new GeolocationAlarmType(source);
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
}

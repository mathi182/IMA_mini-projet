package ca.ulaval.ima.mp.alarmedeluxe.types;

import android.app.Fragment;
import android.os.Parcel;
import android.os.Parcelable;

import ca.ulaval.ima.mp.alarmedeluxe.R;

public class GeolocationAlarmType extends Fragment implements AlarmType {

    private String name;
    private double longitude;
    private double latitude;
    private int logoResource;

    public GeolocationAlarmType() {
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
    public int getLogoResource() {
        return logoResource;
    }
}

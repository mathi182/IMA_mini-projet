package ca.ulaval.ima.mp.alarmedeluxe.types;

import android.app.Activity;
import android.app.Fragment;
import android.os.Parcel;
import android.os.Parcelable;

import ca.ulaval.ima.mp.alarmedeluxe.AlarmRingingActivity;
import ca.ulaval.ima.mp.alarmedeluxe.R;

public class AccelerometerAlarmType extends Fragment implements AlarmType {

    String name;
    int duration;
    int forceNeeded;
    int logoResource;

    public AccelerometerAlarmType() {
        name = "Shaking";
        logoResource = R.mipmap.ic_accelerometer_dark;
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

    public static final Parcelable.Creator<AccelerometerAlarmType> CREATOR = new Parcelable.Creator<AccelerometerAlarmType>() {

        @Override
        public AccelerometerAlarmType createFromParcel(Parcel source) {
            AccelerometerAlarmType alarmType = new AccelerometerAlarmType();
            alarmType.buildFromParcel(source);

            return alarmType;
        }

        @Override
        public AccelerometerAlarmType[] newArray(int size) {
            return new AccelerometerAlarmType[size];
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
}

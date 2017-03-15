package ca.ulaval.ima.mp.alarmedeluxe.domain;

import android.os.Parcel;
import android.os.Parcelable;

public class AccelerometerAlarmType implements AlarmType {

    String name;
    int duration;
    int forceNeeded;

    public AccelerometerAlarmType() {
        name = "Shaking";
    }

    public AccelerometerAlarmType(Parcel in) {

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
            return new AccelerometerAlarmType(source);
        }

        @Override
        public AccelerometerAlarmType[] newArray(int size) {
            return new AccelerometerAlarmType[size];
        }
    };
}

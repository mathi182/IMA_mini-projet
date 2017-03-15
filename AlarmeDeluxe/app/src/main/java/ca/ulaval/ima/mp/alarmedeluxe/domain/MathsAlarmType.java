package ca.ulaval.ima.mp.alarmedeluxe.domain;

import android.app.Fragment;
import android.os.Parcel;
import android.os.Parcelable;

public class MathsAlarmType extends Fragment implements AlarmType {

    private String name;

    public MathsAlarmType() {
        name = "Maths problem";
    }

    public MathsAlarmType(Parcel in) {

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

    public static final Parcelable.Creator<MathsAlarmType> CREATOR = new Parcelable.Creator<MathsAlarmType>() {

        @Override
        public MathsAlarmType createFromParcel(Parcel source) {
            return new MathsAlarmType(source);
        }

        @Override
        public MathsAlarmType[] newArray(int size) {
            return new MathsAlarmType[size];
        }
    };
}

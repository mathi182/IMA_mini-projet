package ca.ulaval.ima.mp.alarmedeluxe.domain;

import android.app.Fragment;
import android.os.Parcel;
import android.os.Parcelable;

public class StandardAlarmType extends Fragment implements AlarmType {

    private String name;

    public StandardAlarmType() {
        name = "Standard";
    }

    public StandardAlarmType(Parcel in) {

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

    public static final Parcelable.Creator<StandardAlarmType> CREATOR = new Parcelable.Creator<StandardAlarmType>() {

        @Override
        public StandardAlarmType createFromParcel(Parcel source) {
            return new StandardAlarmType(source);
        }

        @Override
        public StandardAlarmType[] newArray(int size) {
            return new StandardAlarmType[size];
        }
    };
}

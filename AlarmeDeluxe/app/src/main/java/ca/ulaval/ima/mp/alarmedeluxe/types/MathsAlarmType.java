package ca.ulaval.ima.mp.alarmedeluxe.types;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import ca.ulaval.ima.mp.alarmedeluxe.AlarmRingingActivity;
import ca.ulaval.ima.mp.alarmedeluxe.R;

public class MathsAlarmType extends Fragment implements AlarmType {

    private String name;
    private int logoResource;

    public MathsAlarmType() {
        name = "Maths problem";
        logoResource = R.mipmap.ic_maths_dark;
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

    public static final Parcelable.Creator<MathsAlarmType> CREATOR = new Parcelable.Creator<MathsAlarmType>() {

        @Override
        public MathsAlarmType createFromParcel(Parcel source) {
            MathsAlarmType alarmType = new MathsAlarmType();
            alarmType.buildFromParcel(source);

            return alarmType;
        }

        @Override
        public MathsAlarmType[] newArray(int size) {
            return new MathsAlarmType[size];
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
    public void stop() {

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
}

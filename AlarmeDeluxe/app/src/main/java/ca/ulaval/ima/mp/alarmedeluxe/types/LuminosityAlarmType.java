package ca.ulaval.ima.mp.alarmedeluxe.types;

import android.app.Activity;
import android.app.Fragment;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ca.ulaval.ima.mp.alarmedeluxe.AlarmRingingActivity;
import ca.ulaval.ima.mp.alarmedeluxe.R;

public class LuminosityAlarmType extends Fragment implements AlarmType {

    private String name;
    private MediaPlayer mediaPlayer;
    private int logoResource;

    public LuminosityAlarmType() {
        name = "Luminosity";
        logoResource = R.mipmap.ic_luminosity_dark; //TODO: Logo
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_luminosity, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

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
    public int describeContents() {
        return 0;
    }

    public void buildFromParcel(Parcel in) {
        name = in.readString();
    }

    public static final Parcelable.Creator<LuminosityAlarmType> CREATOR = new Parcelable.Creator<LuminosityAlarmType>() {

        @Override
        public LuminosityAlarmType createFromParcel(Parcel source) {
            LuminosityAlarmType alarmType = new LuminosityAlarmType();
            alarmType.buildFromParcel(source);

            return alarmType;
        }

        @Override
        public LuminosityAlarmType[] newArray(int size) {
            return new LuminosityAlarmType[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
    }

    @Override
    public String toString() {
        return name;
    }
}

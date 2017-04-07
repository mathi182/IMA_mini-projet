package ca.ulaval.ima.mp.alarmedeluxe.types;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ca.ulaval.ima.mp.alarmedeluxe.AlarmRingingActivity;
import ca.ulaval.ima.mp.alarmedeluxe.R;

public class LuminosityAlarmType extends Fragment implements AlarmType, SensorEventListener {

    private String name;
    private MediaPlayer mediaPlayer;
    private int logoResource;
    private SensorManager sensorManager;
    private float lightStrength;
    private float lightStrengthAtStart = -1;
    private Activity activity;

    public LuminosityAlarmType() {
        name = "Luminosity";
        logoResource = R.mipmap.ic_luminosity_dark;
        lightStrength = 30;
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

        sensorManager = (SensorManager)getActivity().getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT),SensorManager.SENSOR_DELAY_NORMAL);

        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        }

        //Ringtone ringtone = RingtoneManager.getRingtone(this, alarmUri);
        mediaPlayer = MediaPlayer.create(getActivity().getApplicationContext(),alarmUri);
        mediaPlayer.start();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
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
    public void stop() {
        sensorManager.unregisterListener(this);
        mediaPlayer.stop();
        mediaPlayer.reset();
        activity.finish();
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

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (lightStrengthAtStart == -1) {
            lightStrengthAtStart = event.values[0];
        }
        if (lightStrengthAtStart + lightStrength >= 360) {
            lightStrength = 360 - lightStrengthAtStart;
        }
        if (event.values[0] >= lightStrengthAtStart + lightStrength) {
            stop();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}

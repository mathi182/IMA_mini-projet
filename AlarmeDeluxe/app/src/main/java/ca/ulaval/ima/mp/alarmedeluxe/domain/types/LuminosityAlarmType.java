package ca.ulaval.ima.mp.alarmedeluxe.domain.types;

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
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ca.ulaval.ima.mp.alarmedeluxe.AlarmRingingActivity;
import ca.ulaval.ima.mp.alarmedeluxe.R;
import ca.ulaval.ima.mp.alarmedeluxe.persistence.DBHelper;

public class LuminosityAlarmType extends Fragment implements AlarmType, SensorEventListener {

    private int id;
    private String name;
    private String description;
    private boolean isDefault = true;
    private MediaPlayer mediaPlayer;
    private int logoResource;
    private SensorManager sensorManager;
    private double lightStrength;
    private double lightStrengthAtStart = -1;
    private Activity activity;
    private TextView txt_luminosity;
    private static final int MAX_VOLUME = 100;
    private Vibrator vib;

    public LuminosityAlarmType() {
        id = -1;
        name = "Luminosity";
        description = "Default";
        logoResource = R.mipmap.ic_luminosity_dark;
        lightStrength = 30;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.alarm_ringing_luminosity, container, false);

        txt_luminosity = (TextView)view.findViewById(R.id.txt_luminosity);
        txt_luminosity.setText("");

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        sensorManager = (SensorManager)getActivity().getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT),SensorManager.SENSOR_DELAY_NORMAL);

        DBHelper database = new DBHelper(getActivity());

        String encodedUri = database.getSettings("ringtone");
        Uri alarmUri;
        if (encodedUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        }
        else{
            alarmUri =Uri.parse(encodedUri);
        }

        String volumeText = database.getSettings("volume");
        int desiredVolume;
        if(volumeText == null){
            desiredVolume = 100;
        }else{
            desiredVolume = (int)(Double.parseDouble(volumeText)*MAX_VOLUME);
        }

        //Ringtone ringtone = RingtoneManager.getRingtone(this, alarmUri);
        mediaPlayer = MediaPlayer.create(getActivity().getApplicationContext(),alarmUri);
        float volume = (float) (1 - (Math.log(MAX_VOLUME - desiredVolume) / Math.log(MAX_VOLUME)));
        mediaPlayer.setVolume(volume,volume);
        vib =(Vibrator)getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        String toggleOn = database.getSettings("vibration");
        if(toggleOn == null || toggleOn.equals("false")){
            mediaPlayer.start();
        }else{
            long[] pattern = {0, 500, 1000};
            vib.vibrate(pattern,0);
        }
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
    public int getAlarmId() {
        return id;
    }

    @Override
    public void stop() {
        sensorManager.unregisterListener(this);
        mediaPlayer.stop();
        mediaPlayer.reset();
        activity.finish();
    }

    @Override
    public boolean isDefaultAlarm() {
        return isDefault;
    }

    @Override
    public double getDuration() {
        return 0;
    }

    @Override
    public double getStrength() {
        return lightStrength;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getURL() {
        return null;
    }

    @Override
    public void buildFromBundle(Bundle bundle) {
        id = bundle.getInt("id");
        description = bundle.getString("description");
        lightStrength = bundle.getDouble("strength");
        isDefault = bundle.getInt("default") == 1;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void buildFromParcel(Parcel in) {
        name = in.readString();
        isDefault = in.readInt() == 1;
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
        dest.writeInt(isDefault ? 1 : 0);
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (lightStrengthAtStart == -1) {
            lightStrengthAtStart = event.values[0];
            txt_luminosity.setText(String.valueOf(lightStrengthAtStart/lightStrength) + "%");
        }
        if (lightStrengthAtStart + lightStrength >= 360) {
            lightStrength = 360 - lightStrengthAtStart;
        }
        if (event.values[0] >= lightStrengthAtStart + lightStrength) {
            stop();
        }

        double ratio = event.values[0]/(lightStrengthAtStart + lightStrength);

        if (ratio > 1) {
            ratio = 1;
        }

        ratio = Math.round(ratio * 100) / 100;

        txt_luminosity.setText(String.valueOf(ratio) + "%");
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void setAlarmId(int id) {
        this.id = id;
    }
}

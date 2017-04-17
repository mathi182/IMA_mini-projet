package ca.ulaval.ima.mp.alarmedeluxe.domain.types;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.hardware.SensorEventListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import ca.ulaval.ima.mp.alarmedeluxe.AlarmRingingActivity;
import ca.ulaval.ima.mp.alarmedeluxe.R;

public class AccelerometerAlarmType extends Fragment implements AlarmType, SensorEventListener {

    private int id;
    private String name;
    private String description;
    private double duration;
    private double forceNeeded;
    private int logoResource;
    private boolean isDefault = true;
    private SensorManager mySensorManager;
    /* Here we store the current values of acceleration, one for each axis */
    private float xAccel;
    private float yAccel;
    private float zAccel;
    /* And here the previous ones */
    private float xPreviousAccel;
    private float yPreviousAccel;
    private float zPreviousAccel;
    /* Used to suppress the first shaking */
    private boolean firstUpdate = true;
    /*What acceleration difference would we assume as a rapid movement? */
    private final double shakeThreshold = 15;
    /* Has a shaking motion been started (one direction) */
    private boolean shakeInitiated = false;
    private ProgressBar progressBar;
    private Activity activity;
    private MediaPlayer mediaPlayer;

    public AccelerometerAlarmType() {
        id = -1;
        name = "Shaking";
        description = "Default";
        logoResource = R.mipmap.ic_accelerometer_dark;
    }

    public void buildFromParcel(Parcel in) {
        id = in.readInt();
        name = in.readString();
        description = in.readString();
        forceNeeded = in.readDouble();
        duration = in.readDouble();
        isDefault = in.readInt() == 1 ? true : false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_alarmringing_accelerometer, container, false);

        return view;
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
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeDouble(forceNeeded);
        dest.writeDouble(duration);
        dest.writeInt(isDefault ? 1 : 0);
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

    @Override
    public int getAlarmId() {
        return id;
    }

    @Override
    public void setAlarmId(int id) {
        this.id = id;
    }

    @Override
    public void stop() {
        mySensorManager.unregisterListener(this);
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
        return duration;
    }

    @Override
    public double getStrength() {
        return forceNeeded;
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
        duration = bundle.getDouble("duration");
        forceNeeded = bundle.getDouble("strength");
        isDefault = bundle.getBoolean("default");
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        updateAccelParameters(event.values[0], event.values[1], event.values[2]);
        if ((!shakeInitiated) && isAccelerationChanged()) {
            shakeInitiated = true;
        } else if ((shakeInitiated) && isAccelerationChanged()) {
            executeShakeAction();
        } else if ((shakeInitiated) && (!isAccelerationChanged())) {
            shakeInitiated = false;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        progressBar = (ProgressBar)getActivity().findViewById(R.id.shaking_progressBar);
        mySensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        mySensorManager.registerListener(this, mySensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_NORMAL);

        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        }

        //Ringtone ringtone = RingtoneManager.getRingtone(this, alarmUri);
        mediaPlayer = MediaPlayer.create(getActivity().getApplicationContext(),alarmUri);
        mediaPlayer.start();
    }

    private void updateAccelParameters(float xNewAccel, float yNewAccel, float zNewAccel) {
        if (firstUpdate) {
            xPreviousAccel = xNewAccel;
            yPreviousAccel = yNewAccel;
            zPreviousAccel = zNewAccel;
            firstUpdate = false;
        } else {
            xPreviousAccel = xAccel;
            yPreviousAccel = yAccel;
            zPreviousAccel = zAccel;
        }
        xAccel = xNewAccel;
        yAccel = yNewAccel;
        zAccel = zNewAccel;
    }

    private boolean isAccelerationChanged() {
        double deltaX = Math.abs(xPreviousAccel - xAccel);
        double deltaY = Math.abs(yPreviousAccel - yAccel);
        double deltaZ = Math.abs(zPreviousAccel - zAccel);
        double customThreshold = shakeThreshold * forceNeeded;

        return (deltaX > customThreshold && deltaY > customThreshold)
                || (deltaX > customThreshold && deltaZ > customThreshold)
                || (deltaY > customThreshold && deltaZ > customThreshold);
    }

    private void executeShakeAction() {
		progressBar.setProgress(progressBar.getProgress() + 2);

        if (progressBar.getProgress() == 100) {
            stop();
        }
    }
}

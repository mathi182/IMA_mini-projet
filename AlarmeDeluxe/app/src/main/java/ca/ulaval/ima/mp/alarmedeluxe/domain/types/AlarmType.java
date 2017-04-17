package ca.ulaval.ima.mp.alarmedeluxe.domain.types;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Parcelable;

public interface AlarmType extends Parcelable {
    Fragment getFragment();
    Activity getAlarmActivity();
    int getLogoResource();
    int getAlarmId();
    void setAlarmId(int id);
    void stop();
    boolean isDefaultAlarm();
    double getDuration();
    double getStrength();
    String getDescription();
    String getURL();
    void buildFromBundle(Bundle bundle);
}

package ca.ulaval.ima.mp.alarmedeluxe.types;

import android.app.Fragment;
import android.os.Parcelable;

public interface AlarmType extends Parcelable {
    Fragment getFragment();
}

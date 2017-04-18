package ca.ulaval.ima.mp.alarmedeluxe.domain.types;

import android.app.Activity;
import android.app.Fragment;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ca.ulaval.ima.mp.alarmedeluxe.AlarmRingingActivity;
import ca.ulaval.ima.mp.alarmedeluxe.R;

public class StandardAlarmType extends Fragment implements AlarmType {

    private int id;
    private String name;
    private String description;
    private boolean isDefault = true;
    private MediaPlayer mediaPlayer;
    private Button btn_close;
    private int m_alarmId;
    private int logoResource;

    public StandardAlarmType() {
        id = -1;
        name = "Standard";
        description = "Default";
        logoResource = R.mipmap.ic_newalarm;
    }

    public void buildFromParcel(Parcel in) {
        name = in.readString();
        isDefault = in.readInt() == 1 ? true : false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_alarmringing_standard, container, false);

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
        dest.writeString(name);
        dest.writeInt(isDefault ? 1 : 0);
    }

    public static final Parcelable.Creator<StandardAlarmType> CREATOR = new Parcelable.Creator<StandardAlarmType>() {

        @Override
        public StandardAlarmType createFromParcel(Parcel source) {
            StandardAlarmType alarmType = new StandardAlarmType();
            alarmType.buildFromParcel(source);

            return alarmType;
        }

        @Override
        public StandardAlarmType[] newArray(int size) {
            return new StandardAlarmType[size];
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
    public void stop() {
        mediaPlayer.stop();
        mediaPlayer.release();
        getActivity().finish();
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
        return 0;
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
        name = bundle.getString("name");
        isDefault = bundle.getInt("default") == 1;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        }

        //Ringtone ringtone = RingtoneManager.getRingtone(this, alarmUri);
        mediaPlayer = MediaPlayer.create(getActivity().getApplicationContext(),alarmUri);
        mediaPlayer.start();
        // Get the alarm ID from the intent extra data
        Bundle extras = getArguments();

        if (extras != null) {
            m_alarmId = extras.getInt("AlarmID", -1);
        } else {
            m_alarmId = -1;
        }

        btn_close = (Button)getActivity().findViewById(R.id.btn_close);
        btn_close.setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                stop();
            }
        });
    }

    @Override
    public void setAlarmId(int id) {
        this.id = id;
    }
}

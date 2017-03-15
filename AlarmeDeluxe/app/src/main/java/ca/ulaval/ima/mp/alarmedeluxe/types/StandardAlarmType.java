package ca.ulaval.ima.mp.alarmedeluxe.types;

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

import ca.ulaval.ima.mp.alarmedeluxe.R;

public class StandardAlarmType extends Fragment implements AlarmType {

    private String name;
    private MediaPlayer mediaPlayer;
    private Button btn_close;
    private int m_alarmId;

    public StandardAlarmType() {
        name = "Standard";
    }

    public StandardAlarmType(Parcel in) {
        name = in.readString();
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

    @Override
    public Fragment getFragment() {
        return this;
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
                mediaPlayer.stop();
                mediaPlayer.release();
                getActivity().finish();
            }
        });
    }
}

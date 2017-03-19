package ca.ulaval.ima.mp.alarmedeluxe.types;

import android.app.Fragment;
import android.os.Parcel;
import android.os.Parcelable;

import ca.ulaval.ima.mp.alarmedeluxe.R;

public class YoutubeAlarmType extends Fragment implements AlarmType {

    private String name;
    private String url;
    private int logoResource;

    public YoutubeAlarmType() {
        name = "YouTube video";
        logoResource = R.mipmap.ic_youtube_dark;
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

    public static final Parcelable.Creator<YoutubeAlarmType> CREATOR = new Parcelable.Creator<YoutubeAlarmType>() {

        @Override
        public YoutubeAlarmType createFromParcel(Parcel source) {
            YoutubeAlarmType alarmType = new YoutubeAlarmType();
            alarmType.buildFromParcel(source);

            return alarmType;
        }

        @Override
        public YoutubeAlarmType[] newArray(int size) {
            return new YoutubeAlarmType[size];
        }
    };

    @Override
    public Fragment getFragment() {
        return this;
    }

    @Override
    public int getLogoResource() {
        return logoResource;
    }
}

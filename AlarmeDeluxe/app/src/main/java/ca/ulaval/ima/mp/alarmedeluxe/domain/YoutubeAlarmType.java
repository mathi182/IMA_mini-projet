package ca.ulaval.ima.mp.alarmedeluxe.domain;

public class YoutubeAlarmType implements AlarmType {

    private String name;
    private String url;

    public YoutubeAlarmType() {
        name = "YouTube video";
    }

    @Override
    public String toString() {
        return name;
    }
}

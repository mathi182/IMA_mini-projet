package ca.ulaval.ima.mp.alarmedeluxe.domain;

public class YoutubeAlarmType implements AlarmType {

    private String name;
    private String url;

    @Override
    public String getName() {
        return name;
    }
}

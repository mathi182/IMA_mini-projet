package ca.ulaval.ima.mp.alarmedeluxe.domain;

public class GeolocationAlarmType implements AlarmType {

    private String name;
    private double longitude;
    private double latitude;

    @Override
    public String getName() {
        return name;
    }
}

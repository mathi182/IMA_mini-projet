package ca.ulaval.ima.mp.alarmedeluxe.domain;

public class GeolocationAlarmType implements AlarmType {

    private String name;
    private double longitude;
    private double latitude;

    public GeolocationAlarmType() {
        name = "Geolocalisation";
    }

    @Override
    public String toString() {
        return name;
    }
}

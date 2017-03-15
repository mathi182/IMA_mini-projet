package ca.ulaval.ima.mp.alarmedeluxe.domain;

public class AccelerometerAlarmType implements AlarmType {

    String name;
    int duration;
    int forceNeeded;

    public AccelerometerAlarmType() {
        name = "Shaking";
    }

    @Override
    public String toString() {
        return name;
    }
}

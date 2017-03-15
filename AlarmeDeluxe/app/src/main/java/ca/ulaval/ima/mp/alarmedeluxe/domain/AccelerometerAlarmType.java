package ca.ulaval.ima.mp.alarmedeluxe.domain;

public class AccelerometerAlarmType implements AlarmType {

    String name;
    int duration;
    int forceNeeded;

    @Override
    public String getName() {
        return name;
    }
}

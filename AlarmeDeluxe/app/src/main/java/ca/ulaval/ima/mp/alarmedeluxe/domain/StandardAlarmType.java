package ca.ulaval.ima.mp.alarmedeluxe.domain;

public class StandardAlarmType implements AlarmType {

    private String name;

    public StandardAlarmType() {
        name = "Standard";
    }

    @Override
    public String toString() {
        return name;
    }
}

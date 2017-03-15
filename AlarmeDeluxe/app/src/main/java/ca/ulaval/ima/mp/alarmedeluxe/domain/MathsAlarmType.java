package ca.ulaval.ima.mp.alarmedeluxe.domain;

public class MathsAlarmType implements AlarmType {

    private String name;

    public MathsAlarmType() {
        name = "Maths problem";
    }

    @Override
    public String toString() {
        return name;
    }
}

package ca.ulaval.ima.mp.alarmedeluxe.domain;

public class Alarm {

    private String title;
    private String description;
    private int hours;
    private int minutes;

    public Alarm() {
        title = "Wake up my dear";
        description = "I'm a description.";
        hours = 7;
        minutes = 30;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getTime() {
        return String.valueOf(hours) + ":" + String.valueOf(minutes);
    }
}

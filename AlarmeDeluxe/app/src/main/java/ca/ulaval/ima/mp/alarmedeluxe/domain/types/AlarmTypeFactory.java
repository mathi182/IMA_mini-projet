package ca.ulaval.ima.mp.alarmedeluxe.domain.types;

public final class AlarmTypeFactory {
    public static final String STANDARD_ALARM_TYPE_NAME = "Standard";
    public static final String ACCELEROMETER_ALARM_TYPE_NAME = "Shaking";
    public static final String GEOLOCALISATION_ALARM_TYPE_NAME = "Geolocalisation";
    public static final String LUMINOSITY_ALARM_TYPE_NAME = "Luminosity";
    public static final String MATHS_ALARM_TYPE_NAME = "Maths";
    public static final String YOUTUBE_ALARM_TYPE_NAME = "Youtube";

    public static AlarmType getByName(String name) {
        switch (name) {
            case STANDARD_ALARM_TYPE_NAME:
                return new StandardAlarmType();
            case ACCELEROMETER_ALARM_TYPE_NAME:
                return new AccelerometerAlarmType();
            case GEOLOCALISATION_ALARM_TYPE_NAME:
                return new GeolocationAlarmType();
            case LUMINOSITY_ALARM_TYPE_NAME:
                return new LuminosityAlarmType();
            case MATHS_ALARM_TYPE_NAME:
                return new MathsAlarmType();
            case YOUTUBE_ALARM_TYPE_NAME:
                return new YoutubeAlarmType();
        }

        return null;
    }
}

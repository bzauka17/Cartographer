package itcom.cartographer.Database;

public class LocationHistoryObject {

    private String timestampMs;
    private double latitudeE7;
    private double longitudeE7;
    private int accuracy;
    private int velocity;
    private int heading;
    private int altitude;
    private int verticalAccuracy;

    public LocationHistoryObject(String timestampMs, double latitudeE7, double longitudeE7, int accuracy, int velocity, int heading, int altitude, int verticalAccuracy) {
        this.timestampMs = timestampMs;
        this.latitudeE7 = latitudeE7;
        this.longitudeE7 = longitudeE7;
        this.accuracy = accuracy;
        this.velocity = velocity;
        this.heading = heading;
        this.altitude = altitude;
        this.verticalAccuracy = verticalAccuracy;
    }

    public LocationHistoryObject() {
    }

    public String getTimestampMs() {
        return timestampMs;
    }

    public void setTimestampMs(String timestampMs) {
        this.timestampMs = timestampMs;
    }

    public double getLatitudeE7() {
        return latitudeE7;
    }

    public void setLatitudeE7(double latitudeE7) {
        this.latitudeE7 = latitudeE7;
    }

    public double getLongitudeE7() {
        return longitudeE7;
    }

    public void setLongitudeE7(double longitudeE7) {
        this.longitudeE7 = longitudeE7;
    }

    public int getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(int accuracy) {
        this.accuracy = accuracy;
    }

    public int getVelocity() {
        return velocity;
    }

    public void setVelocity(int velocity) {
        this.velocity = velocity;
    }

    public int getHeading() {
        return heading;
    }

    public void setHeading(int heading) {
        this.heading = heading;
    }

    public int getAltitude() {
        return altitude;
    }

    public void setAltitude(int altitude) {
        this.altitude = altitude;
    }

    public int getVerticalAccuracy() {
        return verticalAccuracy;
    }

    public void setVerticalAccuracy(int verticalAccuracy) {
        this.verticalAccuracy = verticalAccuracy;
    }
}

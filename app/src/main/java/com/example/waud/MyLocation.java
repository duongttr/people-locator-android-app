package com.example.waud;

import android.os.Bundle;

public class MyLocation {
//    private double longitude;
//    private double latitude;
//    private float bearing;


    public String provider = "WAUD-Provider";
    public long time = 0;
    public long elapsedRealtimeNanos = 0;
    public double elapsedRealtimeUncertaintyNanos = 0.0f;
    public double latitude = 0.0f;
    public double longitude = 0.0f;
    public double altitude = 0.0f;
    public float speed = 0.0f;
    public float bearing = 0.0f;
    public float horizontalAccuracyMeters = 0.0f;
    public float verticalAccuracyMeters = 0.0f;
    public float speedAccuracyMetersPerSecond = 0.0f;
    public float bearingAccuracyDegrees = 0.0f;
    public Bundle extras = null;
    public boolean complete = false;
    public boolean fromMockProvider = false;

    public MyLocation() {
    }

    public MyLocation(String provider, long time, long elapsedRealtimeNanos, double elapsedRealtimeUncertaintyNanos, double latitude, double longitude, double altitude, float speed, float bearing, float horizontalAccuracyMeters, float verticalAccuracyMeters, float speedAccuracyMetersPerSecond, float bearingAccuracyDegrees, Bundle extras, boolean complete, boolean fromMockProvider) {
        this.provider = provider;
        this.time = time;
        this.elapsedRealtimeNanos = elapsedRealtimeNanos;
        this.elapsedRealtimeUncertaintyNanos = elapsedRealtimeUncertaintyNanos;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.speed = speed;
        this.bearing = bearing;
        this.horizontalAccuracyMeters = horizontalAccuracyMeters;
        this.verticalAccuracyMeters = verticalAccuracyMeters;
        this.speedAccuracyMetersPerSecond = speedAccuracyMetersPerSecond;
        this.bearingAccuracyDegrees = bearingAccuracyDegrees;
        this.extras = extras;
        this.complete = complete;
        this.fromMockProvider = fromMockProvider;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getElapsedRealtimeNanos() {
        return elapsedRealtimeNanos;
    }

    public void setElapsedRealtimeNanos(long elapsedRealtimeNanos) {
        this.elapsedRealtimeNanos = elapsedRealtimeNanos;
    }

    public double getElapsedRealtimeUncertaintyNanos() {
        return elapsedRealtimeUncertaintyNanos;
    }

    public void setElapsedRealtimeUncertaintyNanos(double elapsedRealtimeUncertaintyNanos) {
        this.elapsedRealtimeUncertaintyNanos = elapsedRealtimeUncertaintyNanos;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getBearing() {
        return bearing;
    }

    public void setBearing(float bearing) {
        this.bearing = bearing;
    }

    public float getHorizontalAccuracyMeters() {
        return horizontalAccuracyMeters;
    }

    public void setHorizontalAccuracyMeters(float horizontalAccuracyMeters) {
        this.horizontalAccuracyMeters = horizontalAccuracyMeters;
    }

    public float getVerticalAccuracyMeters() {
        return verticalAccuracyMeters;
    }

    public void setVerticalAccuracyMeters(float verticalAccuracyMeters) {
        this.verticalAccuracyMeters = verticalAccuracyMeters;
    }

    public float getSpeedAccuracyMetersPerSecond() {
        return speedAccuracyMetersPerSecond;
    }

    public void setSpeedAccuracyMetersPerSecond(float speedAccuracyMetersPerSecond) {
        this.speedAccuracyMetersPerSecond = speedAccuracyMetersPerSecond;
    }

    public float getBearingAccuracyDegrees() {
        return bearingAccuracyDegrees;
    }

    public void setBearingAccuracyDegrees(float bearingAccuracyDegrees) {
        this.bearingAccuracyDegrees = bearingAccuracyDegrees;
    }

    public Bundle getExtras() {
        return extras;
    }

    public void setExtras(Bundle extras) {
        this.extras = extras;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public boolean isFromMockProvider() {
        return fromMockProvider;
    }

    public void setFromMockProvider(boolean fromMockProvider) {
        this.fromMockProvider = fromMockProvider;
    }
}

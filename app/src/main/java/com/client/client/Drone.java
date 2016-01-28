package com.client.client;

public class Drone {

    private int id = 0;
    private String name = "null";
    private double lat = 0.0;
    private double lon = 0.0;
    private int battery = 100;

    public Drone(int _id, String _name, double _lat, double _lon, int _battery) {
        id = _id;
        name = _name;
        lat = _lat;
        lon = _lon;
        battery = _battery;
    }

    int getId() {
        return id;
    }

    double getLat() {
        return lat;
    }

    double getLon() {
        return lon;
    }

    int getBattery() {
        return battery;
    }

    String getName() { return name; }

    void updateBatteryInfo(int _battery) {
        battery = _battery;
    }

    void updateCoords(double _lat, double _lon) {
        lat = _lat;
        lon = _lon;
    }
}

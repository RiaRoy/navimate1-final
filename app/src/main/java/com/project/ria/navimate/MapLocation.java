package com.project.ria.navimate;

/**
 * Created by skynet on 3/4/18.
 */


public class MapLocation {

    public String id;
    public String phone;
    public String uname;
    public String latitude,longitude;

    public MapLocation() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public MapLocation(String id, String phone, String uname, String latitude, String longitude) {
        this.id = id;
        this.phone = phone;
        this.uname = uname;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
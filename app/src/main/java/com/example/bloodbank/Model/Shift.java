package com.example.bloodbank.Model;

public class Shift {

    String time;
    String hospitalId;

    public Shift(String time, String hospitalId) {
        this.time = time;
        this.hospitalId = hospitalId;
    }

    public Shift() {
    }

    public String getTime() {
        return time;
    }

    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }

    public void setTime(String time) {
        this.time = time;
    }
}

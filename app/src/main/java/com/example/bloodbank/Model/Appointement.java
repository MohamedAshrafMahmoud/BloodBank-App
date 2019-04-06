package com.example.bloodbank.Model;

public class Appointement {

    String name;
    String date;
    String area;
    String hospital;
    String shift;
    String condition;
    String reason;
    boolean done = false;

    String id;

    public Appointement(String name, String date, String area, String hospital, String shift, String condition, String reason, boolean done) {
        this.name = name;
        this.date = date;
        this.area = area;
        this.hospital = hospital;
        this.shift = shift;
        this.condition = condition;
        this.reason = reason;
        this.done = done;
    }

    public Appointement() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public String getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }
}

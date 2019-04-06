package com.example.bloodbank.Model;

public class Hospital {

    String name;
    String areaId;
    String id;



    public Hospital(String name, String areaId) {
        this.name = name;
        this.areaId = areaId;
    }

    public Hospital() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }
}

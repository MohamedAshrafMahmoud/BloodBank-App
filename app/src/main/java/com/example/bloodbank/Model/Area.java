package com.example.bloodbank.Model;

public class Area {

    String name;
    String id;

    public Area(String name) {
        this.name = name;
    }

    public Area() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

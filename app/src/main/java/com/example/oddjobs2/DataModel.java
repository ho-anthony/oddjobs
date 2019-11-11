package com.example.oddjobs2;

public class DataModel {
    String name;
    String des;
    String pay;
    String location;

    // Create a new DataModel with id, image, and name
    public DataModel(String name, String des, String pay, String location) {
        this.name = name;
        this.des = des;
        this.pay = pay;
        this.location = location;
    }

    // Get the name from the DataModel
    public String getName() {
        return name;
    }

    // Get the id from the DataModel
    public String getDes() {
        return des;
    }

    // Get the image from the DataModel
    public String getPay() {
        return pay;
    }

    public String getLocation(){
        return location;
    }
}

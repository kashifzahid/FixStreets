package com.example.fixstreet.Object;

public class incident_type {
    public  String type;
    public String id;
    public int img;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public incident_type(String type, String id, int img) {
        this.img=img;
        this.type = type;
        this.id = id;
    }
}

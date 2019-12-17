package com.example.fixstreet.Object;

import android.net.Uri;

public class incident_type {
    public  String type;
    public String id;
    public int img;
    public Uri url;

    public incident_type(Uri uri) {
        this.url = uri;
    }

    public Uri getUrl() {
        return url;
    }

    public void setUrl(Uri url) {
        this.url = url;
    }

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

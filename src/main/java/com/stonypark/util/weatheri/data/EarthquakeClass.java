package com.stonypark.util.weatheri.data;

/**
 * Created by NgocSon on 6/19/2017.
 */

public class EarthquakeClass {
    private String Time;
    private String center;
    private String mt;
    private String loc;
    private String rem;
    private String img;

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getCenter() {
        return center;
    }

    public void setCenter(String center) {
        this.center = center;
    }

    public String getMt() {
        return mt;
    }

    public void setMt(String mt) {
        this.mt = mt;
    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    public String getRem() {
        return rem;
    }

    public void setRem(String rem) {
        this.rem = rem;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    @Override
    public String toString() {
        return "EarthquakeClass{" +
                "Time='" + Time + '\'' +
                ", center='" + center + '\'' +
                ", mt='" + mt + '\'' +
                ", loc='" + loc + '\'' +
                ", rem='" + rem + '\'' +
                ", img='" + img + '\'' +
                '}';
    }
}

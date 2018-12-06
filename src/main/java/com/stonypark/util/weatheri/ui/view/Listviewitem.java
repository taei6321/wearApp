package com.stonypark.util.weatheri.ui.view;

public class Listviewitem {

    private String listDate;
    private String hour;
    private int weather;
    private double listTemp;
    private double minTemp;
    private double maxTemp;
    private int position;

    public Listviewitem(int position){
        this.position = position;
    }

    public Listviewitem(String listDate, String hour, int weather, double listTemp, int position){
        this.listDate = listDate;
        this.hour = hour;
        this.weather = weather;
        this.listTemp = listTemp;
        this.position = position;
    }

    public Listviewitem(String listDate, int weather, double minTemp, double maxTemp, int position){
        this.listDate = listDate;
        this.weather = weather;
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        this.position = position;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getWeather() {
        return weather;
    }

    public double getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(double minTemp) {
        this.minTemp = minTemp;
    }

    public double getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(double maxTemp) {
        this.maxTemp = maxTemp;
    }

    public void setListDate(String listDate) {
        this.listDate = listDate;
    }

    public void setWeather(int listImage) {
        this.weather = listImage;
    }

    public void setListTemp(double listTemp) {
        this.listTemp = listTemp;
    }

    public String getListDate() {
        return listDate;
    }

    public int geWeather() {
        return weather;
    }

    public double getListTemp() {
        return listTemp;
    }
}

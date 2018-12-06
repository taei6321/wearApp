package com.stonypark.util.weatheri.data;

/**
 * Created by soku on 2016-03-29.
 */
public class CityItem {
    private String region;
    private String name1;
    private String name2;
    private Long oversea;

    public CityItem()
    {
        ;
    }

    public void setRegion(String region)   { this.region = region;}
    public String getRegion() {return region;}
    public void setName1(String name1) { this.name1 = name1;}
    public String getName1() {return name1;}
    public void setName2(String name2) {this.name2 = name2;}
    public String getName2() {return name2;}

    public Long getOversea() {
        return oversea;
    }

    public void setOversea(Long oversea) {
        this.oversea = oversea;
    }
}

package com.stonypark.util.weatheri.data;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by soku on 2015-12-14.
 */
public class TyphoonClass {
    private String img;
    private String date;
    private Calendar dateTime;
    private int typnNo;
    private String typnNm;
    private String typnEngNm;
    private String locaDesc;
    private String drctn;
    private String drctnKor;
    private int spd;
    private int hpa;
    private double lat;
    private double lng;
    private String maxWindSpd;
    private String radius15;




    public void setImg(String img)
    {
        this.img = img;
    }

    public String getImg()
    {
        return img;
    }

    public void setDate(String date)
    {
        this.date = date;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        try
        {
            dateTime = Calendar.getInstance();
            this.dateTime.setTime(sdf.parse(date));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public String getDateStr()
    {
        SimpleDateFormat df   = new SimpleDateFormat("dd일 HH시", Locale.KOREAN);
        return df.format(dateTime.getTime());
    }

    public void setTypnNo(int typnNo)
    {
        this.typnNo = typnNo;
    }

    public int getTypnNo() { return this.typnNo;}
    public void setTypnNm(String typnNm) {this.typnNm=typnNm;}
    public String getTypnNm() {return typnNm;}
    public void setTypnEngNm(String typnEngNm) {this.typnEngNm = typnEngNm;}
    public String getTypnEngNm() {return typnEngNm;}
    public void setLocaDesc(String locaDesc) {this.locaDesc = locaDesc;}
    public String getLocaDesc() {return locaDesc;}

    public void setDrctn(String drctn) {this.drctn = drctn;}
    public String getDrctn() {return drctn;}

    public void setDrctnKor(String drctnKor) {this.drctnKor=drctnKor;}
    public String getDrctnKor() {return drctnKor;}

    public void setSpd(int spd) {this.spd=spd;}
    public int getSpd() {return spd;}
    public void setHpa(int hpa) { this.hpa = hpa;}
    public int getHpa() {return hpa;}
    public void setMaxWindSpd(String maxWindSpd) {this.maxWindSpd = maxWindSpd;}
    public String getMaxWindSpd() {return maxWindSpd;}

    public void setLat(double lat) {this.lat = lat;}
    public double getLat() { return  lat;}
    public void setLng(double lng) {this.lng = lng;}
    public double getLng() {return lng;}

    public String getRadius15() {
        return radius15;
    }

    public void setRadius15(String radius15) {
        this.radius15 = radius15;
    }
}

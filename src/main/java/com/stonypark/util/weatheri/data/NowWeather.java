package com.stonypark.util.weatheri.data;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by soku on 2015-11-24.
 */
public class NowWeather {
    private double Lat;
    private double Lng;
    private String Region;
    private String Larea = "";
    private String Marea = "";
    private String Sarea = "";
    private int Humidity;
    private String City;
    private String Time;
    private int Weather;
    private String WeatherStr;
    private double Tempature;
    private int Rainpercent;
    private double WindSpeed;
    private double YesterDayTempature;
    private String Sunset;
    private String Sunrise;
    private int PM10;
    private int Stress;
    private String WindDirection;
    private String RainAmount;
    private Calendar TimeDate;
    private String isCurrent = "N";
    private double sTmpr;
    private String feelTendency;
    private String pm10Img;
    private int pm10day0;
    private int pm10day1;

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (!(o instanceof NowWeather)) {
            return false;
        }
        return ((NowWeather) o).getRegion().equals(this.getRegion());
    }

    public void setLat(double Lat)
    {
        this.Lat = Lat;
    }

    public Double getLat()
    {
        return Lat;
    }

    public void setLng(Double Lng)
    {
        this.Lng = Lng;
    }

    public Double getLng()
    {
        return Lng;
    }

    public void setRegion(String Region)
    {
        this.Region = Region;
    }

    public String getRegion()
    {
        return Region;
    }

    public void setLarea(String Larea)
    {
        this.Larea = Larea;
    }

    public  String getLarea()
    {
        return Larea;
    }

    public void setMarea(String Marea)
    {
        this.Marea = Marea;
    }

    public String getMarea()
    {
        return Marea;
    }

    public void setSarea(String Sarea)
    {
        this.Sarea = Sarea;
    }

    public String getSarea()
    {
        return Sarea;
    }

    public void setHumidity(int Humidity)
    {
        this.Humidity = Humidity;
    }

    public int getHumidity()
    {
        return Humidity;
    }

    public  void setCity(String City)
    {
        this.City = City;
    }

    public String getCity()
    {
        return City;
    }

    public void setTime(String Time)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        this.Time = Time;
        try
        {
            Log.d("info", Time);
            TimeDate = Calendar.getInstance();
            this.TimeDate.setTime(sdf.parse(Time));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public String getTime()
    {
        return Time;
    }

    public void setWeather(int Weather)
    {
        this.Weather = Weather;
    }

    public int getWeather()
    {
        return Weather;
    }

    public  void setWeatherStr(String WeatherStr)
    {
        this.WeatherStr = WeatherStr;
    }

    public String getWeatherStr()
    {
        if ("흐려져 눈".equals(WeatherStr)) {
            return "흐려져 눈비";
        } else {
            return WeatherStr;
        }
    }

    public void setTempature(double Tempature)
    {
        this.Tempature = Tempature;
    }

    public double getTempature()
    {
        return this.Tempature;
    }

    public void setRainpercent(int Rainpercent)
    {
        this.Rainpercent = Rainpercent;
    }

    public int getRainpercent()
    {
        return Rainpercent;
    }

    public void setWindSpeed(double WindSpeed)
    {
        this.WindSpeed = WindSpeed;
    }

    public double getWindSpeed()
    {
        return WindSpeed;
    }

    public void setYesterDayTempature(double YesterDayTempature)
    {
        this.YesterDayTempature = YesterDayTempature;
    }

    public double getYesterDayTempature()
    {
        return YesterDayTempature;
    }

    public void setSunset(String Sunset)
    {
        this.Sunset = Sunset;
    }

    public String getSunset()
    {
        return Sunset;
    }

    public void setSunrise(String Sunrise)
    {
        this.Sunrise = Sunrise;
    }

    public String getSunrise()
    {
        return Sunrise;
    }

    public void setPM10(int PM10)
    {
        this.PM10 = PM10;
    }

    public int getPM10()
    {
        return PM10;
    }

    public void setStress(int Stress)
    {
        this.Stress = Stress;
    }

    public int getStress()
    {
        return Stress;
    }

    public void setWindDirection(String WindDrection)
    {
        this.WindDirection = WindDrection;
    }

    public String getWindDirection()
    {
        return WindDirection;
    }

    public void setRainAmount(String RainAmount)
    {
        this.RainAmount = RainAmount;
    }

    public String getRainAmount()
    {
        return RainAmount;
    }

    public String getDayOfMonth() {
        SimpleDateFormat df = new SimpleDateFormat("dd일(E)", Locale.KOREAN);
        return df.format(TimeDate.getTime());
    }

    public String getTimeStr()
    {
        SimpleDateFormat df   = new SimpleDateFormat("yyyy년 MM월 dd일 (E)", Locale.KOREAN);
        if (TimeDate != null) {
            return df.format(TimeDate.getTime());
        } else {
            return "";
        }
    }

    public String getPublishStr()
    {
        SimpleDateFormat df   = new SimpleDateFormat("yyyy년 MM월 dd일 (E) HH:mm 발표", Locale.KOREAN);
        if (TimeDate != null) {
            return df.format(TimeDate.getTime());
        } else {
            return "";
        }
    }

    public void setIsCurrent(String isCurrent)
    {
        this.isCurrent = isCurrent;
    }

    public String isCurrent()
    {
        return isCurrent;
    }

    public void setsTmpr(double sTmpr)
    {
        this.sTmpr = sTmpr;
    }

    public double getsTmpr() { return sTmpr;}

    public String getFeelTendency() {
        return feelTendency;
    }

    public void setFeelTendency(String feelTendency) {
        this.feelTendency = feelTendency;
    }

    public String getPm10Img() {
        return pm10Img;
    }

    public void setPm10Img(String pm10Img) {
        this.pm10Img = pm10Img;
    }

    public int getPm10day0() {
        return pm10day0;
    }

    public void setPm10day0(int pm10day0) {
        this.pm10day0 = pm10day0;
    }

    public int getPm10day1() {
        return pm10day1;
    }

    public void setPm10day1(int pm10day1) {
        this.pm10day1 = pm10day1;
    }
}

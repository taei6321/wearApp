package com.stonypark.util.weatheri.data;

/**
 * Created by soku on 2015-11-24.
 */
public class TimeWeather {
    private String TimeDay;
    private String TimeHour;
    private int Weather;
    private String WeatherStr;
    private double Tempature;
    private int Rainpercent;
    private int Humidity;
    private double RainAmount;
    private double WindSpeed;



    public void setTimeDay(String TimeDay)
    {
        this.TimeDay = TimeDay;
    }

    public String getTimeDay()
    {
        return TimeDay;
    }

    public void setTimeHour(String TimeHour)
    {
        this.TimeHour = TimeHour;
    }

    public void setHumidity(int Humidity)
    {
        this.Humidity = Humidity;
    }

    public int getHumidity()
    {
        return Humidity;
    }

    public String getTimeHour()
    {
        return TimeHour;
    }

    public void setWeather(int Weather)
    {
        this.Weather = Weather;
    }

    public int getWeather()
    {
        return Weather;
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

    public void setRainAmount(double RainAmount)
    {
        this.RainAmount = RainAmount;
    }

    public double getRainAmount()
    {
        return RainAmount;
    }

    public void setWeatherStr(String WeatherStr)
    {
        this.WeatherStr = WeatherStr;
    }

    public String getWeatherStr()
    {
        return WeatherStr;
    }

    public void setWindSpeed(double WindSpeed)
    {
        this.WindSpeed = WindSpeed;
    }

    public double getWindSpeed()
    {
        return WindSpeed;
    }
}

package com.stonypark.util.weatheri.data;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by soku on 2015-11-17.
 */
public class DateWeatherItem {
    private String date;
    private Double top;
    private Double low;
    private Double windSpeed;
    private Double RainAmount;
    private int Weather;
    private String WeatherStr;
    private int MinHumidity;
    private int AvgHumidity;
    private Calendar TimeDate;
    private int prep;

    public void setDate(String date)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        this.date = date;
        try
        {
            TimeDate = Calendar.getInstance();
            this.TimeDate.setTime(sdf.parse(date));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        this.date = date;
    }

    public String getDateStr()
    {
        SimpleDateFormat df   = new SimpleDateFormat("MM월 dd일(E)", Locale.KOREAN);
        return df.format(TimeDate.getTime());
    }

    public String getDate()
    {
        return date;
    }

    public void setTop(Double top)
    {
        this.top = top;
    }

    public Double getTop()
    {
        return top;
    }

    public void setLow(Double low)
    {
        this.low = low;
    }

    public Double getLow()
    {
        return low;
    }

    public void setRainAmount(Double RainAmount)
    {
        this.RainAmount = RainAmount;
    }

    public Double getRainAmount()
    {
        return RainAmount;
    }

    public void setMinHumidity(int MinHumidity)
    {
        this.MinHumidity = MinHumidity;
    }

    public int getMinHumidity()
    {
        return MinHumidity;
    }

    public void setAvgHumidity(int AvgHumidity)
    {
        this.AvgHumidity = AvgHumidity;
    }

    public int getAvgHumidity()
    {
        return AvgHumidity;
    }

    public void setWindSpeed(Double windSpeed)
    {
        this.windSpeed = windSpeed;
    }

    public Double getWindSpeed()
    {
        return windSpeed;
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
        return WeatherStr;
    }

    public  void setPrep(int prep)
    {
        this.prep = prep;
    }

    public  int getPrep()
    {
        return prep;
    }

    public Calendar getTimeDate() {
        return TimeDate;
    }

    public void setTimeDate(Calendar timeDate) {
        TimeDate = timeDate;
    }
}

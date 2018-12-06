package com.stonypark.util.weatheri.data;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by soku on 2015-12-14.
 */
public class WxClass {
    private String date;
    private String img;
    private Calendar TimeDate;

    public void setDate(String date)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
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
        SimpleDateFormat df   = new SimpleDateFormat("MM/dd(E)", Locale.KOREAN);
        return df.format(TimeDate.getTime());
    }

    public void setImg(String img)
    {
        this.img = img;
    }

    public String getImg()
    {
        return img;
    }
}

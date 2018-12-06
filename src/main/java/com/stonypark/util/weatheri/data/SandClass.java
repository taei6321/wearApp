package com.stonypark.util.weatheri.data;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by soku on 2015-12-14.
 */
public class SandClass {
    private String img;
    private String date;
    private Calendar dateTime;

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

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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

    public Calendar getDateTime() {
        return this.dateTime;
    }

    public String getDateStr()
    {
        SimpleDateFormat df   = new SimpleDateFormat("yyyy년 MM월 dd일(E)", Locale.KOREAN);
        return df.format(dateTime.getTime());
    }
}

package com.stonypark.util.weatheri.data;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by soku on 2015-12-14.
 */
public class WarnInfoClass {
    private String img;
    private String date;
    private String title;
    private String area;
    private String valid_time;
    private String contents;
    private String status_time;
    private String status;
    private String prewarn;
    private String other;

    private Calendar dateTime;
    private Calendar statusTime;


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

    public String getDateStr()
    {
        SimpleDateFormat df   = new SimpleDateFormat("yyyy년 MM월 dd일(E)", Locale.KOREAN);
        return df.format(dateTime.getTime());
    }


    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getTitle()
    {return title;}

    public void setArea(String area)
    {
        this.area = area;
    }

    public String getArea() { return area;}
    public void setValid_time(String valid_time)
    {
        this.valid_time = valid_time;
    }

    public String getValid_time() {return valid_time;}

    public void setContents(String contents) {this.contents = contents;}
    public String getContents() {return  contents;}

    public void setStatus_time(String status_time) {
        this.status_time = status_time;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try
        {
            statusTime = Calendar.getInstance();
            this.statusTime.setTime(sdf.parse(status_time));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public String getStatusTimeStr()
    {
        SimpleDateFormat df   = new SimpleDateFormat("yyyy년 MM월 dd일(E)", Locale.KOREAN);
        return df.format(statusTime.getTime());
    }

    public void setStatus(String status) {this.status = status;}
    public String getStatus() { return status;}
    public void setPrewarn(String prewarn) {this.prewarn = prewarn;}
    public String getPrewarn() { return prewarn;}
    public void setOther(String other) {this.other = other;}
    public String getOther() {return  other;}

    public Calendar getDateTime() {
        return this.dateTime;
    }
}

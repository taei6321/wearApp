package com.stonypark.util.weatheri.data;

/**
 * Created by soku on 2016-03-25.
 */
public class AlertItem
{
    public String area1;
    public String area2;
    public String area3;
    public double lat;
    public double lng;
    public String code;
    public String displayName;

    public AlertItem()
    {
        ;
    }

    public AlertItem(String code, String area1, String area2, String area3, double lat, double lng)
    {
        this.code = code;
        this.area1 = area1;
        this.area2 = area2;
        this.area3 = area3;
        this.lat = lat;
        this.lng = lng;
    }

}

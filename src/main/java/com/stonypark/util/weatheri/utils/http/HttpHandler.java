package com.stonypark.util.weatheri.utils.http;

import com.stonypark.util.weatheri.db.WeatherDBHandler;
import com.stonypark.util.weatheri.utils.WeatherXMLParser;

/**
 * Created by soku on 2015-12-09.
 */
public class HttpHandler {
    private WeatherXMLParser xmlparse;
    private WeatherDBHandler db;

    public HttpHandler(WeatherDBHandler db)
    {
        xmlparse = new WeatherXMLParser();
        this.db = db;
    }


}

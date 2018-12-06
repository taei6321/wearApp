package com.stonypark.util.weatheri.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.stonypark.util.weatheri.data.AlertItem;
import com.stonypark.util.weatheri.data.CityItem;
import com.stonypark.util.weatheri.data.DateWeatherItem;
import com.stonypark.util.weatheri.data.NowWeather;
import com.stonypark.util.weatheri.data.TimeWeather;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by soku on 2015-11-30.
 */
public class WeatherDBHandler {

    private static SQLiteDatabase db;
    private static WeatherDBHelper INSTANCE;

    public WeatherDBHandler(Context context)
    {
        if (INSTANCE == null) {
            INSTANCE = new WeatherDBHelper(context);
            db = INSTANCE.getWritableDatabase();
        }
    }

    public void setCityNow(NowWeather now)
    {
//        db.execSQL("DELETE FROM weather_now_tab");
        ContentValues values = getCityNowCommonValues(now);

        if (isExistsRegion(now.getRegion()))
        {
            if (now.isCurrent().toUpperCase().equals("Y")) {
                values.put("region", now.getRegion());
                values.put("lat", now.getLat());
                values.put("lng", now.getLng());
                values.put("larea", now.getLarea());
                values.put("marea", now.getMarea());
                values.put("sarea", now.getSarea());

                db.update("weather_now_tab", values, "isCurrent=?", new String[]{"Y"});
            }
            else
                values.put("region", now.getRegion());
                values.put("lat", now.getLat());
                values.put("lng", now.getLng());
                values.put("larea", now.getLarea());
                values.put("marea", now.getMarea());
                values.put("sarea", now.getSarea());

                db.update("weather_now_tab", values, "region=?", new String[]{now.getRegion()});
        }
        else
        {
            values.put("isCurrent", now.isCurrent());
            values.put("region", now.getRegion());
            values.put("lat", now.getLat());
            values.put("lng", now.getLng());
            values.put("larea", now.getLarea());
            values.put("marea", now.getMarea());
            values.put("sarea", now.getSarea());

            db.insert("weather_now_tab", null, values);

        }

        Log.d("info", "DBISCURR:"+now.isCurrent());
    }

    public void setCurrentLocation(NowWeather now) {
        ContentValues values = getCityNowCommonValues(now);

        db.delete("weather_now_tab", "isCurrent=?", new String[]{"Y"});

        values.put("lat", now.getLat());
        values.put("lng", now.getLng());
        values.put("larea", now.getLarea());
        values.put("marea", now.getMarea());
        values.put("sarea", now.getSarea());
        values.put("region", now.getRegion());

        if (isExistsRegion(now.getRegion())) {
            db.update("weather_now_tab", values, "region=?", new String[]{now.getRegion()});
        }

        values.put("isCurrent", "Y");
        db.insert("weather_now_tab", null, values);
    }

    private ContentValues getCityNowCommonValues(NowWeather now) {
        ContentValues values = new ContentValues();

        values.put("publishDate", now.getTime());
        values.put("weather", now.getWeather());
        values.put("weatherStr", now.getWeatherStr());
        values.put("tempature", now.getTempature());
        values.put("windSpeed", now.getWindSpeed());
        values.put("windDirection", now.getWindDirection());
        values.put("rainAmount", now.getRainAmount());
        values.put("stress", now.getStress());
        values.put("pm10", now.getPM10());
        values.put("yesterdayTempature", now.getYesterDayTempature());
        values.put("humidity", now.getHumidity());
        values.put("sunrise", now.getSunrise());
        values.put("sunset", now.getSunset());
        values.put("sTmpr", now.getsTmpr());
        values.put("feel", now.getFeelTendency());

        return values;
    }

    public void setCityBookmark(NowWeather now) {
        ContentValues values = new ContentValues();

        /*
        region INTEGER,  lat REAL, lng REAL, larea  TEXT, marea TEXT, sarea TEXT, publishDate TEXT, weather INTEGER, " +
                "weatherStr TEXT, tempature REAL, windSpeed INTEGER, windDirection TEXT, rainAmount INTEGER, stress INTEGER, pm10 INTEGER, yesterdayTempature REAL, humidity INTEGER, sunrise TEXT, sunset TEXT
         */

        if (getBookMark().contains(now)) {
            values.put("isCurrent", now.isCurrent());
            values.put("bookmark", 1);

            db.update("weather_now_tab", values, "region=?", new String[]{now.getRegion()});
        } else {
            values.put("publishDate", now.getTime());
            values.put("weather", now.getWeather());
            values.put("weatherStr", now.getWeatherStr());
            values.put("tempature", now.getTempature());
            values.put("windSpeed", now.getWindSpeed());
            values.put("windDirection", now.getWindDirection());
            values.put("rainAmount", now.getRainAmount());
            values.put("stress", now.getStress());
            values.put("pm10", now.getPM10());
            values.put("yesterdayTempature", now.getYesterDayTempature());
            values.put("humidity", now.getHumidity());
            values.put("sunrise", now.getSunrise());
            values.put("sunset", now.getSunset());
            values.put("isCurrent", now.isCurrent());
            values.put("sTmpr", now.getsTmpr());
            values.put("region", now.getRegion());
            values.put("lat", now.getLat());
            values.put("lng", now.getLng());
            values.put("larea", now.getLarea());
            values.put("marea", now.getMarea());
            values.put("sarea", now.getSarea());
            values.put("feel", now.getFeelTendency());
            values.put("bookmark", 1);

            db.insert("weather_now_tab", null, values);
        }
    }

    public void delCityBookmark(String Region)
    {
        db.delete("weather_now_tab", "region=?", new String[]{Region});
    }

    public void setCityThreeTime(List<TimeWeather> list, String Region)
    {
        clearCityThreeTime(Region);
        /*
        create table weather_three_tab (region INTEGER,  weatherDate TEXT, weatherHour TEXT, weather INTEGER, " +
                "weatherStr TEXT, tempature REAL, windSpeed INTEGER, rainAmount INTEGER, );");
         */

        db.beginTransaction();
        for (int i=0; i < list.size(); i++)
        {
            TimeWeather item = list.get(i);

            ContentValues values = new ContentValues();

            values.put("region", Region);
            values.put("weatherDate", item.getTimeDay());
            values.put("weatherHour", item.getTimeHour());
            values.put("weather", item.getWeather());
            values.put("weatherStr", item.getWeatherStr());
            values.put("tempature", item.getTempature());
            values.put("windSpeed", item.getWindSpeed());
            values.put("rainAmount", item.getRainAmount());
            values.put("humidity", item.getHumidity());
            values.put("prep", item.getRainpercent());

            db.insert("weather_three_tab", null, values);

        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public void setCityDayTime(List<DateWeatherItem> list, String Region)
    {
        clearCityDayTime(Region);
        /*
        db.execSQL("create table weather_day_tab (region INTEGER,  weatherDate TEXT, weather INTEGER, " +
                "weatherStr TEXT, minTempature REAL, maxTempature REAL, windSpeed REAL, rainAmount REAL, minHumidity INTEGER, avgHumidity INTEGER );");
         */

        db.beginTransaction();
        for (int i=0; i < list.size(); i++)
        {
            DateWeatherItem item = list.get(i);

            ContentValues values = new ContentValues();

            values.put("region", Region);
            values.put("weatherDate", item.getDate());
            values.put("weather", item.getWeather());
            values.put("weatherStr", item.getWeatherStr());
            values.put("minTempature", item.getLow());
            values.put("maxTempature", item.getTop());
            values.put("windSpeed", item.getWindSpeed());
            values.put("rainAmount", item.getRainAmount());
            values.put("minHumidity", item.getMinHumidity());
            values.put("avgHumidity", item.getAvgHumidity());
            values.put("prep", item.getPrep());

            db.insert("weather_day_tab", null, values);

        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public ArrayList<CityItem> getLocal()
    {
        ArrayList<CityItem> list = new ArrayList<>();

        Cursor cursor = db.rawQuery("select region, name1, name2 from city_tab order by oversea asc, name1 asc", null);

        while (cursor.moveToNext())
        {
            CityItem city = new CityItem();

            city.setRegion(cursor.getString(0));
            city.setName1(cursor.getString(1));
            city.setName2(cursor.getString(2));


            list.add(city);
        }

        cursor.close();

        return list;
    }

    public ArrayList<CityItem> getLocal(String filter)
    {
        ArrayList<CityItem> list = new ArrayList<>();

        String sql = "select region, name1, name2 from city_tab ";

        if (filter.trim().length() > 0) {
            sql += "where  name1 like'%" + filter + "%' ";
        }

        sql += " order by oversea asc, name1 asc";

        Cursor cursor = db.rawQuery(sql, null);




        while (cursor.moveToNext())
        {
            CityItem city = new CityItem();

            city.setRegion(cursor.getString(0));
            city.setName1(cursor.getString(1));
            city.setName2(cursor.getString(2));


            list.add(city);
        }

        cursor.close();

        return list;
    }

    public ArrayList<NowWeather> getCitiesWeather(boolean includeCurrent)
    {
        ArrayList<NowWeather> list = new ArrayList<>();

        Cursor cursor = db.rawQuery("select idx, region, lat, lng, larea, marea, sarea, publishDate, weather, weatherStr, tempature, windSpeed, windDirection, rainAmount, stress, pm10, yesterdayTempature, " +
                "humidity, sunrise, sunset, isCurrent, sTmpr, feel from weather_now_tab order by isCurrent desc, idx asc ", null);

        while (cursor.moveToNext())
        {
            NowWeather now = new NowWeather();

            now.setRegion(cursor.getString(1));
            now.setLat(cursor.getDouble(2));
            now.setLng(cursor.getDouble(3));
            now.setLarea(cursor.getString(4));
            now.setMarea(cursor.getString(5));
            now.setSarea(cursor.getString(6));
            now.setTime(cursor.getString(7));
            now.setWeather(cursor.getInt(8));
            now.setWeatherStr(cursor.getString(9));
            now.setTempature(cursor.getDouble(10));
            now.setWindSpeed(cursor.getDouble(11));
            now.setWindDirection(cursor.getString(12));
            now.setRainAmount(cursor.getString(13));
            now.setStress(cursor.getInt(14));
            now.setPM10(cursor.getInt(15));
            now.setYesterDayTempature(cursor.getDouble(16));
            now.setHumidity(cursor.getInt(17));
            now.setSunrise(cursor.getString(18));
            now.setSunset(cursor.getString(19));
            now.setIsCurrent(cursor.getString(20));
            now.setsTmpr(cursor.getDouble(21));
            now.setFeelTendency(cursor.getString(22));

            if (!includeCurrent && "Y".equals(now.isCurrent())) {
                continue;
            }

            list.add(now);
        }

        cursor.close();

        return list;
    }

    public ArrayList<Long> getCities()
    {
        ArrayList<Long> list = new ArrayList<>();

        Cursor cursor = db.rawQuery("select region from weather_now_tab order by isCurrent desc, idx asc", new String[]{});

        while (cursor.moveToNext())
        {
            list.add(cursor.getLong(0));
        }

        cursor.close();

        return list;
    }





    public NowWeather getGPSNow()
    {
        NowWeather now = new NowWeather();

        Cursor cursor = db.rawQuery("select idx, region, lat, lng, larea, marea, sarea, publishDate, weather, weatherStr, tempature, windSpeed, windDirection, rainAmount, stress, pm10, yesterdayTempature, " +
                "humidity, sunrise, sunset, isCurrent, feel from weather_now_tab where isCurrent='Y'", new String[]{});

        if (cursor.moveToNext()) {
            now.setRegion(cursor.getString(1));
            now.setLat(cursor.getDouble(2));
            now.setLng(cursor.getDouble(3));
            now.setLarea(cursor.getString(4));
            now.setMarea(cursor.getString(5));
            now.setSarea(cursor.getString(6));
            now.setTime(cursor.getString(7));
            now.setWeather(cursor.getInt(8));
            now.setWeatherStr(cursor.getString(9));
            now.setTempature(cursor.getDouble(10));
            now.setWindSpeed(cursor.getDouble(11));
            now.setWindDirection(cursor.getString(12));
            now.setRainAmount(cursor.getString(13));
            now.setStress(cursor.getInt(14));
            now.setPM10(cursor.getInt(15));
            now.setYesterDayTempature(cursor.getDouble(16));
            now.setHumidity(cursor.getInt(17));
            now.setSunrise(cursor.getString(18));
            now.setSunset(cursor.getString(19));
            now.setIsCurrent(cursor.getString(20));
            now.setFeelTendency(cursor.getString(21));
        }

        cursor.close();

        return now;
    }

    public NowWeather getCityNow(String Region)
    {
        NowWeather now = new NowWeather();

        Cursor cursor = db.rawQuery("select idx, region, lat, lng, larea, marea, sarea, publishDate, weather, weatherStr, tempature, windSpeed, windDirection, rainAmount, stress, pm10, yesterdayTempature, " +
                "humidity, sunrise, sunset, isCurrent, sTmpr, feel from weather_now_tab where region=?", new String[]{Region});

        if (cursor.moveToNext()) {
            now.setRegion(cursor.getString(1));
            now.setLat(cursor.getDouble(2));
            now.setLng(cursor.getDouble(3));
            now.setLarea(cursor.getString(4));
            now.setMarea(cursor.getString(5));
            now.setSarea(cursor.getString(6));
            now.setTime(cursor.getString(7));
            now.setWeather(cursor.getInt(8));
            now.setWeatherStr(cursor.getString(9));
            now.setTempature(cursor.getDouble(10));
            now.setWindSpeed(cursor.getDouble(11));
            now.setWindDirection(cursor.getString(12));
            now.setRainAmount(cursor.getString(13));
            now.setStress(cursor.getInt(14));
            now.setPM10(cursor.getInt(15));
            now.setYesterDayTempature(cursor.getDouble(16));
            now.setHumidity(cursor.getInt(17));
            now.setSunrise(cursor.getString(18));
            now.setSunset(cursor.getString(19));
            now.setIsCurrent(cursor.getString(20));
            now.setsTmpr(cursor.getDouble(21));
            now.setFeelTendency(cursor.getString(22));
        }

        cursor.close();

        return now;
    }

    public ArrayList<TimeWeather> getCityThreeTime(String Region)
    {
        ArrayList<TimeWeather> list = new ArrayList<>();

        Cursor cursor = db.rawQuery("select weatherDate, weatherHour, weather, weatherStr, tempature, windspeed, rainAmount, humidity, prep from weather_three_tab where region=?",
                new String[]{Region});

        while(cursor.moveToNext())
        {
            TimeWeather t = new TimeWeather();

            t.setTimeDay(cursor.getString(0));
            t.setTimeHour(cursor.getString(1));
            t.setWeather(cursor.getInt(2));
            t.setWeatherStr(cursor.getString(3));
            t.setTempature(cursor.getDouble(4));
            t.setWindSpeed(cursor.getDouble(5));
            t.setRainAmount(cursor.getDouble(6));
            t.setHumidity(cursor.getInt(7));
            t.setRainpercent(cursor.getInt(8));

            list.add(t);
        }

        cursor.close();

        return list;
    }

    public ArrayList<String> getRainArea1()
    {
        ArrayList<String> area1 = new ArrayList<>();

        Cursor cursor = db.rawQuery("select distinct area1 from rainAlert_tab where area2=area1 ORDER BY area1 ASC", null);

        while(cursor.moveToNext())
        {
            area1.add(cursor.getString(0));
        }

        cursor.close();

        return area1;

    }

    public ArrayList<String> getRainArea2(String area1)
    {
        ArrayList<String> area2 = new ArrayList<>();

        Cursor cursor = db.rawQuery("select distinct area2 from rainAlert_tab where area1=? and area3='' ORDER BY area2 ASC", new String[]{area1});

        while(cursor.moveToNext())
        {
            area2.add(cursor.getString(0));
        }

        cursor.close();

        return area2;

    }

    public ArrayList<AlertItem> getRainArea3(String area1, String area2)
    {
        ArrayList<AlertItem> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("select region, area1, area2, area3, lat, lng from rainAlert_tab where area1=? and area2=?", new String[]{area1, area2});

        //Log.d("info", area1 + ":" + area2);

        while(cursor.moveToNext())
        {
            AlertItem item = new AlertItem();

            item.code = cursor.getString(0);
            item.area1 = cursor.getString(1);
            item.area2 = cursor.getString(2);
            item.area3 = cursor.getString(3);
            item.lat = cursor.getDouble(4);
            item.lng = cursor.getDouble(5);

            if ("".equals(item.area3)) {
                item.displayName = item.area2;
            } else {
                item.displayName = item.area3;
            }

            list.add(item);
        }

        cursor.close();

        Collections.sort(list, new Comparator<AlertItem>() {
            @Override
            public int compare(AlertItem lhs, AlertItem rhs) {
                return lhs.displayName.compareTo(rhs.displayName);
            }
        });

        return list;

    }

    public AlertItem getRainAreaByCode(String code) {
        Cursor cursor = db.rawQuery("select region, area1, area2, area3, lat, lng from rainAlert_tab where region=?",
                new String[]{code});
        AlertItem alertItem = null;

        while (cursor.moveToNext()) {
            AlertItem item = new AlertItem();

            item.code = cursor.getString(0);
            item.area1 = cursor.getString(1);
            item.area2 = cursor.getString(2);
            item.area3 = cursor.getString(3);
            item.lat = cursor.getDouble(4);
            item.lng = cursor.getDouble(5);

            if ("".equals(item.area3)) {
                item.displayName = item.area2;
            } else {
                item.displayName = item.area3;
            }

            alertItem = item;
        }

        cursor.close();

        return alertItem;
    }

    public ArrayList<String> getBreakArea1()
    {
        ArrayList<String> area1 = new ArrayList<>();

        Cursor cursor = db.rawQuery("select distinct area1 from breakAlert_tab ORDER BY area1", null);

        while(cursor.moveToNext())
        {
            area1.add(cursor.getString(0));
            Log.d("info", cursor.getString(0));
        }

        cursor.close();

        return area1;

    }

    public ArrayList<AlertItem> getBreakArea2(String area1)
    {
        ArrayList<AlertItem> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("select region, area1, area2, area3, lat, lng from breakAlert_tab where area1=?", new String[]{area1});

        //Log.d("info", area1 + ":" + area2);

        while(cursor.moveToNext())
        {
            AlertItem item = new AlertItem();

            item.code = cursor.getString(0);
            item.area1 = cursor.getString(1);
            item.area2 = cursor.getString(2);
            item.area3 = cursor.getString(3);
            item.lat = cursor.getDouble(4);
            item.lng = cursor.getDouble(5);

            if ("".equals(item.area3)) {
                item.displayName = item.area2;
            } else {
                item.displayName = item.area3;
            }

            list.add(item);
        }

        cursor.close();

        Collections.sort(list, new Comparator<AlertItem>() {
            @Override
            public int compare(AlertItem lhs, AlertItem rhs) {
                return lhs.displayName.compareTo(rhs.displayName);
            }
        });

        return list;

    }

    public ArrayList<DateWeatherItem> getCityDayTime(String Region)
    {
        ArrayList<DateWeatherItem> list = new ArrayList<>();
        /*
        db.execSQL("create table weather_day_tab (region INTEGER,  weatherDate TEXT, weather INTEGER, " +
                "weatherStr TEXT, minTempature REAL, maxTempature REAL, windSpeed REAL, rainAmount REAL, minHumidity INTEGER, avgHumidity INTEGER );");
         */

        Cursor cursor = db.rawQuery("select weatherDate, weather, weatherStr, minTempature, maxTempature, windSpeed, rainAmount, minHumidity, avgHumidity, prep from weather_day_tab where region = ?", new String[]{Region});

        while (cursor.moveToNext())
        {
            DateWeatherItem d= new DateWeatherItem();

            d.setDate(cursor.getString(0));
            d.setWeather(cursor.getInt(1));
            d.setWeatherStr(cursor.getString(2));
            d.setLow(cursor.getDouble(3));
            d.setTop(cursor.getDouble(4));
            d.setWindSpeed(cursor.getDouble(5));
            d.setRainAmount(cursor.getDouble(6));
            d.setMinHumidity(cursor.getInt(7));
            d.setAvgHumidity(cursor.getInt(8));
            d.setPrep(cursor.getInt(9));

            list.add(d);
        }

        cursor.close();

        return list;
    }

    // minTemp, maxTemp 가져올 때 사용
    public ArrayList<DateWeatherItem> getCityDayTime(String Region, String today)
    {
        ArrayList<DateWeatherItem> list = new ArrayList<>();
        /*
        db.execSQL("create table weather_day_tab (region INTEGER,  weatherDate TEXT, weather INTEGER, " +
                "weatherStr TEXT, minTempature REAL, maxTempature REAL, windSpeed REAL, rainAmount REAL, minHumidity INTEGER, avgHumidity INTEGER );");
         */

        Cursor cursor = db.rawQuery("select weatherDate, weather, weatherStr, minTempature, maxTempature, windSpeed, rainAmount, minHumidity, avgHumidity, prep from weather_day_tab where region = ? and weatherDate = ?", new String[]{Region, today});

        while (cursor.moveToNext())
        {
            DateWeatherItem d= new DateWeatherItem();

            d.setDate(cursor.getString(0));
            d.setWeather(cursor.getInt(1));
            d.setWeatherStr(cursor.getString(2));
            d.setLow(cursor.getDouble(3));
            d.setTop(cursor.getDouble(4));
            d.setWindSpeed(cursor.getDouble(5));
            d.setRainAmount(cursor.getDouble(6));
            d.setMinHumidity(cursor.getInt(7));
            d.setAvgHumidity(cursor.getInt(8));
            d.setPrep(cursor.getInt(9));

            list.add(d);
        }

        cursor.close();

        return list;
    }

    public void addMyRainAlert(AlertItem item)
    {
        if (isExistsMyRainAlert(item.code)) return;

        ContentValues values = new ContentValues();

        values.put("region", item.code);
        values.put("area1", item.area1);
        values.put("area2", item.area2);
        values.put("area3", item.area3);
        values.put("lat", item.lat);
        values.put("lng", item.lng);

        db.insert("myRainAlert_tab", null, values);
    }

    public void addMyBreakAlert(AlertItem item)
    {
        if (isExistsMyRainAlert(item.code)) return;

        ContentValues values = new ContentValues();

        values.put("region", item.code);
        values.put("area1", item.area1);
        values.put("area2", item.area2);
        values.put("area3", item.area3);
        values.put("lat", item.lat);
        values.put("lng", item.lng);

        db.insert("myBreakAlert_tab", null, values);
    }

    public void delMyRainAlert(String region)
    {
        db.delete("myRainAlert_tab", "region=?", new String[]{region});
    }

    public void delMyBreakAlert(String area2)
    {
        db.delete("myBreakAlert_tab", "area2=?", new String[]{area2});
    }

    public ArrayList<AlertItem> getMyRainAlert()
    {
        ArrayList<AlertItem> list = new ArrayList<>();

        Cursor cursor = db.rawQuery("select region, area1, area2, area3, lat, lng from myRainAlert_tab", null);

        while (cursor.moveToNext()) {
            AlertItem a = new AlertItem();

            a.code = cursor.getString(0);
            a.area1 = cursor.getString(1);
            a.area2 = cursor.getString(2);
            a.area3 = cursor.getString(3);
            a.lat = cursor.getDouble(4);
            a.lng = cursor.getDouble(5);

            list.add(a);
        }

        cursor.close();

        return list;
    }
    public ArrayList<NowWeather> getBookMark()
    {
        ArrayList<NowWeather> list = new ArrayList<>();

        Cursor cursor = db.rawQuery("select idx, region, lat, lng, larea, marea, sarea, publishDate, weather, weatherStr, tempature, windSpeed, windDirection, rainAmount, stress, pm10, yesterdayTempature, " +
                "humidity, sunrise, sunset, isCurrent, sTmpr, feel from weather_now_tab", null);

        while (cursor.moveToNext())
        {
            NowWeather now = new NowWeather();

            now.setRegion(cursor.getString(1));
            now.setLat(cursor.getDouble(2));
            now.setLng(cursor.getDouble(3));
            now.setLarea(cursor.getString(4));
            now.setMarea(cursor.getString(5));
            now.setSarea(cursor.getString(6));
            now.setTime(cursor.getString(7));
            now.setWeather(cursor.getInt(8));
            now.setWeatherStr(cursor.getString(9));
            now.setTempature(cursor.getDouble(10));
            now.setWindSpeed(cursor.getDouble(11));
            now.setWindDirection(cursor.getString(12));
            now.setRainAmount(cursor.getString(13));
            now.setStress(cursor.getInt(14));
            now.setPM10(cursor.getInt(15));
            now.setYesterDayTempature(cursor.getDouble(16));
            now.setHumidity(cursor.getInt(17));
            now.setSunrise(cursor.getString(18));
            now.setSunset(cursor.getString(19));
            now.setIsCurrent(cursor.getString(20));
            now.setsTmpr(cursor.getDouble(21));
            now.setFeelTendency(cursor.getString(22));

            Log.d("info", "BM:"+now.getLarea());

            list.add(now);
        }

        cursor.close();

        return list;
    }

    public ArrayList<AlertItem> getMyBreakAlert()
    {
        ArrayList<AlertItem> list = new ArrayList<>();

        Cursor cursor = db.rawQuery("select region, area1, area2, area3, lat, lng from myBreakAlert_tab", null);

        while (cursor.moveToNext()) {
            AlertItem a = new AlertItem();

            a.code = cursor.getString(0);
            a.area1 = cursor.getString(1);
            a.area2 = cursor.getString(2);
            a.area3 = cursor.getString(3);
            a.lat = cursor.getDouble(4);
            a.lng = cursor.getDouble(5);

            list.add(a);
        }

        cursor.close();

        return list;
    }

    public CityItem getCityItemByRegionId(String regionId) {
        CityItem cityItem = null;

        Cursor cursor = db.rawQuery("select region, name1, name2, oversea from city_tab where region=?",
                new String[]{String.valueOf(regionId)});

        if (cursor.moveToNext()) {
            cityItem = new CityItem();
            cityItem.setRegion(cursor.getString(0));
            cityItem.setName1(cursor.getString(1));
            cityItem.setName2(cursor.getString(2));
            cityItem.setOversea(cursor.getLong(3));
        }

        cursor.close();

        return cityItem;
    }

    private boolean isExistsMyRainAlert(String region)
    {
        boolean exists = false;
        Cursor cursor = db.rawQuery("select region from myRainAlert_tab where region = ?", new String[]{region});

        if (cursor.moveToNext())
            exists = true;
        cursor.close();

        return exists;
    }

    private boolean isExistsMyBreakAlert(String area2)
    {
        boolean exists = false;
        Cursor cursor = db.rawQuery("select area1 from myBreakAlert_tab where area2 = ?", new String[]{area2});

        if (cursor.moveToNext())
            exists = true;
        cursor.close();

        return exists;
    }


    private void clearCityDayTime(String Region)
    {
        db.delete("weather_day_tab", "region=?", new String[]{Region});
    }
    private void clearCityThreeTime(String Region)
    {
        db.delete("weather_three_tab", "region=?", new String[]{Region});
    }

    private boolean isExistsRegion (String region)
    {
        boolean exists = false;
        Cursor cursor = db.rawQuery("select region from weather_now_tab where region=?", new String[]{region});

        if (cursor.moveToNext())
            exists = true;

        cursor.close();
        return exists;
    }


    private int last_row_id()
    {
        int idx = -1;
        Cursor cursor = db.rawQuery("select last_insert_rowid()", new String[]{});

        if (cursor.moveToNext())
            idx = cursor.getInt(0);

        cursor.close();
        return idx;
    }

}

package com.stonypark.util.weatheri.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.stonypark.util.weatheri.R;
import com.stonypark.util.weatheri.data.AlertItem;
import com.stonypark.util.weatheri.data.NowWeather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by soku on 2015-11-30.
 */
public class WeatherDBHelper extends SQLiteOpenHelper {
    private String TAG = getClass().getName();

    private String NOW_TAB = "weather_now_tab";
    private String THREE_TAB = "weather_three_tab";
    private String DAY_TAB = "weather_day_tab";
    private String CITY_TAB = "city_tab";
    private String RAIN_TAB = "rainAlert_tab";
    private String BREAK_TAB = "breakAlert_tab";

    private static final int VERSION = 3;
    Context mContext;
    public WeatherDBHelper(Context context)
    {
        super(context, "weatheriWear.db", null, VERSION);

        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String Drop_SQL = "drop table if exists "+ NOW_TAB;
        try{
            db.execSQL(Drop_SQL);
        }catch (Exception e){
            Log.e(TAG, "Exception in Drop_SQL" +e);
        }
        db.execSQL("create table "+ NOW_TAB +" (idx INTEGER PRIMARY KEY AUTOINCREMENT, region TEXT,  lat REAL, lng REAL, larea  TEXT, marea TEXT, sarea TEXT, publishDate TEXT, weather INTEGER, " +
                "weatherStr TEXT, tempature REAL, windSpeed REAL, windDirection TEXT, rainAmount Text, stress INTEGER, pm10 INTEGER, yesterdayTempature REAL, humidity INTEGER, sunrise TEXT, sunset TEXT, isCurrent TEXT, sTmpr REAL, feel TEXT);");

        Drop_SQL = "drop table if exists "+ THREE_TAB;
        try{
            db.execSQL(Drop_SQL);
        }catch (Exception e){
            Log.e(TAG, "Exception in Drop_SQL " +e);
        }
        db.execSQL("create table "+ THREE_TAB +" (region TEXT,  weatherDate TEXT, weatherHour TEXT, weather INTEGER, " +
                "weatherStr TEXT, tempature REAL, windSpeed REAL, rainAmount REAL, humidity INTEGER, prep INTEGER);");

        Drop_SQL = "drop table if exists "+ DAY_TAB;
        try{
            db.execSQL(Drop_SQL);
        }catch (Exception e){
            Log.e(TAG, "Exception in Drop_SQL " +e);
        }
        db.execSQL("create table "+ DAY_TAB +" (region TEXT,  weatherDate TEXT, weather INTEGER, " +
                "weatherStr TEXT, minTempature REAL, maxTempature REAL, windSpeed REAL, rainAmount REAL, minHumidity INTEGER, avgHumidity INTEGER, prep INTEGER);");

        Drop_SQL = "drop table if exists "+ CITY_TAB;
        try{
            db.execSQL(Drop_SQL);
        }catch (Exception e){
            Log.e(TAG, "Exception in Drop_SQL " +e);
        }
        db.execSQL("create table "+ CITY_TAB +" (region TEXT,  name1 TEXT, name2 TEXT, oversea INTEGER );");

        Drop_SQL = "drop table if exists "+ RAIN_TAB;
        try{
            db.execSQL(Drop_SQL);
        }catch (Exception e){
            Log.e(TAG, "Exception in Drop_SQL " +e);
        }
        db.execSQL("create table "+ RAIN_TAB +" (region TEXT,  area1 TEXT, area2 TEXT, area3, lat REAL, lng REAL);");
//        db.execSQL("create table myRainAlert_tab (region TEXT,  area1 TEXT, area2 TEXT, area3, lat REAL, lng REAL);");

        Drop_SQL = "drop table if exists "+ BREAK_TAB;
        try{
            db.execSQL(Drop_SQL);
        }catch (Exception e){
            Log.e(TAG, "Exception in Drop_SQL " +e);
        }
        db.execSQL("create table "+ BREAK_TAB +" (region TEXT,  area1 TEXT, area2 TEXT, area3, lat REAL, lng REAL);");
//        db.execSQL("create table myBreakAlert_tab (region TEXT,  area1 TEXT, area2 TEXT, area3, lat REAL, lng REAL);");

        insertCities(db, "domestic_areas_new", 0);
        insertCities(db, "oversea_areas", 1);

        addRainAlert(db);
        addBreakAlert(db);

        onUpgrade(db, 1, VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        int upgradeTo = oldVersion + 1;

        db.execSQL("DELETE FROM " + NOW_TAB);
        while (upgradeTo <= newVersion) {
            switch (upgradeTo) {
                case 2:
                    db.execSQL("DELETE FROM "+ CITY_TAB +" WHERE oversea = 0");
                    List<ContentValues> csvValues = insertCities(db, "domestic_areas_new", 0);
                    List<NowWeather> bookmarked = getBookMark(db);

                    if (bookmarked.size() > 0) {
                        for (ContentValues values : csvValues) {
                            String region = values.getAsString("region");
                            NowWeather now = new NowWeather();
                            now.setRegion(region);
                            if (bookmarked.contains(now)) {
                                ContentValues newValues = new ContentValues();
                                newValues.put("larea", values.getAsString("name2"));
                                newValues.put("marea", values.getAsString("name1"));

                                db.update("weather_now_tab", newValues, "region=?", new String[]{region});
                            }
                        }
                    }
                    break;
                case 3:
                    db.execSQL("ALTER TABLE "+ NOW_TAB +" ADD COLUMN bookmark INTEGER DEFAULT 0");
                    db.execSQL("UPDATE "+ NOW_TAB +" SET bookmark = 1 WHERE isCurrent = 'N'");
            }
            upgradeTo++;
        }
    }

    private void addRainAlert(SQLiteDatabase db)
    {
        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset(R.raw.rain_alert));
            JSONArray m_jArry = obj.getJSONArray("rain");


            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject item = m_jArry.getJSONObject(i);

                String area1 = item.getString("area1");
                String area2 = item.getString("area2");
                String area3 = item.getString("area3");
                String code = item.getString("code");
                double lat = item.getDouble("lat");
                double lng = item.getDouble("lng");

                AlertItem aItem = new AlertItem(code, area1, area2, area3, lat, lng);

                insertRainAlert(db, aItem);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void addBreakAlert(SQLiteDatabase db)
    {
        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset(R.raw.break_alert));
            JSONArray m_jArry = obj.getJSONArray("break");


            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject item = m_jArry.getJSONObject(i);

                String area1 = item.getString("area1");
                String area2 = item.getString("area2");

                AlertItem aItem = new AlertItem("0", area1, area2, "", 0, 0);

                insertBreakAlert(db, aItem);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String loadJSONFromAsset(int resId) {
        String json = null;
        try {
            InputStream is = mContext.getResources().openRawResource(resId);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    private void insertRainAlert(SQLiteDatabase db, AlertItem item)
    {
        ContentValues values = new ContentValues();

        values.put("region", item.code);
        values.put("area1", item.area1);
        values.put("area2", item.area2);
        values.put("area3", item.area3);
        values.put("lat", item.lat);
        values.put("lng", item.lng);

        db.insert(RAIN_TAB, null, values);
    }

    private void insertBreakAlert(SQLiteDatabase db, AlertItem item)
    {
        ContentValues values = new ContentValues();

        values.put("region", item.code);
        values.put("area1", item.area1);
        values.put("area2", item.area2);
        values.put("area3", item.area3);
        values.put("lat", item.lat);
        values.put("lng", item.lng);

        db.insert(BREAK_TAB, null, values);
    }

    private List<ContentValues> insertCities(SQLiteDatabase db, String csvFileName, int overseaType) {
        List<ContentValues> contentValues = new ArrayList<>();
        InputStream inputStream = mContext.getResources().openRawResource(
                mContext.getResources().getIdentifier(csvFileName,
                        "raw", mContext.getPackageName()));

        BufferedReader reader =
                new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    String region = line.split(",")[0];
                    String name1 = line.split(",")[1];
                    String name2 = "";
                    if (line.split(",").length > 2) {
                        name2 = line.split(",")[2];
                    }

                    ContentValues values = new ContentValues();
                    values.put("region", region);
                    values.put("name1", name1);
                    values.put("name2", name2);
                    values.put("oversea", overseaType);
                    contentValues.add(values);

                    db.insert(CITY_TAB, null, values);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return contentValues;
    }

    private List<NowWeather> getBookMark(SQLiteDatabase db) {
        ArrayList<NowWeather> list = new ArrayList<>();

        Cursor cursor = db.rawQuery("select idx, region, lat, lng, larea, marea, sarea, publishDate, weather, weatherStr, tempature, windSpeed, windDirection, rainAmount, stress, pm10, yesterdayTempature, " +
                "humidity, sunrise, sunset, isCurrent, sTmpr, feel from "+ NOW_TAB +" where isCurrent='N' ", null);

        while (cursor.moveToNext()) {
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
            list.add(now);
        }

        cursor.close();

        return list;
    }
}

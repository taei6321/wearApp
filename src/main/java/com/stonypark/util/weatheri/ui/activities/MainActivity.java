package com.stonypark.util.weatheri.ui.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.widget.FrameLayout;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.stonypark.util.weatheri.data.DateWeatherItem;
import com.stonypark.util.weatheri.data.NowWeather;
import com.stonypark.util.weatheri.data.TimeWeather;
import com.stonypark.util.weatheri.db.WeatherDBHandler;
import com.stonypark.util.weatheri.db.WeatherDBHelper;
import com.stonypark.util.weatheri.ui.adapters.CustomPagerAdapter;
import com.stonypark.util.weatheri.utils.WeatherXMLParser;
import com.stonypark.util.weatheri.utils.http.HttpRequest;

import com.stonypark.util.weatheri.R;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import me.relex.circleindicator.CircleIndicator;

public class MainActivity extends WearableActivity {

    private String TAG = getClass().getName();

    private Context context;
    private WeatherDBHandler db;

    List<String> sList = new ArrayList<String>();

    // Local DB select
    ArrayList<NowWeather> now;
    ArrayList<DateWeatherItem> day;
    public static int index;

    // API
    HttpRequest client = new HttpRequest();
    WeatherXMLParser xmlparse = new WeatherXMLParser();

    // 오늘 날짜
    long time = System.currentTimeMillis();
    Date today = new Date(time);
    SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
    String sToday = df.format(today);

    // DB 날씨 정보
    public static String[] region;
    public static String[] location;
    public static String[] publishDate;
    public static Integer[] weather;
    public static Double[] temp;
    public static Double[] minTemp;
    public static Double[] maxTemp;

    // DB 서브 페이지 필요 정보
    public static Integer[] pm10;
    public static Integer[] stress;
    public static String[] windDirection;
    public static Double[] windSpeed;
    public static String[] sunrise;
    public static String[] sunset;

    // main xml viewPager indicator
    ViewPager viewPager;
    CustomPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IntentFilter messageFilter = new IntentFilter("message-forwarded-from-data-layer");
        MessageReceiver messageReceiver= new MessageReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, messageFilter);

        context = this;
        db = new WeatherDBHandler(context);
        xmlparse = new WeatherXMLParser();

        dataBring();

        for(int i=0; i<region.length; i++){
            updateCites(region[i]);
            updateThreeTime(region[i]);
            updateDayTime(region[i]);
            Log.d(TAG, "Update Data");
        }

        final FrameLayout mLayout = (FrameLayout) findViewById(R.id.mainLayout);

        // indicator
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        adapter = new CustomPagerAdapter(this);
        viewPager.setAdapter(adapter);

        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);

        indicator.setViewPager(viewPager);
        indicator.bringToFront();

        // Enables Always-on
        setAmbientEnabled();
    }

    public class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");
            sList.add(message);
            // Display message in UI
            dataBring();

            if(sList != null){
                deleteCites();
                for(int i=0; i<sList.size(); i++){
                    updateCites(sList.get(i));
                    updateThreeTime(sList.get(i));
                    updateDayTime(sList.get(i));
                }
            }else{
                for(int i=0; i<region.length; i++){
                    updateCites(region[i]);
                    updateThreeTime(region[i]);
                    updateDayTime(region[i]);
                }
            }
        }
    }

    // Bring data of db
    private void dataBring(){
        now = new ArrayList<NowWeather>();

        now = db.getBookMark();
        index = now.size();

        region = new String[index];
        location = new String[index];
        publishDate = new String[index];
        weather = new Integer[index];
        temp = new Double[index];
        minTemp = new Double[index];
        maxTemp = new Double[index];
        pm10 = new Integer[index];
        stress = new Integer[index];
        windDirection = new String[index];
        windSpeed = new Double[index];
        sunrise = new String[index];
        sunset = new String[index];

        int i=0;
        for(NowWeather n : now){
            region[i] = n.getRegion();
            location[i] = n.getMarea();
            publishDate[i] = n.getTime();
            weather[i] = n.getWeather();
            temp[i] = n.getTempature();
            pm10[i] = n.getPM10();
            stress[i] = n.getStress();
            windDirection[i] = n.getWindDirection();
            windSpeed[i] = n.getWindSpeed();
            sunrise[i] = n.getSunrise();
            sunset[i] = n.getSunset();

            i++;
        }

        day = new ArrayList<DateWeatherItem>();

        for(int j=0; j<now.size(); j++){
            day = db.getCityDayTime(region[j], sToday);
            for(DateWeatherItem d : day){

                minTemp[j] = d.getLow();
                maxTemp[j] = d.getTop();

            }
        }
    }

    private void deleteCites(){
        SQLiteDatabase sDB;
        WeatherDBHelper INSTANCE = new WeatherDBHelper(context);
        sDB = INSTANCE.getWritableDatabase();
        sDB.execSQL("DELETE FROM weather_now_tab");
        sDB.execSQL("DELETE FROM weather_three_tab");
        sDB.execSQL("DELETE FROM weather_day_tab");
    }

    private void deleteCites(String region){
        SQLiteDatabase sDB;
        WeatherDBHelper INSTANCE = new WeatherDBHelper(context);
        sDB = INSTANCE.getWritableDatabase();
        sDB.execSQL("DELETE FROM weather_now_tab where region = ?", new String[]{region});
        sDB.execSQL("DELETE FROM weather_three_tab where region = ?", new String[]{region});
        sDB.execSQL("DELETE FROM weather_day_tab where region = ?", new String[]{region});
    }

    private void updateCites(final String Region) {

        String url = String.format("http://jessie.weatheri.co.kr/wtiapp/data/now_%s.xml", Region);

        client.get(url, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String xmlBody = new String(responseBody);
                NowWeather nowWeather = xmlparse.getNowWeather(xmlBody);

                db.setCityNow(nowWeather);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody,
                                  Throwable error) {

            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
    }

    private void updateThreeTime(final String Region) {
        String url = String.format("http://jessie.weatheri.co.kr/wtiapp/data/fctthree_%s.xml", Region);

        client.get(url, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String xmlBody = new String(responseBody);
                ArrayList<TimeWeather> items = xmlparse.getTimeWeather(xmlBody);

                db.setCityThreeTime(items, Region);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody,
                                  Throwable error) {

            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
    }

    private void updateDayTime(final String Region) {
        String url = String.format("http://jessie.weatheri.co.kr/wtiapp/data/fctday_%s.xml", Region);

        Log.d(TAG, "updateDayTime: " + url);

        client.get(url, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String xmlBody = new String(responseBody);
                ArrayList<DateWeatherItem> items = xmlparse.getDateWeather(xmlBody);

                db.setCityDayTime(items, Region);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody,
                                  Throwable error) {

            }

            @Override
            public void onFinish() { super.onFinish(); }
        });
    }
}

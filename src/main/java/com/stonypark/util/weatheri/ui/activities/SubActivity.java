package com.stonypark.util.weatheri.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.stonypark.util.weatheri.R;
import com.stonypark.util.weatheri.data.DateWeatherItem;
import com.stonypark.util.weatheri.data.TimeWeather;
import com.stonypark.util.weatheri.db.WeatherDBHandler;
import com.stonypark.util.weatheri.ui.adapters.SubPagerAdapter;
import com.stonypark.util.weatheri.ui.view.Listviewitem;
import com.stonypark.util.weatheri.utils.WeatherXMLParser;
import com.stonypark.util.weatheri.utils.http.HttpRequest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import cz.msebera.android.httpclient.Header;

public class SubActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private String TAG = getClass().getName();

    public static final int TYPE_THREE = 1;
    public static final int TYPE_LINE = 2;
    public static final int TYPE_DAY = 3;

    // 새로고침
    private SwipeRefreshLayout mSwipeRefreshLayout;

    // DB 필요 내용
    private Context context;
    private WeatherDBHandler db;
    WeatherXMLParser xmlparse = new WeatherXMLParser();
    HttpRequest client = new HttpRequest();

    ListView listSub;

    // 해달 region
    String region;

    // DataBase 저장 ArrayList
    ArrayList<TimeWeather> timeWeathers = new ArrayList<>();
    ArrayList<DateWeatherItem> dateWeathers = new ArrayList<>();

    // ListView에 사용할 Adapter
    SubPagerAdapter adapter;

    // Adapter에 보낼 ArrayList
    ArrayList<Listviewitem> subList = new ArrayList<>();

    // 시간별 DB 변수
    String[] date = null;
    String[] hour = null;
    int[] weather = null;
    double[] temp = null;

    // 일별 DB 변수
    String[] dayDate = null;
    int[] dayWeather = null;
    Double[] minTemp = null;
    Double[] maxTemp = null;

    // 날짜 포맷
    SimpleDateFormat threeCom = new SimpleDateFormat("yyyyMMddHH");
    SimpleDateFormat fDf = new SimpleDateFormat("MM.dd");
    SimpleDateFormat uDf = new SimpleDateFormat("HH:mm");

    // 오늘 날짜
    long lToday = System.currentTimeMillis();
    Date today = new Date(lToday);
    Date update = new Date(lToday);

    // 업데이트 날짜
    String dayUp = uDf.format(update);
    TextView updateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        Intent intent = getIntent();
        region = intent.getExtras().getString("region");

        listSub = (ListView) findViewById(R.id.listSub);


        // 오늘 날짜
        long lToday = System.currentTimeMillis();
        String sToday = fDf.format(today);

        // 내일 날짜
        Calendar c = Calendar.getInstance();
        c.setTime(today);
        c.add(Calendar.DATE,1);
        String sTomorrow = fDf.format(c.getTime());


        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mSwipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        );

        context = this;
        db = new WeatherDBHandler(context);
        xmlparse = new WeatherXMLParser();

        threeData(region);
        dayData(region);

        // 머리, 중간, 끝
        View headV = getLayoutInflater().inflate(R.layout.divider, null, false);
        View footV = getLayoutInflater().inflate(R.layout.update_bottom, null, false);

        TextView divider = (TextView) headV.findViewById(R.id.divider);
        divider.setText("Today "+sToday);

        updateTime = (TextView) footV.findViewById(R.id.updateTime);
        updateTime.setText("업데이트 "+dayUp);

        listSub.addFooterView(footV);
        listSub.addHeaderView(headV);

        // 시간별
        for(int i=0; i<date.length; i++) {

            String dDay = date[i]+hour[i];
            Date sDate = new Date();
            long reqDate = 0;
            Date tDate = new Date();
            long curDate = 0;

            try {
                sDate = threeCom.parse(dDay);
                reqDate = sDate.getTime();
                tDate = threeCom.parse(threeCom.format(today));
                curDate = tDate.getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            long minute = (reqDate - curDate) / 60000;

            if(minute > 0 && minute <2100){
               Listviewitem lv = new Listviewitem(date[i], hour[i], weather[i], temp[i], TYPE_THREE);
               subList.add(lv);
           }
        }

        for(int i=0; i<1; i++){
            Listviewitem lv = new Listviewitem(TYPE_LINE);
            subList.add(lv);
        }


        // 일별
        for(int i=0; i<7; i++){
            Listviewitem lv = new Listviewitem(dayDate[i], dayWeather[i], minTemp[i], maxTemp[i], TYPE_DAY);
            subList.add(lv);
        }

        adapter = new SubPagerAdapter(this, R.layout.item, subList);

        listSub.setAdapter(adapter);
    }


    @Override
    public void onRefresh() {
        // 새로고침 코드 DB 업데이트 해서 현재 시간에 맞춰 다시 설비
        threeData(region);
        dayData(region);

        lToday = System.currentTimeMillis();
        update = new Date(lToday);
        dayUp = uDf.format(update);
        updateTime.setText("업데이트 "+dayUp);

        // 새로고침 완료
        mSwipeRefreshLayout.setRefreshing(false);
    }

    public void threeData(String region) {
        updateThreeTime(region);
        timeWeathers = db.getCityThreeTime(region);

        if(timeWeathers != null){
            int index = timeWeathers.size();

            date = new String[index];
            hour =  new String[index];
            weather =  new int[index];
            temp =  new double[index];

            int i=0;
            for(TimeWeather t : timeWeathers){
                date[i] = t.getTimeDay();
                hour[i] = t.getTimeHour();
                weather[i] = t.getWeather();
                temp[i] = t.getTempature();

                i++;
            }
        }
    }

    public void dayData(String region){
        updateDayTime(region);
        dateWeathers = db.getCityDayTime(region);

        if(dateWeathers != null){
            int index = dateWeathers.size();

            dayDate = new String[index];
            dayWeather = new int[index];
            minTemp = new  Double[index];
            maxTemp = new  Double[index];

            int i=0;
            for(DateWeatherItem d : dateWeathers){
                dayDate[i] = d.getDate();

                dayWeather[i] = d.getWeather();
                minTemp[i] = d.getLow();
                maxTemp[i] = d.getTop();

                i++;
            }
        }
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

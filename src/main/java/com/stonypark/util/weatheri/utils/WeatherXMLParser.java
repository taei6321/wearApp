package com.stonypark.util.weatheri.utils;

import android.util.Log;

import com.stonypark.util.weatheri.data.DateWeatherItem;
import com.stonypark.util.weatheri.data.EarthquakeClass;
import com.stonypark.util.weatheri.data.NowWeather;
import com.stonypark.util.weatheri.data.PM10Class;
import com.stonypark.util.weatheri.data.SandClass;
import com.stonypark.util.weatheri.data.SatelliteClass;
import com.stonypark.util.weatheri.data.TimeWeather;
import com.stonypark.util.weatheri.data.TyphoonClass;
import com.stonypark.util.weatheri.data.UVClass;
import com.stonypark.util.weatheri.data.WarnInfoClass;
import com.stonypark.util.weatheri.data.WxClass;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by soku on 2015-11-26.
 */
public class WeatherXMLParser {
    private String TAG = getClass().getName();
    private DocumentBuilderFactory factory;
    private DocumentBuilder parser;

    public WeatherXMLParser()
    {
        try {
            factory = DocumentBuilderFactory.newInstance();
            parser = factory.newDocumentBuilder();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public NowWeather getNowWeather(String xmlbody)
    {
        NowWeather now = new NowWeather();

        xmlbody = xmlbody.replace("\n<?xml", "<?xml");
        try
        {
            InputSource is = new InputSource(new StringReader(xmlbody));
            Document doc = parser.parse(is);

            Element elmt = (Element) doc.getElementsByTagName("AREA").item(0);
            if (!"".equals(getText(elmt, "townFcastRgnCd"))) {
                now.setRegion(getText(elmt, "townFcastRgnCd"));
            }
            if (!"".equals(getText(elmt, "lat"))) {
                now.setLat(Double.parseDouble(getText(elmt, "lat")));
            }
            if (!"".equals(getText(elmt, "lon"))) {
                now.setLng(Double.parseDouble(getText(elmt, "lon")));
            }
            now.setLarea(getText(elmt, "lareaNm"));
            now.setMarea(getText(elmt, "mareaNm"));
            now.setSarea(getText(elmt, "sareaNm"));
            now.setTime(getText(elmt, "fcastYmdt"));
            now.setWeatherStr(getText(elmt, "wetrTxt"));
            if (!"".equals(getText(elmt, "wetrCd"))) {
                now.setWeather(Integer.parseInt(getText(elmt, "wetrCd")));
            }
            if (!"".equals(getText(elmt, "tmpr"))) {
                now.setTempature(Float.parseFloat(getText(elmt, "tmpr")));
            }
            now.setWindDirection(getText(elmt, "windDrctn"));
            if (!"".equals(getText(elmt, "windSpd"))) {
                now.setWindSpeed(Double.parseDouble(getText(elmt, "windSpd")));
            }
            now.setRainAmount(getText(elmt, "onehourRainAmt"));
            if (!"".equals(getText(elmt, "disIdx"))) {
                now.setStress(Integer.parseInt(getText(elmt, "disIdx")));
            }
            now.setSunrise(getText(elmt, "sunRise"));
            now.setSunset(getText(elmt, "sunSet"));
            if (!"".equals(getText(elmt, "yTemp"))) {
                now.setYesterDayTempature(Double.parseDouble(getText(elmt, "yTemp")));
            }
            if (!"".equals(getText(elmt, "pm10"))) {
                now.setPM10(Integer.parseInt(getText(elmt, "pm10")));
            }
            if (!"".equals(getText(elmt, "humd"))) {
                now.setHumidity(Integer.parseInt(getText(elmt, "humd")));
            }
            if (!"".equals(getText(elmt, "sTmpr"))) {
                now.setsTmpr(Double.parseDouble(getText(elmt, "sTmpr")));
            }
            now.setFeelTendency(getText(elmt, "ment"));
            now.setPm10Img(getText(elmt, "pm10img"));
            if (!"".equals(getText(elmt, "pm10day0"))) {
                now.setPm10day0(Integer.parseInt(getText(elmt, "pm10day0")));
            }
            if (!"".equals(getText(elmt, "pm10day1"))) {
                now.setPm10day1(Integer.parseInt(getText(elmt, "pm10day1")));
            }
        }
        catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }

        return now;
    }

    public ArrayList<DateWeatherItem> getDateWeather(String xmlbody)
    {
        ArrayList<DateWeatherItem> list = new ArrayList<>();

        try {
            InputSource is = new InputSource(new StringReader(xmlbody));
            Document doc = parser.parse(is);

            NodeList items = doc.getElementsByTagName("dailyLandFcast");

            for (int i = 0; i < items.getLength(); i++) {
                DateWeatherItem item = new DateWeatherItem();

                Element elmt = (Element) items.item(i);

                item.setDate(getText(elmt, "aplYmd"));
                item.setLow(Double.parseDouble(getText(elmt, "minTmpr")));
                item.setTop(Double.parseDouble(getText(elmt, "maxTmpr")));
                item.setWeather(Integer.parseInt(getText(elmt, "wetrCd")));
                item.setWeatherStr(getText(elmt, "wetrTxt"));
                item.setWindSpeed(Double.parseDouble(getText(elmt, "windSpd")));
                item.setRainAmount(Double.parseDouble(getText(elmt, "rainAmt")));
                item.setMinHumidity(Integer.parseInt(getText(elmt, "minHumd")));
                item.setAvgHumidity(Integer.parseInt(getText(elmt, "avgHumd")));
                item.setPrep(Integer.parseInt(getText(elmt, "prep")));

                list.add(item);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return list;
    }

    public PM10Class getPM(String xmlbody)
    {
        PM10Class item = new PM10Class();
        try
        {
            InputSource is = new InputSource(new StringReader(xmlbody));
            Document doc = parser.parse(is);

            NodeList nodes = doc.getElementsByTagName("PM10");

            item.setImg(getText((Element)nodes.item(0), "img"));

            nodes = doc.getElementsByTagName("RECORD");

            if (nodes.getLength() > 0)
            {
                Element node = (Element)nodes.item(0);

                item.setDate(getText(node, "fcstYmdt"));
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return item;
    }

    public WarnInfoClass getWarn(String xmlbody)
    {
        WarnInfoClass warn = new WarnInfoClass();

        try
        {
            InputSource is = new InputSource(new StringReader(xmlbody));
            Document doc = parser.parse(is);

            NodeList nodes = doc.getElementsByTagName("warn");

            if (nodes.getLength() ==0) return null;

            Element node = (Element)nodes.item(0);

            warn.setImg(getText(node, "img"));

            nodes = doc.getElementsByTagName("region");

            if (nodes.getLength() ==0) return null;

            node = (Element)nodes.item(0);

            warn.setDate(getText(node, "TM_FC"));
            warn.setTitle(getText(node, "TITLE"));
            warn.setArea(getText(node, "AREA"));
            warn.setValid_time(getText(node, "VALID_TIME"));
            warn.setContents(getText(node, "CONTENTS"));
            warn.setStatus_time(getText(node, "STATUS_TIME"));
            warn.setStatus(getText(node, "STATUS"));
            warn.setPrewarn(getText(node, "PREWARN"));
            warn.setOther(getText(node, "OTHER"));

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return warn;
    }

    public ArrayList<TyphoonClass> getTyphoon(String xmlbody)
    {
        ArrayList<TyphoonClass> items = new ArrayList<>();

        try
        {
            InputSource is = new InputSource(new StringReader(xmlbody));
            Document doc = parser.parse(is);

            NodeList nodes = doc.getElementsByTagName("typn");

            for (int i=0; i < nodes.getLength(); i++)
            {
                Element node = (Element)nodes.item(i);

                TyphoonClass item = new TyphoonClass();

                item.setDate(getText(node, "locaYmdt"));
                item.setImg(getText(node, "typnImg"));
                item.setTypnNo(Integer.parseInt(getText(node, "typnNo")));
                item.setTypnNm(getText(node, "typnNm"));
                item.setTypnEngNm(getText(node, "typnEngNm"));
                item.setLocaDesc(getText(node, "locaDesc"));
                item.setDrctn(getText(node, "drctn"));
                item.setDrctnKor(getText(node, "drctnKor"));

                item.setSpd(Integer.parseInt(getText(node, "spd")));
                item.setHpa(Integer.parseInt(getText(node, "hpa")));

                item.setLat(Double.parseDouble(getText(node, "latude")));
                item.setLng(Double.parseDouble(getText(node, "lotude")));

                item.setMaxWindSpd(getText(node, "maxWindSpd"));
                item.setRadius15(getText(node, "radius15"));

                items.add(item);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return items;
    }

    public SandClass getSand(String xmlbody)
    {
        SandClass sand = new SandClass();

        try
        {
            InputSource is = new InputSource(new StringReader(xmlbody));
            Document doc = parser.parse(is);

            NodeList nodes = doc.getElementsByTagName("PM10");

            if (nodes.getLength() ==0) return null;

            Element node = (Element)nodes.item(0);

            sand.setImg(getText(node, "img"));

            nodes = doc.getElementsByTagName("RECORD");

            if (nodes.getLength() ==0) return null;

            node = (Element)nodes.item(0);

            sand.setDate(getText(node, "OBS_TIME"));


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return sand;
    }

    public ArrayList<WxClass> getWx(String xmlbody)
    {
        ArrayList<WxClass> items = new ArrayList<>();

        try
        {
            InputSource is = new InputSource(new StringReader(xmlbody));
            Document doc = parser.parse(is);

            NodeList nodes = doc.getElementsByTagName("img");

            for (int i=0; i < nodes.getLength(); i++)
            {
                Element node = (Element)nodes.item(i);

                WxClass item = new WxClass();

                item.setDate(getText(node, "time"));
                item.setImg(getText(node, "file"));

                items.add(item);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return items;
    }

    public ArrayList<SatelliteClass> getSatellite(String xmlbody)
    {
        ArrayList<SatelliteClass> items = new ArrayList<>();

        try
        {
            InputSource is = new InputSource(new StringReader(xmlbody));
            Document doc = parser.parse(is);

            NodeList nodes = doc.getElementsByTagName("img");

            for (int i=0; i < nodes.getLength(); i++)
            {
                Element node = (Element)nodes.item(i);

                SatelliteClass item = new SatelliteClass();

                item.setDate(getText(node, "time"));
                item.setImg(getText(node, "file"));

                items.add(item);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return items;
    }
    public UVClass getUV(String xmlbody)
    {
        UVClass uv = new UVClass();

        try
        {
            InputSource is = new InputSource(new StringReader(xmlbody));
            Document doc = parser.parse(is);

            NodeList nodes = doc.getElementsByTagName("uvNdxFcastReport");

            if (nodes.getLength() > 0)
            {

                Element node = (Element)nodes.item(0);

                uv.setDate(getText(node, "reportYmdt"));
                uv.setImg(getText(node, "img"));

            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return uv;
    }

    public ArrayList<TimeWeather> getTimeWeather(String xmlbody)
    {
        ArrayList<TimeWeather> list = new ArrayList<>();

        try {
            InputSource is = new InputSource(new StringReader(xmlbody));
            Document doc = parser.parse(is);

            NodeList threeHourFcasts = doc.getElementsByTagName("threeHourFcast");

            for (int i = 0; i < threeHourFcasts.getLength(); i++) {
                TimeWeather t = new TimeWeather();

                Element threeHourFcast = (Element) threeHourFcasts.item(i);

                t.setTimeHour(getText(threeHourFcast, "aplHour"));
                t.setWeather(Integer.parseInt(getText(threeHourFcast, "wetrCd")));
                t.setWindSpeed(Double.parseDouble(getText(threeHourFcast, "windSpd")));
                t.setTempature(Double.parseDouble(getText(threeHourFcast, "tmpr")));
                t.setWeatherStr(getText(threeHourFcast, "wetrTxt"));
                t.setTimeDay(getText(threeHourFcast, "aplYmd"));
                t.setRainAmount(Double.parseDouble(getText(threeHourFcast, "rainAmt")));
                t.setHumidity(Integer.parseInt(getText(threeHourFcast, "humd")));
                t.setRainpercent(Integer.parseInt(getText(threeHourFcast, "prep")));

                list.add(t);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return list;
    }

    public EarthquakeClass getEarthquakeClass(String xmlBody) {
        EarthquakeClass earthquakeClass = new EarthquakeClass();

        try {
            InputSource is = new InputSource(new StringReader(xmlBody));
            Document doc = parser.parse(is);

            NodeList nodes = doc.getElementsByTagName("EARTHQUAKE");

            if (nodes.getLength() > 0) {
                Element node = (Element)nodes.item(0);

                earthquakeClass.setTime(getText(node, "TM_EQK"));
                earthquakeClass.setCenter(getText(node, "CENTER"));
                earthquakeClass.setMt(getText(node, "MT"));
                earthquakeClass.setLoc(getText(node, "LOC"));
                earthquakeClass.setRem(getText(node, "REM"));
                earthquakeClass.setImg(getText(node, "img"));
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }

        return earthquakeClass;
    }

    private String getText(Element p, String tag)
    {
        String text = "";

        NodeList n = p.getElementsByTagName(tag);

        if (n.getLength() == 1)
        {
            text = n.item(0).getTextContent();
        }

        return text;
    }
}

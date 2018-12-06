package com.stonypark.util.weatheri.ui.adapters;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.stonypark.util.weatheri.R;
import com.stonypark.util.weatheri.ui.view.Listviewitem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SubPagerAdapter extends PagerAdapter implements ListAdapter {

    private String TAG = getClass().getName();

    public static final int TYPE_THREE = 1;
    public static final int TYPE_LINE = 2;
    public static final int TYPE_DAY = 3;

    private Context ctx;
    private LayoutInflater inflater;

    private LinearLayout backgroundImage;
    private int layout;

    private ArrayList<Listviewitem> listviewitem = new ArrayList<Listviewitem>();

    // 날짜 포맷
    SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
    SimpleDateFormat dayDF = new SimpleDateFormat("dd (E)");

    // 오늘 날짜
    long lToday = System.currentTimeMillis();
    Date today = new Date(lToday);

    public SubPagerAdapter(Context context){
        this.ctx = context;
    }
    public SubPagerAdapter(Context context, int layout, ArrayList<Listviewitem> listviewitem){
        this.ctx = context;
        this.layout = layout;
        this.listviewitem = listviewitem;
    }

    @Override
    public int getCount() {
        return listviewitem.size();
    }

    @Override
    public Object getItem(int i) {
        return listviewitem.size();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int position, View v, ViewGroup container) {
        inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        Log.d(TAG, "position : "+position);
        if(listviewitem.size() != position){
            if(listviewitem.get(position).getPosition() == TYPE_THREE){

                v = inflater.inflate(R.layout.item, container, false);
                try {
                    Date dayD = df.parse(listviewitem.get(position).getListDate());
                    String dayS = dayDF.format(dayD);
                    TextView listDate = v.findViewById(R.id.listDate);
                    listDate.setText(String.format("%s %s:00", dayS, listviewitem.get(position).getHour()));
                }catch (Exception e){
                    e.printStackTrace();
                }

                ImageView weatherImage = v.findViewById(R.id.listImage);
                TypedArray imgs = ctx.getResources().obtainTypedArray(R.array.w_icon_small);
                // get resource ID by index
                imgs.getResourceId(listviewitem.get(position).getWeather() - 1, -1);
                // or set you ImageView's resource to the id
                weatherImage.setImageResource(imgs.getResourceId(listviewitem.get(position).getWeather() - 1, -1));
                // recycle the array
                imgs.recycle();

                TextView listTemp = v.findViewById(R.id.listTemp);
                listTemp.setText(String.format("%.0f˚", listviewitem.get(position).getListTemp()));

            }else if(listviewitem.get(position).getPosition() == TYPE_LINE){
                v = inflater.inflate(R.layout.divider, container, false);

                TextView divider = v.findViewById(R.id.divider);
                divider.setText("Weekly");

            }else if(listviewitem.get(position).getPosition() == TYPE_DAY){

                v = inflater.inflate(R.layout.item, container, false);
                try {
                    Date dayD = df.parse(listviewitem.get(position).getListDate());
                    String dayS = dayDF.format(dayD);
                    TextView listDate = v.findViewById(R.id.listDate);
                    listDate.setText(dayS);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                ImageView weatherImage = v.findViewById(R.id.listImage);
                TypedArray imgs = ctx.getResources().obtainTypedArray(R.array.w_icon_small);
                // get resource ID by index
                imgs.getResourceId(listviewitem.get(position).getWeather() - 1, -1);
                // or set you ImageView's resource to the id
                weatherImage.setImageResource(imgs.getResourceId(listviewitem.get(position).getWeather() - 1, -1));
                // recycle the array
                imgs.recycle();

                TextView listTemp = v.findViewById(R.id.listTemp);
                listTemp.setText(String.format("%.0f˚/ %.0f˚", listviewitem.get(position).getMinTemp(), listviewitem.get(position).getMaxTemp()));

            }else{
                Log.e(TAG, "getView Error : NULL");
            }
        }

        return v;
    }

    @Override
    public int getItemViewType(int pos) {
        return (pos<listviewitem.size() ? 1 : 2);
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view == object);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.refreshDrawableState();
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int i) {
        return false;
    }
}

package com.stonypark.util.weatheri.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;
import com.stonypark.util.weatheri.R;
import com.stonypark.util.weatheri.ui.activities.MainActivity;
import com.stonypark.util.weatheri.ui.activities.SubActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class CustomPagerAdapter extends PagerAdapter implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private String TAG = getClass().getName();

    private String region;

    private Context ctx;

    private LayoutInflater inflater;

    private LinearLayout backgroundImage;

    // 도시추가 기능
    Node mNode; // the connected device to send the message to
    GoogleApiClient  mGoogleApiClient;
    private static final String HELLO_WORLD_WEAR_PATH = "/openApp";

    // 추가 성공시 진동

    public CustomPagerAdapter(Context context){
        this.ctx = context;
    }

    @Override
    public int getCount() {
        return MainActivity.index+1;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mGoogleApiClient = new GoogleApiClient.Builder(ctx)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        if(MainActivity.index != position) {
            region = MainActivity.region[position];

            View v = inflater.inflate(R.layout.basic_frag, container, false);

            ImageView locationImage = v.findViewById(R.id.locationImage);
            locationImage.setImageResource(R.drawable.point);

            TextView locationText = v.findViewById(R.id.locationText);
            locationText.setText(MainActivity.location[position]);

            TextView nowText = v.findViewById(R.id.nowText);
            long now = System.currentTimeMillis();
            Date today = new Date(now);
            SimpleDateFormat df = new SimpleDateFormat("MM.dd (E)");
            String sToday = df.format(today);
            nowText.setText(sToday);

            ImageView weatherImage = v.findViewById(R.id.weatherImage);
            TypedArray imgs = ctx.getResources().obtainTypedArray(R.array.w_icon_large);
            // get resource ID by index
            imgs.getResourceId(MainActivity.weather[position] - 1, -1);
            // or set you ImageView's resource to the id
            weatherImage.setImageResource(imgs.getResourceId(MainActivity.weather[position] - 1, -1));
            // recycle the array
            imgs.recycle();

            backgroundImage = (LinearLayout) v.findViewById(R.id.back);

            backgroundImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ctx, SubActivity.class);
                    intent.putExtra("region", MainActivity.region[position]);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    ctx.startActivity(intent);
                }
            });

            boolean is_night = false;
            Calendar cToday = GregorianCalendar.getInstance();
            if (cToday.get(Calendar.HOUR_OF_DAY) < 7 || cToday.get(Calendar.HOUR_OF_DAY) > 18) {
                is_night = true;
            }
            getBGId(MainActivity.weather[position], is_night);

            TextView weatherText = v.findViewById(R.id.weatherText);
            weatherText.setText(String.format("%.0f˚", MainActivity.temp[position]));

            TextView tempText = v.findViewById(R.id.tempText);
            tempText.setText(String.format("%.0f˚", MainActivity.minTemp[position]) + " / " + String.format("%.0f˚", MainActivity.maxTemp[position]));

            getPM10(MainActivity.pm10[position], v);

            stress(MainActivity.stress[position], v);

            wind(MainActivity.windSpeed[position], MainActivity.windDirection[position], v);

            getSun(MainActivity.sunrise[position], MainActivity.sunset[position], v);

            container.addView(v);
            return v;
        }else{
            View v = inflater.inflate(R.layout.city_add, container, false);

            ImageButton cityAdd = v.findViewById(R.id.cityAdd);
            cityAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sendDataLayerMessage();
                }
            });

            container.addView(v);
            return v;
        }
    }

    private void sendDataLayerMessage() {
        Log.d(TAG, "sendMessageToDevice");
        mGoogleApiClient.connect();
        if (mNode != null && mGoogleApiClient!=null && mGoogleApiClient.isConnected()) {
            Wearable.MessageApi.sendMessage(
                    mGoogleApiClient, mNode.getId(), HELLO_WORLD_WEAR_PATH, null).setResultCallback(

                    new ResultCallback<MessageApi.SendMessageResult>() {
                        @Override
                        public void onResult(MessageApi.SendMessageResult sendMessageResult) {

                            if (!sendMessageResult.getStatus().isSuccess()) {
                                Log.e(TAG, "Failed to send message with status code: "
                                        + sendMessageResult.getStatus().getStatusCode());
                            }else{
                                Log.e(TAG, "Success to send message");
                                Toast.makeText(ctx, "휴대폰에서\n도시를 추가해주세요.", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
            );
        }else{
            //Improve your code
        }
    }

    private void resolveNode() {

        Wearable.NodeApi.getConnectedNodes(mGoogleApiClient).setResultCallback(new ResultCallback<NodeApi.GetConnectedNodesResult>() {
            @Override
            public void onResult(NodeApi.GetConnectedNodesResult nodes) {
                for (Node node : nodes.getNodes()) {
                    mNode = node;
                }
            }
        });
    }

    private void getPM10(int pm, View v){
        ImageView pm10 = v.findViewById(R.id.pm10);
        TextView pm10Value = v.findViewById(R.id.pm10Value);
        TextView pm10Text = v.findViewById(R.id.pm10Text);
        if(pm <= 30){
            pm10.setImageResource(R.drawable.pm10);
            pm10Value.setText(pm+" ( 좋음 )");
            pm10Text.setText(R.string.pm10_good_value);
        }else if(pm <= 80){
            pm10.setImageResource(R.drawable.pm10);
            pm10Value.setText(pm+" ( 보통 )");
            pm10Text.setText(R.string.pm10_moderate_value);
        }else if(pm < 150){
            pm10.setImageResource(R.drawable.pm10);
            pm10Value.setText(pm+" ( 나쁨 )");
            pm10Text.setText(R.string.pm10_poor_value);
        }else{
            pm10.setImageResource(R.drawable.pm10);
            pm10Value.setText(pm+" ( 매우나쁨 )");
            pm10Text.setText(R.string.pm10_bad_value);
        }
    }

    private void stress(int st, View v){
        ImageView stress = v.findViewById(R.id.stress);
        TextView stressValue = v.findViewById(R.id.stressValue);
        TextView stressText = v.findViewById(R.id.stressText);

        if(st < 68){
            stress.setImageResource(R.drawable.stress);
            stressValue.setText(st+" ( 낮음 )");
            stressText.setText(R.string.stress_good_value);
        }else if(st < 75){
            stress.setImageResource(R.drawable.stress);
            stressValue.setText(st+" ( 보통 )");
            stressText.setText(R.string.stress_moderate_value);
        }else if(st < 80){
            stress.setImageResource(R.drawable.stress);
            stressValue.setText(st+" ( 높음 )");
            stressText.setText(R.string.stress_poor_value);
        }else{
            stress.setImageResource(R.drawable.stress);
            stressValue.setText(st+" ( 매우높음 )");
            stressText.setText(R.string.stress_bad_value);
        }
    }

    private void wind(double speed, String direction, View v){
        ImageView wind = v.findViewById(R.id.wind);
        wind.setImageResource(R.drawable.wind);

        TextView windSpeed = v.findViewById(R.id.windSpeed);
        windSpeed.setText(String.format("%.1f ms",speed));

        ImageView windDirection = v.findViewById(R.id.windDirection);

        if(direction.equals("N")){
            windDirection.setImageResource(R.drawable.dir09);
        }else if(direction.equals("NNE")){
            windDirection.setImageResource(R.drawable.dir10);
        }else if(direction.equals("NE")){
            windDirection.setImageResource(R.drawable.dir11);
        }else if(direction.equals("ENE")){
            windDirection.setImageResource(R.drawable.dir12);
        }else if(direction.equals("E")){
            windDirection.setImageResource(R.drawable.dir13);
        }else if(direction.equals("ESE")){
            windDirection.setImageResource(R.drawable.dir14);
        }else if(direction.equals("SE")){
            windDirection.setImageResource(R.drawable.dir15);
        }else if(direction.equals("SSE")){
            windDirection.setImageResource(R.drawable.dir16);
        }else if(direction.equals("S")){
            windDirection.setImageResource(R.drawable.dir01);
        }else if(direction.equals("SSW")){
            windDirection.setImageResource(R.drawable.dir02);
        }else if(direction.equals("SW")){
            windDirection.setImageResource(R.drawable.dir03);
        }else if(direction.equals("WSW")){
            windDirection.setImageResource(R.drawable.dir04);
        }else if(direction.equals("W")){
            windDirection.setImageResource(R.drawable.dir05);
        }else if(direction.equals("WNW")){
            windDirection.setImageResource(R.drawable.dir06);
        }else if(direction.equals("NW")){
            windDirection.setImageResource(R.drawable.dir07);
        }else if(direction.equals("NNW")){
            windDirection.setImageResource(R.drawable.dir08);
        }
    }

    private void getSun(String rise, String set, View v){
        ImageView sun = v.findViewById(R.id.sun);
        sun.setImageResource(R.drawable.sun);

        ImageView sunriseImage = v.findViewById(R.id.sunriseImage);
        sunriseImage.setImageResource(R.drawable.sunrise);

        TextView sunriseText = v.findViewById(R.id.sunriseText);
        sunriseText.setText(rise);

        ImageView sunsetImage = v.findViewById(R.id.sunsetImage);
        sunsetImage.setImageResource(R.drawable.sunset);

        TextView sunsetText = v.findViewById(R.id.sunsetText);
        sunsetText.setText(set);
    }

    private void getBGId(int type, boolean is_night) {
        switch (type) {
            case 1:
                if (is_night) {
                    backgroundImage.setBackgroundResource(R.drawable.bg08);
                } else {
                    backgroundImage.setBackgroundResource(R.drawable.bg01);
                }
                break;
            case 2:
                if (is_night) {
                    backgroundImage.setBackgroundResource(R.drawable.bg09);
                } else {
                    backgroundImage.setBackgroundResource(R.drawable.bg02);
                }
                break;
            case 21:
                if (is_night) {
                    backgroundImage.setBackgroundResource(R.drawable.bg10);
                } else {
                    backgroundImage.setBackgroundResource(R.drawable.bg03);
                }
                break;
            case 22:
                if (is_night) {
                    backgroundImage.setBackgroundResource(R.drawable.bg07);
                }
                break;
            case 4:
            case 7:
            case 10:
            case 17:
            case 18:
                if (is_night) {
                    backgroundImage.setBackgroundResource(R.drawable.bg12);
                } else {
                    backgroundImage.setBackgroundResource(R.drawable.bg05);
                }
                break;
            case 5:
            case 6:
            case 8:
            case 19:
            case 20:
                if (is_night) {
                    backgroundImage.setBackgroundResource(R.drawable.bg13);
                } else {
                    backgroundImage.setBackgroundResource(R.drawable.bg06);
                }
                break;
            default:
                if (is_night) {
                    backgroundImage.setBackgroundResource(R.drawable.bg11);
                } else {
                    backgroundImage.setBackgroundResource(R.drawable.bg04);
                }
                break;
        }
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view == object);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.refreshDrawableState();
    }

    private Bitmap decodeSampledBitmapFromResource(Resources res, int resId) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = 2;

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        resolveNode();
    }

    @Override
    public void onConnectionSuspended(int i) {
        //Improve your code
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        //Improve your code
    }

}

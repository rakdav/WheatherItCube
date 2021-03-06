package com.example.wheatheritcube;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private boolean binded=false;
    private ServiceWeather serviceWeather;
    private static Activity  activity;
    private static final int MY_PERMISSION_REQUEST_CODE_FINE = 1;
    private static final int MY_PERMISSION_REQUEST_COARSE_FINE = 1;

    ServiceConnection weatherServiceconnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ServiceWeather.LocalWheatherBinder binder=(ServiceWeather.LocalWheatherBinder)service;
            serviceWeather=binder.getService();
            binded=true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            binded=false;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity=this;
        watchMileage();

    }

    public Activity getActivity() {
        return activity;
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Подключение разрещений
        int accessFinePermisson = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int accessCoarsePermisson = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);
        ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},MY_PERMISSION_REQUEST_CODE_FINE);
        ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},MY_PERMISSION_REQUEST_COARSE_FINE);
        //
        Intent intent=new Intent(MainActivity.this,ServiceWeather.class);
        bindService(intent,weatherServiceconnection, Context.BIND_AUTO_CREATE);
        Log.d("serv",Boolean.toString(binded));
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(binded)
        {
            unbindService(weatherServiceconnection);
            binded=false;
        }
    }

    public static void getAccessCoarsePermisions()
    {
        ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},MY_PERMISSION_REQUEST_COARSE_FINE);
    }
    private void watchMileage()
    {
        final TextView textView=findViewById(R.id.distance);;
        Handler handler=new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                double distanse=0.0;
                if(serviceWeather!=null) distanse=serviceWeather.getMiles()/1000;
                textView.setText("Steps:"+distanse);
                handler.postDelayed(this,1000);
            }

        });
    }


}
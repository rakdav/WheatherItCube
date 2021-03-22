package com.example.wheatheritcube;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private boolean binded=false;
    private ServiceWeather serviceWeather;

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
        watchMileage();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent=new Intent(this,ServiceWeather.class);
        bindService(intent,weatherServiceconnection, Context.BIND_AUTO_CREATE);
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
    private void watchMileage()
    {
        final TextView textView=findViewById(R.id.distance);;
        Handler handler=new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                double distanse=0.0;
                if(serviceWeather!=null) distanse=serviceWeather.getMiles();
                textView.setText("Steps:"+distanse);
                handler.postDelayed(this,1000);
            }

        });
    }
}
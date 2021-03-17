package com.example.wheatheritcube;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

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
}
package com.example.wheatheritcube;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class ServiceWeather extends Service {
    private static String LOG_TAG="WheatherService";
    private final IBinder binder=new LocalWheatherBinder();
    public class LocalWheatherBinder extends Binder
    {
        public ServiceWeather getService()
        {
            return ServiceWeather.this;
        }
    }
    public ServiceWeather() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return this.binder;
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return true;
    }
}
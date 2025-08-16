package com.example.ktl

import android.app.Application
import android.util.Log
import com.google.android.gms.ads.MobileAds

class MainApp:Application() {
    override fun onCreate() {
        super.onCreate()
        MobileAds.initialize(this){
            Log.d("MyLog","Yandex Ads SDK initialized")
        }
    }
}
package com.shanemaglangit.a2020

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.google.android.gms.ads.MobileAds

//private const val ACTION_START_BREAK = "${BuildConfig.APPLICATION_ID}.ACTION_START_BREAK"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MobileAds.initialize(this) {}
    }

    override fun onStart() {
        super.onStart()
        if(intent.getBooleanExtra("startCountdown", false)) {
            this.findNavController(R.id.nav_host_fragment).navigate(R.id.restFragment)
        }
    }
}

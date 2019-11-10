package com.shanemaglangit.a2020

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.google.android.gms.ads.MobileAds
import com.shanemaglangit.a2020.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startKoin {
            androidContext(this@MainActivity)
            modules(appModule)
        }

        MobileAds.initialize(this) {}
    }

    override fun onStart() {
        super.onStart()
        if(intent.getBooleanExtra("startCountdown", false)) {
            this.findNavController(R.id.nav_host_fragment).navigate(R.id.restFragment)
        }
    }
}

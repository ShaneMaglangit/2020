package com.shanemaglangit.a2020.rest

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.shanemaglangit.a2020.R
import com.shanemaglangit.a2020.databinding.ActivityRestBinding

class RestActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRestBinding
    private lateinit var restViewModel: RestViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MobileAds.initialize(this) {}

        binding = DataBindingUtil.setContentView(this, R.layout.activity_rest)

        restViewModel = ViewModelProvider(this, RestViewModelFactory(application))
            .get(RestViewModel::class.java)

        binding.restViewModel = restViewModel
        binding.lifecycleOwner = this

        // Load the ads
        binding.adBreak.loadAd(AdRequest.Builder().build())

        restViewModel.startTimer()

        // Toggle the visibility of views once timer ends
        restViewModel.timeLeft.observe(this, Observer {
            if (it == 0) {
                binding.textTimeleftCaption.visibility = View.GONE
                binding.progressTimeElapsed.visibility = View.GONE
                binding.textTimeleft.visibility = View.GONE
                binding.buttonEnd.visibility = View.VISIBLE
            }
        })

        // Stops the activity
        restViewModel.endActivity.observe(this, Observer {
            if (it) {
                if (Build.VERSION.SDK_INT >= 21) finishAndRemoveTask()
                else finishAffinity()
            }
        })
    }
}

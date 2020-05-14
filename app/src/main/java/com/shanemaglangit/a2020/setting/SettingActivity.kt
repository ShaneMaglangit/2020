package com.shanemaglangit.a2020.setting

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import com.shanemaglangit.a2020.R
import com.shanemaglangit.a2020.databinding.ActivityMainBinding
import com.shanemaglangit.a2020.setRatingText

class SettingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var settingViewModel: SettingViewModel
    private lateinit var interstitialAd: InterstitialAd

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MobileAds.initialize(this) {}

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        settingViewModel = ViewModelProvider(
            this,
            SettingViewModelFactory(application)
        ).get(SettingViewModel::class.java)

        binding.settingViewModel = settingViewModel
        binding.lifecycleOwner = this

        binding.textRating.startTextAnimation(
            settingViewModel.userRating.value!!, 2500, 1000, true
        )

        binding.textTotal.startTextAnimation(
            settingViewModel.totalBreak.value!!,
            1500,
            1000
        )

        binding.textCompleted.startTextAnimation(
            settingViewModel.completedBreak.value!!,
            1500,
            1000
        )

        binding.textSkipped.startTextAnimation(
            settingViewModel.skippedBreak.value!!,
            1500,
            1000
        )

        // Set up liveData observers
        settingViewModel.duration.setFieldChangeObserver()
        settingViewModel.work.setFieldChangeObserver()
        settingViewModel.invalidFields.setInvalidFieldObserver()
        settingViewModel.clickCount.setCountObserver()

        // Load the interstitial ad
        loadInterstitialAd()
    }

    /**
     * Used to set a valueAnimator for the TextView
     * @param endValue value where the animation will stop
     * @param duration duration of the animation
     * @param delay time before the animation starts
     * @param isRating is the view a rating
     */
    private fun TextView.startTextAnimation(
        endValue: Int,
        duration: Long,
        delay: Long = 0,
        isRating: Boolean = false
    ) {
        val valueAnimator = ValueAnimator.ofInt(0, endValue)
        valueAnimator.addUpdateListener {
            if (isRating) setRatingText(this, valueAnimator.animatedValue.toString())
            else this.text = valueAnimator.animatedValue.toString()
        }
        valueAnimator.duration = duration
        valueAnimator.startDelay = delay
        valueAnimator.start()
    }

    /**
     * Used to set the observer for invalidField
     */
    private fun LiveData<Boolean>.setInvalidFieldObserver() {
        this.observe(this@SettingActivity, Observer {
            if (it) {
                AlertDialog.Builder(ContextThemeWrapper(this@SettingActivity, R.style.AppTheme_Dialog))
                    .setTitle("Invalid fields")
                    .setMessage("Fields cannot be set to 0")
                    .setCancelable(true)
                    .setPositiveButton("Ok", null)
                    .setOnDismissListener { settingViewModel.invalidFieldsDialogComplete() }
                    .show()
            }
        })
    }


    /**
     * Used to set the observer for observing if the mutableLiveData changed
     */
    private fun MutableLiveData<Int>.setFieldChangeObserver() {
        this.observe(this@SettingActivity, Observer {
            settingViewModel.fieldsChanged()
        })
    }

    /**
     * Used to set the observer for observing the current click count of the button
     */
    private fun LiveData<Int>.setCountObserver() {
        this.observe(this@SettingActivity, Observer {
            if (it > 3) {
                interstitialAd.adListener = object : AdListener() {
                    override fun onAdClosed() {
                        super.onAdClosed()
                        interstitialAd.loadAd(AdRequest.Builder().build())
                        settingViewModel.enableBreaks()
                        settingViewModel.resetClickCount()
                    }
                }

                if (interstitialAd.isLoaded) interstitialAd.show()
                else {
                    settingViewModel.enableBreaks()
                    settingViewModel.resetClickCount()
                }
            }
        })
    }

    /**
     * Used to load the interstitial ad
     */
    private fun loadInterstitialAd() {
        interstitialAd = InterstitialAd(this)
        interstitialAd.adUnitId = resources.getString(R.string.AD_INTERSTITIAL_ID)
        interstitialAd.loadAd(AdRequest.Builder().build())
    }
}

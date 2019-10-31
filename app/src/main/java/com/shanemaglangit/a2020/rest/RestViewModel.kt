package com.shanemaglangit.a2020.rest

import android.app.Application
import android.content.Context
import android.media.MediaPlayer
import android.os.Build
import android.os.CountDownTimer
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.shanemaglangit.a2020.R

class RestViewModel(application: Application) : AndroidViewModel(application) {

    private val sharedPreferences =
        application.getSharedPreferences("user_pref", Context.MODE_PRIVATE)

    private val mediaPlayer = MediaPlayer.create(application, R.raw.tropical)
    private val vibrator = application.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

    private var _progress = MutableLiveData<Int>()
    val progress: LiveData<Int>
        get() = _progress

    private var _timeLeft = MutableLiveData<Int>()
    val timeLeft: LiveData<Int>
        get() = _timeLeft

    private var _max = MutableLiveData<Int>(sharedPreferences.getInt("break_duration", 20) * 1000)
    val max: LiveData<Int>
        get() = _max

    fun startTimer() {
        val timer = object: CountDownTimer(max.value!!.toLong(), 10) {
            override fun onTick(millisUntilFinished: Long) {
                val millisElapsed = max.value!! - millisUntilFinished.toInt()
                _timeLeft.value = millisUntilFinished.toInt() / 1000
                _progress.value = millisElapsed
            }

            override fun onFinish() {
                playRingtoneAndVibrate()
            }
        }

        timer.start()
    }

    fun playRingtoneAndVibrate() {
        mediaPlayer.start()

        if(Build.VERSION.SDK_INT >= 26)
            vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
        else vibrator.vibrate(500)
    }
}
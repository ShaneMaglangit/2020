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
    // Shared Preferences
    private val sharedPreferences =
        application.getSharedPreferences("user_pref", Context.MODE_PRIVATE)
    private val editor = sharedPreferences.edit()

    private lateinit var timer: CountDownTimer

    // Objects for Ringtone and Vibration
    private val mediaPlayer = MediaPlayer.create(application, R.raw.notification)
    private val vibrator = application.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

    // Observer variables for events
    private val _endActivity = MutableLiveData<Boolean>()
    val endActivity: LiveData<Boolean>
        get() = _endActivity


    // Rest Values
    private val _progress = MutableLiveData<Int>()
    val progress: LiveData<Int>
        get() = _progress

    private val _timeLeft = MutableLiveData<Int>()
    val timeLeft: LiveData<Int>
        get() = _timeLeft

    private val _max = MutableLiveData<Int>(sharedPreferences.getInt("break_duration", 20) * 1000)
    val max: LiveData<Int>
        get() = _max

    // Performance Values
    private val totalBreaks = sharedPreferences.getInt("total_break", 0)
    private val completedBreaks = sharedPreferences.getInt("completed_break", 0)
    private val skippedBreaks = sharedPreferences.getInt("skipped_break", 0)

    /**
     * Used to start the timer
     */
    fun startTimer() {
        // Update the sharedPreferences
        editor.putInt("total_break", totalBreaks + 1)
        editor.putInt("skipped_break", skippedBreaks + 1)
        editor.apply()

        // Create the timer object
        timer = object : CountDownTimer(max.value!!.toLong(), 10) {
            override fun onTick(millisUntilFinished: Long) {
                val millisElapsed = max.value!! - millisUntilFinished.toInt()
                _timeLeft.value = millisUntilFinished.toInt() / 1000
                _progress.value = millisElapsed
            }

            override fun onFinish() {
                editor.putInt("skipped_break", skippedBreaks)
                editor.putInt("completed_break", completedBreaks + 1)
                editor.apply()

                playRingtoneAndVibrate()
            }
        }

        timer.start()
    }

    /**
     * Used to close the activity
     */
    fun closeRest() {
        _endActivity.value = true
    }

    /**
     * Used to play the ringtone and vibrate
     */
    fun playRingtoneAndVibrate() {
        mediaPlayer.start()

        if (Build.VERSION.SDK_INT >= 26)
            vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
        else vibrator.vibrate(500)
    }
}
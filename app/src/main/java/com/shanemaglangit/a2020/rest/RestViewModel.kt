package com.shanemaglangit.a2020.rest

import android.app.Application
import android.content.Context
import android.os.CountDownTimer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class RestViewModel(application: Application) : AndroidViewModel(application) {

    private val sharedPreferences =
        application.getSharedPreferences("user_pref", Context.MODE_PRIVATE)

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

            override fun onFinish() {}
        }

        timer.start()
    }
}
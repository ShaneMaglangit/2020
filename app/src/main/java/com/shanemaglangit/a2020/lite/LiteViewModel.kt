package com.shanemaglangit.a2020.lite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class LiteViewModel(application: Application) : AndroidViewModel(application) {
    private val sharedPreferences = application.getSharedPreferences("user_pref", 0)

    val duration = MutableLiveData<Int>(sharedPreferences.getInt("break_duration", 20))
    val work = MutableLiveData<Int>(sharedPreferences.getInt("work_duration", 20))

    fun savePreferences() {
        val editor = sharedPreferences.edit()
        editor.putInt("break_duration", duration.value!!)
        editor.putInt("work_duration", work.value!!)
        editor.apply()
    }
}
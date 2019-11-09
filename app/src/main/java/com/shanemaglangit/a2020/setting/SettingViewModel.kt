package com.shanemaglangit.a2020.setting

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.shanemaglangit.a2020.AlarmReceiver
import com.shanemaglangit.a2020.setAlarmManager

class SettingViewModel(application: Application) : AndroidViewModel(application) {
    private val sharedPreferences
            = application.getSharedPreferences("user_pref", Context.MODE_PRIVATE)
    private val editor = sharedPreferences.edit()

    private val alarmReceiver = AlarmReceiver()

    val duration = MutableLiveData<Int>(sharedPreferences.getInt("break_duration", 20000) / 1000)
    val work = MutableLiveData<Int>(sharedPreferences.getInt("work_duration", 1200000) / 60000)
    val isEnabled = MutableLiveData<Boolean>(sharedPreferences.getBoolean("break_enabled", false))

    fun startBreaks() {
        if((work.value == 0) or (duration.value == 0)) {
            Toast
                .makeText(getApplication(), "Fields can not be set to 0!", Toast.LENGTH_SHORT)
                .show()
        }
        else {
            editor.putInt("break_duration", duration.value!!)
            editor.putInt("work_duration", work.value!!)
            editor.apply()

            setAlarmManager(getApplication())

            Toast
                .makeText(getApplication(), "Breaks are now started!", Toast.LENGTH_SHORT)
                .show()

            isEnabled.value = true
        }
    }

    fun stopBreaks() {
        if(isEnabled.value!!) {
            isEnabled.value = false
        }
    }

    fun toggleEnabled() {
        try {
            if (isEnabled.value!!) {
                val intentFilter = IntentFilter()
                intentFilter.addAction(Intent.ACTION_SCREEN_ON)
                intentFilter.addAction(Intent.ACTION_SCREEN_OFF)

                getApplication<Application>().registerReceiver(alarmReceiver, intentFilter)
            }
            else {
                getApplication<Application>().unregisterReceiver(alarmReceiver)
            }
        }
        catch(illegalArgumentException: IllegalArgumentException) {
            Log.i("SettingViewModel", illegalArgumentException.toString())
        }

        editor.putBoolean("break_enabled", isEnabled.value!!)
        editor.apply()

        setAlarmManager(getApplication())

        Toast
            .makeText(getApplication(), "Break is now ${if(isEnabled.value!!) "enabled" else "disabled"}", Toast.LENGTH_SHORT)
            .show()
    }

    fun enableBreaks() {
        editor.putInt("break_duration", duration.value!!)
        editor.putInt("work_duration", work.value!!)
        editor.putBoolean("break_enabled", true)
        editor.apply()

        isEnabled.value = true
    }

    fun disableBreaks() {
        editor.putBoolean("break_enabled", false)
        editor.apply()

        isEnabled.value = false
    }

    private fun startBreakService() {
        val context = getApplication<Application>()

        if(Build.VERSION.SDK_INT >= 26) {
            context.startForegroundService(Intent(context, BreakService::class.java))
        }
        else {
            context.startService(Intent(context, BreakService::class.java))
        }
    }

    private fun stopBreakService() {
        getApplication<Application>().stopService(Intent(getApplication(), BreakService::class.java))
    }

}
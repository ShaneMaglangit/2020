package com.shanemaglangit.a2020.setting

import android.app.Application
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.shanemaglangit.a2020.service.BreakService

class SettingViewModel(
    application: Application,
    sharedPreferences: SharedPreferences
) : AndroidViewModel(application) {
    private val editor = sharedPreferences.edit()

    private val _showDisabledSnackbar = MutableLiveData<Boolean>()
    val showDisabledSnackbar: LiveData<Boolean>
            get() = _showDisabledSnackbar

    private val _invalidFields = MutableLiveData<Boolean>()
    val invalidFields: LiveData<Boolean>
        get() = _invalidFields

    val duration = MutableLiveData<Int>(sharedPreferences.getInt("break_duration", 20))
    val work = MutableLiveData<Int>(sharedPreferences.getInt("work_duration", 20))
    val isEnabled = MutableLiveData<Boolean>(sharedPreferences.getBoolean("break_enabled", false))

    fun toggleBreaks() {
        if(isEnabled.value == false) {
            if((work.value == 0) or (duration.value == 0)) _invalidFields.value = true
            else enableBreaks()
        }
        else if(isEnabled.value == true) {
            disableBreaks()
        }
    }

    fun enableBreaks() {
        editor.putInt("break_duration", duration.value!!)
        editor.putInt("work_duration", work.value!!)
        editor.putBoolean("break_enabled", true)
        editor.apply()

        isEnabled.value = true

        if(Build.VERSION.SDK_INT >= 26) {
            getApplication<Application>().startForegroundService(Intent(getApplication(), BreakService::class.java))
        }
        else {
            getApplication<Application>().startService(Intent(getApplication(), BreakService::class.java))
        }
    }

    fun disableBreaks() {
        editor.putBoolean("break_enabled", false)
        editor.apply()

        isEnabled.value = false
        _showDisabledSnackbar.value = true

        getApplication<Application>().stopService(Intent(getApplication(), BreakService::class.java))
    }

    fun disabledSnackbarComplete() {
        _showDisabledSnackbar.value = false
    }

    fun invalidFieldsSnackbarComplete() {
        _invalidFields.value = false
    }
}
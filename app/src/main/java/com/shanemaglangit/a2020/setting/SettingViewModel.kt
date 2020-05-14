package com.shanemaglangit.a2020.setting

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.shanemaglangit.a2020.service.BreakService

/**
 * ViewModel for the SettingFragment
 * @param application application for the AndroidViewModel
 */
class SettingViewModel(application: Application) : AndroidViewModel(application) {
    // Shared Preferences
    private val sharedPreferences =
        application.getSharedPreferences("user_pref", Context.MODE_PRIVATE)
    private val editor = sharedPreferences.edit()

    // Observer Variables for Dialogs
    private val _invalidFields = MutableLiveData<Boolean>()
    val invalidFields: LiveData<Boolean>
        get() = _invalidFields

    // Performance Values
    private val _totalBreak = MutableLiveData<Int>(sharedPreferences.getInt("total_break", 0))
    val totalBreak: LiveData<Int>
        get() = _totalBreak

    private val _completedBreak =
        MutableLiveData<Int>(sharedPreferences.getInt("completed_break", 0))
    val completedBreak: LiveData<Int>
        get() = _completedBreak

    private val _skippedBreak = MutableLiveData<Int>(sharedPreferences.getInt("skipped_break", 0))
    val skippedBreak: LiveData<Int>
        get() = _skippedBreak

    private val _userRating = MutableLiveData<Int>()
    val userRating: LiveData<Int>
        get() = _userRating

    // Preferences Values
    private val _clickCount = MutableLiveData<Int>(sharedPreferences.getInt("click_count", 0))
    val clickCount: LiveData<Int>
        get() = _clickCount

    val duration = MutableLiveData<Int>(sharedPreferences.getInt("break_duration", 20))
    val work = MutableLiveData<Int>(sharedPreferences.getInt("work_duration", 20))
    val isEnabled = MutableLiveData<Boolean>(sharedPreferences.getBoolean("break_enabled", false))

    init {
        _userRating.value =
            if (_totalBreak.value != 0) {
                (_completedBreak.value!!.toDouble() / _totalBreak.value!!.toDouble() * 100).toInt()
            } else 100
    }

    /**
     * Used to toggle breaks between enabled and disabled
     */
    fun toggleBreaks() {
        if (isEnabled.value == false) {
            if ((work.value == 0) or (duration.value == 0)) _invalidFields.value = true
            else {
                incrementClickCount()
                if(clickCount.value!! <= 3) enableBreaks()
            }
        } else if (isEnabled.value == true) {
            disableBreaks()
        }
    }

    /**
     * Used to enable breaks and start the break service
     */
    fun enableBreaks() {
        // Update the sharedPreferences
        editor.putInt("break_duration", duration.value!!)
        editor.putInt("work_duration", work.value!!)
        editor.apply()

        // Update the liveData
        isEnabled.value = true

        // Start the service
        if (Build.VERSION.SDK_INT >= 26) {
            getApplication<Application>().startForegroundService(
                Intent(
                    getApplication(),
                    BreakService::class.java
                )
            )
        } else {
            getApplication<Application>().startService(
                Intent(
                    getApplication(),
                    BreakService::class.java
                )
            )
        }
    }

    /**
     * Used to disable breakd and stop the break service
     */
    private fun disableBreaks() {
        // Update the liveData
        isEnabled.value = false

        // Start the service
        getApplication<Application>().stopService(
            Intent(
                getApplication(),
                BreakService::class.java
            )
        )
    }

    /**
     * Triggered when fields are changed
     */
    fun fieldsChanged() {
        if ((work.value != sharedPreferences.getInt("work_duration", 20))
            or (duration.value != sharedPreferences.getInt("break_duration", 20))
            and isEnabled.value!!
        ) {
            disableBreaks()
        }
    }

    /**
     * Used to notify that the invalid fields dialog is successfully shown
     */
    fun invalidFieldsDialogComplete() {
        _invalidFields.value = false
    }

    /**
     * Used to reset the click count observed for showing the ads
     */
    fun resetClickCount() {
        _clickCount.value = 0
        editor.putInt("click_count", clickCount.value!!)
        editor.apply()
    }

    /**
     * Used to increase click count
     */
    private fun incrementClickCount() {
        // Update the click count
        _clickCount.value = _clickCount.value?.plus(1) ?: 0
        editor.putInt("click_count", clickCount.value!!)
        editor.apply()
    }
}
package com.shanemaglangit.a2020

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity

fun setAlarmManager(context: Context, isActive: Boolean? = null, resumedInterval: Int? = null) {
    val sharedPreferences = context.getSharedPreferences("user_pref", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    val isEnabled = isActive ?: sharedPreferences.getBoolean("break_enabled", false)
    val breakDuration = sharedPreferences.getInt("break_duration", 20000)
    val workDuration = sharedPreferences.getInt("work_duration", 1200000)

    val alarmManager = context.getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager
    val alarmIntent = Intent(context, AlarmReceiver::class.java)
    val alarmPendingIntent =
        PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT)

    if (isEnabled) {
        val triggerInterval = resumedInterval ?: breakDuration + workDuration

        editor.putLong("startOfAlarm", System.currentTimeMillis())
        editor.apply()

        if (Build.VERSION.SDK_INT >= 23)
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC,
                System.currentTimeMillis() + triggerInterval,
                alarmPendingIntent
            )
        else alarmManager.setExact(AlarmManager.RTC, System.currentTimeMillis() + triggerInterval, alarmPendingIntent)
    }
    else {
        val startOfAlarm = sharedPreferences.getLong("startOfAlarm", 0)
        val workTimeLeft = breakDuration - (startOfAlarm - System.currentTimeMillis())

        editor.putInt("workTimeLeft", workTimeLeft.toInt())
        editor.apply()

        alarmManager.cancel(alarmPendingIntent)
    }
}
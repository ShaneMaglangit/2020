package com.shanemaglangit.a2020

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity

fun setAlarmManager(context: Context) {
    val sharedPreferences = context.getSharedPreferences("user_pref", Context.MODE_PRIVATE)
    val workDuration = sharedPreferences.getInt("work_duration", 20)
    val isEnabled = sharedPreferences.getBoolean("break_enabled", false)

    // Limiting the trigger interval to 5 seconds for testing purposes.
    val triggerInterval = System.currentTimeMillis() + 5000

    if (isEnabled) {
        val alarmManager = context.getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(context, AlarmReceiver::class.java)
        val alarmPendingIntent =
            PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        if (Build.VERSION.SDK_INT >= 23)
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC,
                triggerInterval,
                alarmPendingIntent
            )
        else alarmManager.setExact(AlarmManager.RTC, triggerInterval, alarmPendingIntent)
    }
}
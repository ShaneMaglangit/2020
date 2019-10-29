package com.shanemaglangit.a2020

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager

//private const val ACTION_START_BREAK = "${BuildConfig.APPLICATION_ID}.ACTION_START_BREAK"

class MainActivity : AppCompatActivity() {
    private lateinit var localBroadcastManager: LocalBroadcastManager
    private val receiver = AlarmReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupAlarmManager()
//
//        localBroadcastManager = LocalBroadcastManager.getInstance(this)
//        localBroadcastManager.registerReceiver(receiver, IntentFilter(ACTION_START_BREAK))
    }

    private fun setupAlarmManager() {
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

        val alarmIntent = Intent(this, AlarmReceiver::class.java)
        alarmIntent.putExtra("startCountdown", true)

        val alarmPendingIntent =
                PendingIntent.getBroadcast(this, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        if (Build.VERSION.SDK_INT >= 23) alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC, System.currentTimeMillis() + 20 * 60000, alarmPendingIntent)
        else alarmManager.setExact(AlarmManager.RTC, System.currentTimeMillis() + 20 * 60000, alarmPendingIntent)
    }

    override fun onDestroy() {
        super.onDestroy()
//        localBroadcastManager.unregisterReceiver(receiver)
    }
}

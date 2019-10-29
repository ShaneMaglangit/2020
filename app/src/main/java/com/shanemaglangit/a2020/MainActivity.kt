package com.shanemaglangit.a2020

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager

private const val ACTION_START_BREAK = "${BuildConfig.APPLICATION_ID}.ACTION_START_BREAK"
private const val REQUEST_CODE = 0

class MainActivity : AppCompatActivity() {
    private lateinit var localBroadcastManager: LocalBroadcastManager
    private lateinit var alarmManager: AlarmManager
    private val receiver = AlarmReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val alarmIntent = Intent(this, AlarmReceiver::class.java)
        val alarmPendingIntent =
            PendingIntent.getBroadcast(this, REQUEST_CODE, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        localBroadcastManager = LocalBroadcastManager.getInstance(this)
        localBroadcastManager.registerReceiver(receiver, IntentFilter(ACTION_START_BREAK))
    }

    override fun onDestroy() {
        super.onDestroy()
        localBroadcastManager.unregisterReceiver(receiver)
    }
}

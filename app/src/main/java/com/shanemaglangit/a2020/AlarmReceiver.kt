package com.shanemaglangit.a2020

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

private const val ACTION_START_BREAK = BuildConfig.APPLICATION_ID + ".ACTION_START_BREAK"

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val i = Intent(context, MainActivity::class.java)
        i.putExtra("startCountdown", true)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        context.startActivity(i)

        setAlarmManager(context)
    }
}

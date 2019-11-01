package com.shanemaglangit.a2020

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat


class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        when(intent.action) {
            Intent.ACTION_SCREEN_ON -> {
                showResumeNotification(context)
                setAlarmManager(context)
                Log.i("AlarmReceiver", "Break is resumed")
            }
            Intent.ACTION_SCREEN_OFF -> {
                setAlarmManager(context, isCanceled = true)
                Log.i("AlarmReceiver", "Break is paused")
            }
            else -> {
                val i = Intent(context, MainActivity::class.java)
                i.putExtra("startCountdown", true)
                i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
                context.startActivity(i)

                setAlarmManager(context)
            }
        }
    }

    private fun showResumeNotification(context: Context) {
        val channelId = "CHANNEL_2020"
        val notificationManagerCompat = NotificationManagerCompat.from(context)
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentTitle("Breaks are now resumed")
            .setContentText("Breaks are paused when phone is locked and automatically resumed once re-opened.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        if (Build.VERSION.SDK_INT >= 27) {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val notificationChannel = NotificationChannel(
                channelId,
                "2020 Notification Channel",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }

       notificationManagerCompat.notify(0, builder.build())
    }
}

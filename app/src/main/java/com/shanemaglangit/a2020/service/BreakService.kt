package com.shanemaglangit.a2020.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.*
import android.os.Build
import android.os.CountDownTimer
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.shanemaglangit.a2020.MainActivity
import com.shanemaglangit.a2020.R

class BreakService : Service() {
    private lateinit var alarmReceiver: BroadcastReceiver
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var timer: CountDownTimer
    private var breakDuration = 20000L
    private var workDuration = 1200000L
    private var remainingMillis = 0L

    companion object {
        const val CHANNEL_ID = "CHANNEL_2020"
    }

    override fun onCreate() {
        super.onCreate()
        sharedPreferences = this.getSharedPreferences("user_pref", Context.MODE_PRIVATE)
        breakDuration = sharedPreferences.getInt("break_duration", 20).toLong() * 1000
        workDuration = sharedPreferences.getInt("work_duration", 20).toLong() * 60000

        alarmReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                when (intent.action) {
                    Intent.ACTION_SCREEN_OFF -> {
                        stopTimer()
                        Log.i("BreakService", "Timer stopped with $remainingMillis")
                    }
                    Intent.ACTION_USER_PRESENT -> {
                        startTimer(remainingMillis)
                        Log.i("BreakService", "Timer resumed with $remainingMillis")
                    }
                }
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val settingIntent = Intent(this, MainActivity::class.java)
            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val settingPendingIntent = PendingIntent.getActivity(this, 0, settingIntent, 0)

        val notification = NotificationCompat.Builder(this,
            CHANNEL_ID
        )
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentTitle("Break reminders are active")
            .setContentText("Click here to modify your preferences")
            .setContentIntent(settingPendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        val intentFilter = IntentFilter()
        intentFilter.addAction(Intent.ACTION_USER_PRESENT)
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF)

        startTimer(workDuration)
        createNotificationChannel()
        registerReceiver(alarmReceiver, intentFilter)
        startForeground(1, notification)
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        stopTimer()
        unregisterReceiver(alarmReceiver)
    }

    private fun startTimer(endOfTimerInMillis: Long) {
         timer = object: CountDownTimer(endOfTimerInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                remainingMillis = millisUntilFinished
            }

            override fun onFinish() {
                val restIntent = Intent(this@BreakService, MainActivity::class.java)
                .putExtra("startCountdown", true)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)

                startActivity(restIntent)
                startTimer(workDuration + breakDuration)
            }
        }

        timer.start()
    }

    private fun stopTimer() {
        timer.cancel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= 27) {
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                "2020 Notification Channel",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }
}
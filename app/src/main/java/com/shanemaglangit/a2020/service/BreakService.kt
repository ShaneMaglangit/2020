package com.shanemaglangit.a2020.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.*
import android.media.RingtoneManager
import android.os.Build
import android.os.CountDownTimer
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.shanemaglangit.a2020.setting.SettingActivity
import com.shanemaglangit.a2020.R
import com.shanemaglangit.a2020.rest.RestActivity

class BreakService : Service() {
    private lateinit var alarmReceiver: BroadcastReceiver
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var timer: CountDownTimer
    private lateinit var notificationManager: NotificationManager
    private var breakDuration = 20000L
    private var workDuration = 1200000L
    private var remainingMillis = 0L

    companion object {
        const val CHANNEL_ID = "CHANNEL_2020"
    }

    /**
     * Used to instantiate and initialize lateinit variables and is also responsible
     * for getting the preferences set by the user in setting fragment.
     */
    override fun onCreate() {
        super.onCreate()
        sharedPreferences = this.getSharedPreferences("user_pref", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
        breakDuration = sharedPreferences.getInt("break_duration", 20).toLong() * 1000
        workDuration = sharedPreferences.getInt("work_duration", 20).toLong() * 60000
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        editor.putBoolean("break_enabled", true)
        editor.apply()

        // Broadcast receiver to catch broadcast events.
        alarmReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                when (intent.action) {
                    // Used to pause the timer
                    Intent.ACTION_SCREEN_OFF -> stopTimer()
                    // Used to restart the timer
                    Intent.ACTION_USER_PRESENT -> startTimer(remainingMillis)
                    // Used to ensure the service stops when the phone is shutdown
                    Intent.ACTION_SHUTDOWN -> stopSelf()
                }
            }
        }
    }

    /**
     * Used when the foreground service is started that creates the notification,
     * start the timer for the breaks, and register the receiver to catch broadcasts
     */
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val settingIntent = Intent(this, SettingActivity::class.java)
            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        val settingPendingIntent = PendingIntent.getActivity(this, 0, settingIntent, 0)
        val notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification_icon)
            .setContentTitle("Break reminders are active")
            .setContentText("Click here to modify your preferences")
            .setContentIntent(settingPendingIntent)
            .setSound(notificationSound)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        val intentFilter = IntentFilter()
        intentFilter.addAction(Intent.ACTION_USER_PRESENT)
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF)
        intentFilter.addAction(Intent.ACTION_SHUTDOWN)

        startTimer(workDuration)
        createNotificationChannel()
        registerReceiver(alarmReceiver, intentFilter)
        startForeground(1, notification)
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent): IBinder? = null

    /**
     * Used to stop the timer and unregister the receiver
     */
    override fun onDestroy() {
        super.onDestroy()

        editor.putBoolean("break_enabled", false)
        editor.apply()

        stopTimer()
        unregisterReceiver(alarmReceiver)
    }

    /**
     * Start the timer for the breaks
     * @param endOfTimerInMillis duration of the timer
     */
    private fun startTimer(endOfTimerInMillis: Long) {
        timer = object : CountDownTimer(endOfTimerInMillis, 1000) {
            /**
             * Used to keep track of the remaining milliseconds left
             */
            override fun onTick(millisUntilFinished: Long) {
                remainingMillis = millisUntilFinished
            }

            /**
             * Used to start the rest activity and restart the timer
             */
            override fun onFinish() {
                val restIntent = Intent(this@BreakService, RestActivity::class.java)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)

                startActivity(restIntent)
                startTimer(workDuration + breakDuration)
            }
        }

        timer.start()
    }

    /**
     * Used to stop the timer
     */
    private fun stopTimer() {
        timer.cancel()
    }

    /**
     * Creates the notification channel for the foreground service
     */
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= 27) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                "2020 Notification Channel",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }
}
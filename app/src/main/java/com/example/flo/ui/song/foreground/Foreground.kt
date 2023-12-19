package com.example.flo.ui.song.foreground

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.annotation.IntRange
import androidx.core.app.NotificationCompat
import com.example.flo.R
import com.example.flo.ui.song.SongActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Foreground : Service() {
    var job :Job? = null
    val CHANNEL_ID = "FLO111"
    val NOTI_ID = 153
    val notification = null
    lateinit var manager: NotificationManager
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(CHANNEL_ID, "FOREGROUND", NotificationManager.IMPORTANCE_LOW)
            manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotificationChannel()
        startForeground(NOTI_ID, createCountProgressBar(0))

        val scope = CoroutineScope(Dispatchers.Main)
        job = CoroutineScope(Dispatchers.Main).launch {
            for (i in 1..100) {
                updateProgress(i)
                delay(200)
            }
            stopSelf()
        }

        return super.onStartCommand(intent, flags, startId)
    }

    private fun createCountProgressBar(@IntRange(from = 0L, to = 100L) progress : Int) =
        NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("FLO")
            .setSmallIcon(R.drawable.icon_flo)
            .setContentText("count : " + progress)
            .setContentIntent(createPendingIntent())
            .setAutoCancel(true)
            .setProgress(100, progress, false)
            .build()
    private fun createPendingIntent(): PendingIntent {
        val intent = Intent(this, SongActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        return PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
    }
    private fun updateProgress(@IntRange(from = 0L, to = 100L) progress : Int) {
        manager.notify(NOTI_ID, createCountProgressBar(progress))
    }
    override fun onBind(intent: Intent): IBinder? {
        return Binder()
    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
        manager.cancel(NOTI_ID)
    }
}
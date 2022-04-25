package com.example.musicplayerapp.service

import android.app.*
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.IBinder
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.musicplayerapp.MainActivity

class MediaPlayerService: Service(){

    private lateinit var mediaPlayerMyata: MediaPlayer

    var isRunning = false
    var streamType = MutableLiveData<String?>()


    override fun onBind(p0: Intent?): IBinder? {
       return null
    }

    override fun onCreate() {
        super.onCreate()

        initMusic()
        isRunning = true
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        showNotification()
        streamType.value = intent?.getStringExtra("DATA")

        if(mediaPlayerMyata.isPlaying)
            mediaPlayerMyata.pause()
        mediaPlayerMyata.reset()
        mediaPlayerMyata.setDataSource(this, Uri.parse(
            "https://radio-node-6.dline-media.com/${streamType.value}")
        )

        mediaPlayerMyata.prepare()
        mediaPlayerMyata.setOnPreparedListener{
            mediaPlayerMyata.start()
        }

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        isRunning = false
    }


    private fun showNotification(){
        val notificationIntent = Intent(this, MainActivity::class.java)

        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)

        val notification = Notification
            .Builder(this, "1")
            .setContentText("Myata radio")
            .setContentIntent(pendingIntent)
            .build()

        startForeground(2, notification)
    }


    private fun createNotificationChannel(){

        val serviceChannel = NotificationChannel(
            "1", "Stream service channel",
            NotificationManager.IMPORTANCE_LOW
        )

        val manager = getSystemService(
            NotificationManager::class.java
        )

        manager.createNotificationChannel(serviceChannel)
    }

    private fun initMusic(){
        mediaPlayerMyata = MediaPlayer.create(this, Uri.parse("https://radio-node-6.dline-media.com/myata"))
    }
}
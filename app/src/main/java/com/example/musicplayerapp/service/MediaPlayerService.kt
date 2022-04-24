package com.example.musicplayerapp.service

import android.app.*
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.IBinder
import com.example.musicplayerapp.MainActivity

class MediaPlayerService: Service(){

    private lateinit var mediaPlayerMyata: MediaPlayer
    private lateinit var mediaPlayerXtra: MediaPlayer
    private lateinit var mediaPlayerGold: MediaPlayer

    var isRunning = false
    var streamType: String? = "myata"

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
        streamType = intent?.getStringExtra("DATA")
        when(streamType){
            "myata"->mediaPlayerMyata.start()
            "gold"->mediaPlayerGold.start()
            "myata_hits"->mediaPlayerXtra.start()
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
        mediaPlayerXtra = MediaPlayer.create(this, Uri.parse("https://radio-node-6.dline-media.com/myata_hits"))
        mediaPlayerGold = MediaPlayer.create(this, Uri.parse("https://radio-node-6.dline-media.com/gold"))
    }
}
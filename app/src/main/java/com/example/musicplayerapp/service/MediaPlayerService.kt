package com.example.musicplayerapp.service

import android.app.*
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.IBinder
import android.util.Log
import com.example.musicplayerapp.MainActivity

class MediaPlayerService() : Service(), MediaPlayer.OnPreparedListener{

    private lateinit var mediaPlayerMyata: MediaPlayer
    private lateinit var mediaPlayerXtra: MediaPlayer
    private lateinit var mediaPlayerGold: MediaPlayer

    var isRunning = false



    override fun onBind(p0: Intent?): IBinder? {
        Log.e("SERVICE", "ON BIND")
       return null
    }


    override fun onCreate() {
        super.onCreate()
        initMusic()
        isRunning = false
        createNotificationChannel()
    }

    override fun onPrepared(p0: MediaPlayer?) {

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        showNotification()
        Log.e("SERVICE", "ONSCMD")
        when(intent?.getStringExtra("STREAM")){
            "myata"-> {playerHandler(mediaPlayerMyata)}
            "gold"->{ playerHandler(mediaPlayerGold)}
            "myata_hits"->{ playerHandler(mediaPlayerXtra)}
        }

        return START_STICKY
    }

    private fun playerHandler(mediaPlayer: MediaPlayer){

        if(mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            Log.e("OK", "stop")
        }
        else{
            mediaPlayer.start()
//            mediaPlayer.setOnPreparedListener {
//                Log.e("OK", "Start")
//                it.start()
//            }
        }
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
        Log.e("SERVICE", "CREATE START")
        mediaPlayerMyata = MediaPlayer.create(this, Uri.parse(
            "https://radio-node-6.dline-media.com/myata"))
        mediaPlayerXtra = MediaPlayer.create(this, Uri.parse(
            "https://radio-node-6.dline-media.com/myata_hits"))
        mediaPlayerGold = MediaPlayer.create(this, Uri.parse(
            "https://radio-node-6.dline-media.com/gold"))

        mediaPlayerGold.reset()
        mediaPlayerMyata.reset()
        mediaPlayerXtra.reset()

        mediaPlayerMyata.setDataSource(this, Uri.parse(
            "https://radio-node-6.dline-media.com/myata"))
        mediaPlayerGold.setDataSource(this, Uri.parse(
            "https://radio-node-6.dline-media.com/gold"))
        mediaPlayerXtra.setDataSource(this, Uri.parse(
            "https://radio-node-6.dline-media.com/myata_hits"))

        mediaPlayerGold.prepareAsync()
        mediaPlayerMyata.prepareAsync()
        mediaPlayerXtra.prepareAsync()

        Log.e("SERVICE", "CREATED")
    }
}

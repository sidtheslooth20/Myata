package com.example.musicplayerapp.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.ui.PlayerNotificationManager


class MediaPlayerService(): Service(){

    private lateinit var exoPlayer: ExoPlayer
    val myataItem = MediaItem.fromUri("https://radio-node-6.dline-media.com/myata")
    val xtraItem = MediaItem.fromUri("https://radio-node-6.dline-media.com/myata_hits")
    val goldItem = MediaItem.fromUri("https://radio-node-6.dline-media.com/gold")

    var playerNotificationManager: PlayerNotificationManager? = null
    var song: String = ""
    var artist: String = ""


    val mediaDescriptionAdapter = object: PlayerNotificationManager.MediaDescriptionAdapter{
        override fun getCurrentContentTitle(player: Player): CharSequence {
            return song
        }

        override fun createCurrentContentIntent(player: Player): PendingIntent? {
            return null
        }

        override fun getCurrentContentText(player: Player): CharSequence? {
            return artist
        }

        override fun getCurrentSubText(player: Player): CharSequence? {
            return super.getCurrentSubText(player)
        }

        override fun getCurrentLargeIcon(
            player: Player,
            callback: PlayerNotificationManager.BitmapCallback
        ): Bitmap? {
            return null
        }
    }


    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        when(intent?.getStringExtra("ACTION")){
            "startStop"->{
                if(exoPlayer.isPlaying)
                    exoPlayer.pause()
                else{
                    when(intent.getStringExtra("STREAM")){
                        "myata"->{exoPlayer.setMediaItem(myataItem)}
                        "gold"->{exoPlayer.setMediaItem(goldItem)}
                        "myata_hits"->{exoPlayer.setMediaItem(xtraItem)}
                    }
                    exoPlayer.prepare()
                    exoPlayer.play()
                }
            }
            "switch"->{
                when(intent.getStringExtra("STREAM")){
                    "myata"->{exoPlayer.setMediaItem(myataItem)}
                    "gold"->{exoPlayer.setMediaItem(goldItem)}
                    "myata_hits"->{exoPlayer.setMediaItem(xtraItem)}
                }
                song = intent.getStringExtra("SONG")!!
                artist = intent.getStringExtra("ARTIST")!!
            }
            "switch_track"->{
                song = intent.getStringExtra("SONG")!!
                artist = intent.getStringExtra("ARTIST")!!
            }
        }

        return START_NOT_STICKY
    }

    override fun onCreate() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notification: Notification = Notification.Builder(this, createNotificationChannel("307","307"))
                .build()
            startForeground(307, notification)
        } else {
            val notification: Notification = Notification.Builder(this)
                .build()
            startForeground(307, notification)
        }

        Log.e("Service","Create")
        exoPlayer = ExoPlayer.Builder(this).build().apply {
            addListener(object: Player.Listener{

                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    super.onIsPlayingChanged(isPlaying)
                    val intent = Intent("play_pause").apply {
                    }

                    LocalBroadcastManager.getInstance(this@MediaPlayerService)
                        .sendBroadcast(intent)
                }
            })
        }

        playerNotificationManager = PlayerNotificationManager.Builder(
            this, 307, "307")
            .setMediaDescriptionAdapter(mediaDescriptionAdapter)
            .build()

        playerNotificationManager!!.setPlayer(exoPlayer)

        super.onCreate()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(channelId: String, channelName: String): String{
        val chan = NotificationChannel(channelId,
            channelName, NotificationManager.IMPORTANCE_LOW)

        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(chan)
        return channelId
    }

    override fun onDestroy() {

        playerNotificationManager?.setPlayer(null)
        stopForeground(true)
        stopSelf()
        Log.e("Service","Stopped")
        exoPlayer.release()
        super.onDestroy()
    }
}
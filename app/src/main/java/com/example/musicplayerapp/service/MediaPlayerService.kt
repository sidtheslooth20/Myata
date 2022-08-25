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
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerNotificationManager


class MediaPlayerService(): Service(){

    private lateinit var exoPlayer: ExoPlayer
    val myataItem = MediaItem.fromUri("https://radio-node-6.dline-media.com/myata")
    val xtraItem = MediaItem.fromUri("https://radio-node-6.dline-media.com/myata_hits")
    val goldItem = MediaItem.fromUri("https://radio-node-6.dline-media.com/gold")

    var playerNotificationManager: PlayerNotificationManager? = null
    var song: String = ""
    var artist: String = ""
    var stream: String = ""
    lateinit var notification: Notification


    val mediaDescriptionAdapter = object: PlayerNotificationManager.MediaDescriptionAdapter{
        override fun getCurrentContentTitle(player: Player): CharSequence {
            return artist
        }

        override fun createCurrentContentIntent(player: Player): PendingIntent? {
            return null
        }

        override fun getCurrentContentText(player: Player): CharSequence? {
            return song
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

        super.onStartCommand(intent, flags, startId)

        if(intent != null) {
            when(intent?.getStringExtra("ACTION")){
                "startStop"->{
                    if(exoPlayer.isPlaying)
                        exoPlayer.pause()
                    else{
                        if (stream != intent.getStringExtra("STREAM")!!)
                        {
                            stream = intent.getStringExtra("STREAM")!!
                            when(stream){
                                "myata"->{exoPlayer.setMediaItem(myataItem)}
                                "gold"->{exoPlayer.setMediaItem(goldItem)}
                                "myata_hits"->{exoPlayer.setMediaItem(xtraItem)}
                            }
                        }
                        exoPlayer.prepare()
                        exoPlayer.play()
                    }
                }
                "switch"->{
                    if (stream != intent.getStringExtra("STREAM")!!)
                    {
                        stream = intent.getStringExtra("STREAM")!!
                        when(stream){
                            "myata"->{exoPlayer.setMediaItem(myataItem)}
                            "gold"->{exoPlayer.setMediaItem(goldItem)}
                            "myata_hits"->{exoPlayer.setMediaItem(xtraItem)}
                        }
                    }
                    song = intent.getStringExtra("SONG")!!
                    artist = intent.getStringExtra("ARTIST")!!
                }

                "switch_track"->{
                    song = intent.getStringExtra("SONG")!!
                    artist = intent.getStringExtra("ARTIST")!!
                    if(exoPlayer.isPlaying){
                        exoPlayer.pause()
                        exoPlayer.play()
                    }
                    else{
                        exoPlayer.pause()
                    }

                    Log.e("SWITCH",song)
                }
            }
        }

        return START_STICKY
    }

    override fun onCreate() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification = Notification.Builder(this, createNotificationChannel("307","307"))
                .build()
            startForeground(307, notification)
        } else {
            notification = Notification.Builder(this)
                .build()
            startForeground(307, notification)
        }

        Log.e("Service","Create")

        exoPlayer = ExoPlayer.Builder(this).build().apply {
            addListener(object: Player.Listener{
                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    super.onIsPlayingChanged(isPlaying)
                    val action = if(isPlaying) "play" else "pause"
                    val intent = Intent(action).apply {
                    }
                    LocalBroadcastManager.getInstance(this@MediaPlayerService)
                        .sendBroadcast(intent)
                }
            })
        }

        val notificationListener: PlayerNotificationManager.NotificationListener =
            object : PlayerNotificationManager.NotificationListener {

                override fun onNotificationCancelled(
                    notificationId: Int,
                    dismissedByUser: Boolean
                ) {
                    Log.d("DISMISS","onNotificationCancelled dismissedByUser $dismissedByUser")
                    stopSelf()
                }

                override fun onNotificationPosted(
                    notificationId: Int,
                    notification: Notification,
                    ongoing: Boolean
                ) {
                    if(ongoing){
                        startForeground(notificationId, notification)
                    }
                    else{
                        stopForeground(false)
                    }
                }
            }


        playerNotificationManager = PlayerNotificationManager.Builder(
            this, 307, "307")
            .setNotificationListener(notificationListener)
            .setMediaDescriptionAdapter(mediaDescriptionAdapter)
            .build()

        playerNotificationManager!!.setPlayer(exoPlayer)

        playerNotificationManager?.setUseStopAction(true)
        super.onCreate()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(channelId: String, channelName: String): String{
        val chan = NotificationChannel(channelId,
            channelName, NotificationManager.IMPORTANCE_NONE)
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(chan)
        return channelId
    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(this@MediaPlayerService)
            .sendBroadcast(Intent("Dismiss").apply {})
        playerNotificationManager?.setPlayer(null)
        stopForeground(true)
        stopSelf()
        Log.e("Service","Stopped")
        exoPlayer.release()
        super.onDestroy()
    }
}
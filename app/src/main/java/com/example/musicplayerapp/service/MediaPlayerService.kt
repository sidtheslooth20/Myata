package com.example.musicplayerapp.service

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.Bitmap
import android.os.IBinder
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

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onCreate() {
        exoPlayer = ExoPlayer.Builder(this).build()

        playerNotificationManager = PlayerNotificationManager.Builder(
            this, 1, "1")
            .setChannelNameResourceId(R.string.exo_download_notification_channel_name)
            .setMediaDescriptionAdapter(mediaDescriptionAdapter)
            .build()

        playerNotificationManager!!.setPlayer(exoPlayer)
        super.onCreate()
    }
}
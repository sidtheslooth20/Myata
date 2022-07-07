package com.example.musicplayerapp.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.google.android.exoplayer2.*


class MediaPlayerService(): Service(){

    private lateinit var exoPlayer: ExoPlayer
    val myataItem = MediaItem.fromUri("https://radio-node-6.dline-media.com/myata")
    val xtraItem = MediaItem.fromUri("https://radio-node-6.dline-media.com/myata_hits")
    val goldItem = MediaItem.fromUri("https://radio-node-6.dline-media.com/gold")

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
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onCreate() {

        exoPlayer = ExoPlayer.Builder(this).build()

        super.onCreate()
    }

}
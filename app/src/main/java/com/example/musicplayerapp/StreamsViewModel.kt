package com.example.musicplayerapp

import android.app.Application
import android.media.MediaPlayer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.musicplayerapp.service.MediaPlayerService
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.ContextCompat.startForegroundService


class StreamsViewModel(app: Application):AndroidViewModel(app) {

    var currentSongLive = MutableLiveData<String?>()
    var currentAuthorLive = MutableLiveData<String?>()
    var songLayoutWeight = 0F
    var currentStream = MutableLiveData<String?>()


    private fun startStopService(context: Context){
        startForegroundService(context, Intent(context,MainActivity::class.java))
    }


    fun play_stream(context: Context){
//        when(currentStream){
//            "myata" -> mediaPlayerMyata?.start()
//            "myata_hits" -> mediaPlayerXtra?.start()
//            "gold" -> mediaPlayerGold?.start()
//        }
        startStopService(context)
    }

    fun setSongLayoutWeight(weight: Float): Float{
        songLayoutWeight = weight
        return weight
    }
}
package com.example.musicplayerapp

import android.app.Application
import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StreamsViewModel(app: Application):AndroidViewModel(app) {

    var currentSongLive = MutableLiveData<String?>()
    var currentAuthorLive = MutableLiveData<String?>()
    var mediaPlayerMyata: MediaPlayer? = null
    var mediaPlayerGold: MediaPlayer? = null
    var mediaPlayerXtra: MediaPlayer? = null
    var songLayoutWeight = 0F
    var currentStream = "myata"

    fun setup_stream(context: Context?){
        Log.d("TAG","https://radio-node-6.dline-media.com/${currentStream}")
        mediaPlayerMyata = MediaPlayer.create(context, Uri.parse("https://radio-node-6.dline-media.com/myata"))
        mediaPlayerXtra = MediaPlayer.create(context, Uri.parse("https://radio-node-6.dline-media.com/myata_hits"))
        mediaPlayerGold = MediaPlayer.create(context, Uri.parse("https://radio-node-6.dline-media.com/gold"))
    }

    fun play_stream(){
        when(currentStream){
            "myata" -> mediaPlayerMyata?.start()
            "myata_hits" -> mediaPlayerXtra?.start()
            "gold" -> mediaPlayerGold?.start()
        }
    }

    fun setSongLayoutWeight(weight: Float): Float{
        songLayoutWeight = weight
        return weight
    }
}
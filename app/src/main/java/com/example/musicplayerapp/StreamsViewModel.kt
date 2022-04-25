package com.example.musicplayerapp

import android.app.Application
import android.content.Intent
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.musicplayerapp.service.MediaPlayerService
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.lang.Error
import java.net.URL


class StreamsViewModel(app: Application):AndroidViewModel(app) {

    var currentSongLive = MutableLiveData<String?>()
    var currentAuthorLive = MutableLiveData<String?>()
    var songLayoutWeight = 0F
    var currentStreamLive = MutableLiveData<String?>()
    private val client = OkHttpClient()


    fun getStreamJson() = viewModelScope.launch {
        parseJson()
    }

    suspend fun parseJson() = withContext(Dispatchers.IO){

        while (true) {
            val gson = Gson()
            try{
                val request = Request.Builder()
                    .url("https://radio.dline-media.com/mounts.json")
                    .header("Connection", "close")
                    .build()

                client.newCall(request).execute().use { response->
                    if(!response.isSuccessful) throw IOException("Unexpected code $response")
                    val streamInfo =
                        gson.fromJson(response.body()?.string(), Map::class.java).get("/gold") as Map<String, String?>
                    val songArtist = streamInfo.get("now_playing")?.split("- ")

                    currentSongLive.postValue(songArtist?.get(1))
                    currentAuthorLive.postValue(songArtist?.get(0))
                }
            }
            catch (e: Exception){
                Log.e("ERROR", "ERROR")
                delay(1000)
                continue
            }
            delay(5000)
        }
    }


    fun setSongLayoutWeight(weight: Float): Float{
        songLayoutWeight = weight
        return weight
    }
}
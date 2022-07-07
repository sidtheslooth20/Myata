package com.example.musicplayerapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException


class StreamsViewModel(app: Application):AndroidViewModel(app) {


    var currentSongLive = MutableLiveData<String?>()
    var currentAuthorLive = MutableLiveData<String?>()
    var songLayoutWeight = 0F
    var currentStreamLive = MutableLiveData<String?>()
    var currentImgLinkLive = MutableLiveData<String?>()
    private val client = OkHttpClient()


    fun getStreamJson() = viewModelScope.launch {
        parseJson()
    }

    suspend fun parseJson() = withContext(Dispatchers.IO){

        while (true) {
            val gson = Gson()
            try{
                var request = Request.Builder()
                    .url("https://radio.dline-media.com/mounts.json")
                    .header("Connection", "close")
                    .build()

                client.newCall(request).execute().use { response->
                    if(!response.isSuccessful) throw IOException("Unexpected code $response")

                    val streamInfo =
                        gson.fromJson(response.body()?.string(), Map::class.java).get("/${currentStreamLive.value}") as Map<String, String?>
                    val songArtist = streamInfo.get("now_playing")?.split("- ")

                    if(currentSongLive.value != songArtist?.get(1)) {
                        currentSongLive.postValue(songArtist?.get(1))
                        currentAuthorLive.postValue(songArtist?.get(0))
                    }

                }

                request = Request.Builder()
                    .url("https://www.last.fm/music/${currentAuthorLive.value?.replace(' ','+')}/+images")
                    .build()

            }
            catch (e: Exception){
                delay(1000)
                continue
            }
            delay(5000)
        }
    }

    fun startService(){

    }


    fun setSongLayoutWeight(weight: Float): Float{
        songLayoutWeight = weight
        return weight
    }
}
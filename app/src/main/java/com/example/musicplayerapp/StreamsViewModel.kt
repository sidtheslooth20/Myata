package com.example.musicplayerapp

import android.app.Application
import android.util.Log
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
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import java.io.IOException


class StreamsViewModel(app: Application):AndroidViewModel(app) {


    var currentSongLive = MutableLiveData<String?>()
    var currentAuthorLive = MutableLiveData<String?>()
    var isPlaying = false
    var currentStreamLive = MutableLiveData<String?>()
    var currentImgLinkLive = MutableLiveData<String?>()
    private val client = OkHttpClient()


    fun getStreamJson() = viewModelScope.launch {
        parseJson()
    }

    fun getImage() = viewModelScope.launch {
        getArtistImage()
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
                        gson.fromJson(response.body()?.string(), Map::class.java).get("/${currentStreamLive.value}") as Map<String, String?>
                    val songArtist = streamInfo.get("now_playing")?.split("- ")

                    if(currentSongLive.value != songArtist?.get(1)) {
                        currentAuthorLive.postValue(songArtist?.get(0))
                        currentSongLive.postValue(songArtist?.get(1))
                    }
                }
            }
            catch (e: Exception){
                delay(1000)
                continue
            }
            delay(5000)
        }
    }

    suspend fun getArtistImage() = withContext(Dispatchers.IO){
        try {
            val doc: org.jsoup.nodes.Document = Jsoup.connect(
                "https://last.fm/music/${
                    currentAuthorLive.value
                        !!.trim().replace("/","%2F").replace(" ft."," feat.").replace(" ", "+")
                }/+images"
            ).get()

            val elements: Elements = doc.select("ul[class=image-list]")
            currentImgLinkLive.postValue(elements.select("a[class=image-list-item]")
                .select("img").attr("src"))

        } catch (e:IOException){
            Log.e("IOException", "https://last.fm/music/${
                currentAuthorLive.value
                !!.trim().replace("/","%2F").replace(" ft."," feat").replace(" ", "+")
            }/+images")
            currentImgLinkLive.postValue(null)
        }
    }
}
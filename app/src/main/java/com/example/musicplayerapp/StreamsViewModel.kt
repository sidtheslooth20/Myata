package com.example.musicplayerapp

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import com.example.musicplayerapp.retrofitthings.Retrofitinstance
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.Cache.Companion.varyHeaders
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import java.io.IOException


class StreamsViewModel(app: Application):AndroidViewModel(app) {

    var currentMyataState = MutableLiveData<PlayerState?>()
    var currentGoldState = MutableLiveData<PlayerState?>()
    var currentXtraState = MutableLiveData<PlayerState?>()
    var isPlaying = MutableLiveData<Boolean>()
    var playlistList = MutableLiveData<MutableList<YandexPlaylist>>()
    var currentStreamLive = MutableLiveData<String?>()
    private val context = getApplication<Application>().applicationContext


    init {
        isPlaying.value = false

        val receiver = PlayPauseBroadcastReceiver()
        context?.let {
            LocalBroadcastManager.getInstance(it).registerReceiver(receiver,
                IntentFilter("play_pause")
            )
        }

        getStreamJson()
        getPlaylists()
        currentMyataState.value = PlayerState("YOU ARE LISTENING", "RADIO MYATA", null)
        currentGoldState.value = PlayerState("YOU ARE LISTENING", "RADIO MYATA", null)
        currentXtraState.value = PlayerState("YOU ARE LISTENING", "RADIO MYATA", null)
    }

    fun getPlaylists() = viewModelScope.launch {
        requestYandex()
    }

    suspend fun requestYandex() = withContext(Dispatchers.IO){
        val bufferList: MutableList<YandexPlaylist> = mutableListOf()
        val gson = Gson()
        val client = OkHttpClient.Builder().followRedirects(false).followSslRedirects(false).build()
        val request = Request.Builder()
            .url("https://api.music.yandex.net/users/662251307/playlists/list")
            .header("Accept", "application/json")
            .header("Content-Type", "application/x-www-form-urlencoded")
            .build()

        client.newCall(request).execute().use { response->
            if(!response.isSuccessful) throw IOException("Unexpected code $response")

            val playlist_list = gson.fromJson(response.body?.string(), Map::class.java).get("result") as List<Map<String, Any>>

            for (i in playlist_list){
                bufferList.add(YandexPlaylist(
                    "https://music.yandex.ru/users/shaldin.voice/playlists/${i.get("kind").toString().replace(".0","")}",
                    Uri.parse("https://"+(i.get("cover") as Map<String, Any>).get("uri").toString().replace("%%","400x400"))))
               }
            playlistList.postValue(bufferList)
            Log.e("Count", playlist_list.size.toString())
        }
    }

    fun getStreamJson() = viewModelScope.launch {
        parseJson()
    }

    suspend fun parseJson() = withContext(Dispatchers.IO){

        val client = OkHttpClient.Builder().build()
        while (true) {
            val gson = Gson()
            try{
                val request = Request.Builder()
                    .url("https://radio.dline-media.com/mounts.json")
                    .header("Connection", "close")
                    .build()

                client.newCall(request).execute().use { response->
                    if(!response.isSuccessful) throw IOException("Unexpected code $response")
                    val streamInfo = gson.fromJson(response.body?.string(), Map::class.java)

                    val streamMyataInfo = streamInfo.get("/myata") as Map<String, String?>
                    var songArtist = streamMyataInfo.get("now_playing")?.split("- ")
                    if(currentMyataState.value?.song != songArtist?.get(1)) {
                        try {
                            val doc: org.jsoup.nodes.Document = Jsoup.connect(
                                "https://last.fm/music/${
                                    songArtist?.get(0)?.split(" ft.")?.get(0)
                                    !!.trim().replace("/", "%2F").replace(" ft.", " feat.")
                                        .replace(" ", "+")
                                }/+images"
                            ).get()
                            Log.e("ft", "https://last.fm/music/${
                                songArtist?.get(0)?.split(" ft.")?.get(0)
                                !!.trim().replace("/", "%2F").replace(" ft.", " feat.")
                                    .replace(" ", "+")
                            }/+images")

                            val elements: Elements = doc.select("ul[class=image-list]")
                            currentMyataState.postValue(
                                PlayerState(
                                    songArtist?.get(0), songArtist?.get(1),
                                        elements.select("a[class=image-list-item]")
                                            .select("img").attr("src")
                                )
                            )
                        }
                        catch (ex: IOException){
                            currentMyataState.postValue(
                                PlayerState(
                                    songArtist?.get(0), songArtist?.get(1), null
                                )
                            )
                        }
                    }
                    val streamGoldInfo = streamInfo.get("/gold") as Map<String, String?>
                    songArtist = streamGoldInfo.get("now_playing")?.split("- ")
                    if(currentGoldState.value?.song != songArtist?.get(1)) {
                        try {
                            val doc: org.jsoup.nodes.Document = Jsoup.connect(
                                "https://last.fm/music/${
                                    songArtist?.get(0)?.split(" ft.")?.get(0)
                                    !!.trim().replace("/", "%2F")
                                        .replace(" ", "+")
                                }/+images"
                            ).get()
                            Log.e("ft", "https://last.fm/music/${
                                songArtist?.get(0)?.split(" ft.")?.get(0)
                                !!.trim().replace("/", "%2F").replace(" ft.", " feat.")
                                    .replace(" ", "+")
                            }/+images")

                            val elements: Elements = doc.select("ul[class=image-list]")
                            currentGoldState.postValue(
                                PlayerState(
                                    songArtist?.get(0), songArtist?.get(1),
                                        elements.select("a[class=image-list-item]")
                                            .select("img").attr("src")
                                )
                            )
                        }
                        catch (ex: IOException){
                            currentGoldState.postValue(
                                PlayerState(
                                    songArtist?.get(0), songArtist?.get(1), null
                                )
                            )
                        }
                    }
                    val streamXtraInfo = streamInfo.get("/myata_hits") as Map<String, String?>
                    songArtist = streamXtraInfo.get("now_playing")?.split("- ")
                    if(currentXtraState.value?.song != songArtist?.get(1)) {
                        try {
                            val doc: org.jsoup.nodes.Document = Jsoup.connect(
                                "https://last.fm/music/${
                                    songArtist?.get(0)?.split(" ft.")?.get(0)
                                    !!.trim().replace("/", "%2F").replace(" ft.", " feat.")
                                        .replace(" ", "+")
                                }/+images"
                            ).get()
                            Log.e("ft", "https://last.fm/music/${
                                songArtist?.get(0)?.split(" ft.")?.get(0)
                                !!.trim().replace("/", "%2F").replace(" ft.", " feat.")
                                    .replace(" ", "+")
                            }/+images")

                            val elements: Elements = doc.select("ul[class=image-list]")
                            currentXtraState.postValue(
                                PlayerState(
                                    songArtist?.get(0), songArtist?.get(1),
                                        elements.select("a[class=image-list-item]")
                                            .select("img").attr("src")
                                )
                            )
                        }
                        catch (ex: IOException){
                            currentXtraState.postValue(
                                PlayerState(
                                    songArtist?.get(0), songArtist?.get(1), null
                                    )
                            )
                        }
                    }
                }
            }
            catch (e: Exception){
                delay(3000)
                Log.d("e", e.toString())
                Log.e("VM", currentMyataState.value?.song.toString())
                Log.e("VM", currentMyataState.value?.artist.toString())
                Log.e("VM", currentMyataState.value?.img.toString())
                Log.e("VM2", currentGoldState.value?.song.toString())
                Log.e("VM2", currentGoldState.value?.artist.toString())
                Log.e("VM2", currentGoldState.value?.img.toString())
                Log.e("VM3", currentXtraState.value?.song.toString())
                Log.e("VM3", currentXtraState.value?.artist.toString())
                Log.e("VM3", currentXtraState.value?.img.toString())
                continue
            }
            delay(5000)
        }
    }

    class YandexPlaylist(uri: String, img: Uri){
        val uri = uri
        val img = img
    }

    class PlayerState(artist: String?, song: String?, img: String?){
        var artist = artist
        var song = song
        var img = img
    }

    inner class PlayPauseBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            isPlaying.value = !isPlaying.value!!
        }
    }
}
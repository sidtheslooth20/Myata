package com.example.musicplayerapp

import android.annotation.SuppressLint
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
import com.example.musicplayerapp.service.MediaPlayerService
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.*
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection


class StreamsViewModel(app: Application):AndroidViewModel(app) {

    var currentMyataState = MutableLiveData<PlayerState?>()
    var currentGoldState = MutableLiveData<PlayerState?>()
    var currentXtraState = MutableLiveData<PlayerState?>()
    var isPlaying = MutableLiveData<Boolean>()
    var isInSplitMode = MutableLiveData<Boolean>()
    var playlistList = MutableLiveData<MutableList<MyataPlaylist>>()
    var currentStreamLive = MutableLiveData<String?>()
    var currentFragmentLiveData = MutableLiveData<String>()
    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext
    var isUIActive = true

    //problem why we need this is service cannot launch fragment, it can only recreate activity
    var ifNeedToNavigateStraightToPlayer = false
    //To avoid reaction on swich stream pause
    var ifNeedToListenReciever = true


    init {
        isPlaying.value = false
        isInSplitMode.value = false
        currentStreamLive.value = "myata"

        val receiver1 = PlayPauseBroadcastReceiver()
        context?.let {
            LocalBroadcastManager.getInstance(it).registerReceiver(receiver1,
                IntentFilter("play")
            )
        }

        val receiver2 = PlayPauseBroadcastReceiver()
        context?.let {
            LocalBroadcastManager.getInstance(it).registerReceiver(receiver2,
                IntentFilter("pause")
            )
        }

        getStreamJson()
        getPlaylists()
        currentMyataState.value = PlayerState("YOU ARE LISTENING", "RADIO MYATA", null)
        currentGoldState.value = PlayerState("YOU ARE LISTENING", "RADIO MYATA", null)
        currentXtraState.value = PlayerState("YOU ARE LISTENING", "RADIO MYATA", null)
    }

    fun getPlaylists() = viewModelScope.launch {
        while (true){
            try{
                requestMyata()
                break
            }
            catch (e:Exception){
                Log.e("Exception: ",e.toString())
                continue
            }
        }
    }

    suspend fun requestMyata() = withContext(Dispatchers.IO){

        try {
            val url = URL("https://radiomyata.ru/covers/playlists.txt")

            val connection: HttpURLConnection = url.openConnection() as HttpsURLConnection
            val br = BufferedReader(InputStreamReader(connection.getInputStream()))
            var lines: MutableList<MyataPlaylist> = mutableListOf()
            val wholeText = br.readText().split("\n\n","\r\r")
            br.close()
            Log.d("LINE", wholeText.toString())
            for (str in wholeText){
                Log.d("SOSI", str.split(" — ", " - ")[0])
                if(!str.isBlank())
                    lines.add(MyataPlaylist(str.split(" — ", " - ")[1].trim(' '),
                        Uri.parse(str.split(" — ", " - ")[0].trim(' '))))
            }
            Log.d("LINES", lines.toString())
            playlistList.postValue(lines)
        } catch (e: IOException) {
            Log.e("IOexception", "Myata request exception: " + e.getLocalizedMessage())
            e.printStackTrace()
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
                    .url("https://drh-api.dline-media.com/api/statistics?filter[organization_id]=7")
                    .header("Connection", "close")
                    .build()

                client.newCall(request).execute().use { response->
                    if(!response.isSuccessful) throw IOException("Unexpected code $response")
                    val streamInfo = gson.fromJson(response.body?.string(), Map::class.java)
                    val streamDataInfo = streamInfo.get("data") as List<Map<String,Any>>
                    var streamsData = streamDataInfo[0].get("streams") as List<Map<String, Any>>
                    var songData = streamsData[0].get("last_song") as Map<String,String>
                    var songArtist = songData.get("title").toString().split("-")
                    if(currentMyataState.value?.song != songArtist?.get(1)) {
                        if(currentStreamLive.value == "myata"){
                            if(currentMyataState.value?.song != null){
                                context.startService(Intent(
                                    context,
                                    MediaPlayerService::class.java
                                ).also {
                                    it.putExtra("ACTION", "switch_track")
                                    it.putExtra("SONG", songArtist?.get(1))
                                    it.putExtra("ARTIST", songArtist?.get(0))
                                })

                                currentMyataState.postValue(
                                    PlayerState(
                                        songArtist?.get(0), songArtist?.get(1), null)
                                )
                            }
                        }
                        try {
                            if(isUIActive){
                                val doc: org.jsoup.nodes.Document = Jsoup.connect(
                                    songArtist?.let { formUrl(it) }
                                ).get()

                                val elements: Elements = doc.select("ul[class=image-list]")
                                currentMyataState.postValue(
                                    PlayerState(
                                        songArtist?.get(0), songArtist?.get(1),
                                        elements.select("a[class=image-list-item]")
                                            .select("img").attr("src"))
                                )
                                Log.d("IMG", elements.select("a[class=image-list-item]")
                                    .select("img").attr("src"))
                            }
                        }
                        catch (ex: IOException){
                            currentMyataState.postValue(
                                PlayerState(
                                    songArtist?.get(0), songArtist?.get(1), null
                                )
                            )
                            Log.e("Exception", ex.toString())
                        }
                    }
                    streamsData = streamDataInfo[1].get("streams") as List<Map<String, Any>>
                    songData = streamsData[0].get("last_song") as Map<String,String>
                    songArtist = songData.get("title").toString().split("-")
                    if(currentGoldState.value?.song != songArtist?.get(1)) {
                        if(currentStreamLive.value == "gold"){
                            if(currentGoldState.value?.song != null){
                                context.startService(Intent(
                                    context,
                                    MediaPlayerService::class.java
                                ).also {
                                    it.putExtra("ACTION", "switch_track")
                                    it.putExtra("SONG", songArtist?.get(1))
                                    it.putExtra("ARTIST", songArtist?.get(0))
                                })

                                currentGoldState.postValue(
                                    PlayerState(
                                        songArtist?.get(0), songArtist?.get(1), null)
                                )
                            }
                        }
                        try {
                            if(isUIActive){
                                val doc: org.jsoup.nodes.Document = Jsoup.connect(
                                    songArtist?.let { formUrl(it) }
                                ).get()

                                val elements: Elements = doc.select("ul[class=image-list]")
                                currentGoldState.postValue(
                                    PlayerState(
                                        songArtist?.get(0), songArtist?.get(1),
                                        elements.select("a[class=image-list-item]")
                                            .select("img").attr("src")
                                    )
                                )
                                Log.d("IMG", elements.select("a[class=image-list-item]")
                                    .select("img").attr("src"))
                            }
                        }
                        catch (ex: IOException){
                            currentGoldState.postValue(
                                PlayerState(
                                    songArtist?.get(0), songArtist?.get(1), null
                                )
                            )
                            Log.e("Exception", ex.toString())
                        }
                    }
                    streamsData = streamDataInfo[2].get("streams") as List<Map<String, Any>>
                    songData = streamsData[0].get("last_song") as Map<String,String>
                    songArtist = songData.get("title").toString().split("-")
                    if(currentXtraState.value?.song != songArtist?.get(1)) {
                        if(currentStreamLive.value == "myata_hits"){
                            if(currentXtraState.value?.song != null){
                                context.startService(Intent(
                                    context,
                                    MediaPlayerService::class.java
                                ).also {
                                    it.putExtra("ACTION", "switch_track")
                                    it.putExtra("SONG", songArtist?.get(1))
                                    it.putExtra("ARTIST", songArtist?.get(0))
                                })

                                currentXtraState.postValue(
                                    PlayerState(
                                        songArtist?.get(0), songArtist?.get(1), null)
                                )
                            }
                        }
                        try {
                            if(isUIActive){
                                val doc: org.jsoup.nodes.Document = Jsoup.connect(
                                    songArtist?.let { formUrl(it) }
                                ).get()

                                val elements: Elements = doc.select("ul[class=image-list]")
                                currentXtraState.postValue(
                                    PlayerState(
                                        songArtist?.get(0), songArtist?.get(1),
                                        elements.select("a[class=image-list-item]")
                                            .select("img").attr("src")
                                    )
                                )
                                Log.d("IMG", elements.select("a[class=image-list-item]")
                                    .select("img").attr("src"))
                            }
                        }
                        catch (ex: IOException){
                            currentXtraState.postValue(
                                PlayerState(
                                    songArtist?.get(0), songArtist?.get(1), null
                                    )
                            )
                            Log.e("Exception", ex.toString())
                        }
                    }
                }
            }
            catch (e: Exception){
                Log.e("Exception", e.toString())
                delay(500)
                continue
            }
            delay(1000)
        }
    }

    fun formUrl(songArtist: List<String>): String{
        Log.d("URL", "https://last.fm/music/${songArtist.get(0)
            ?.lowercase()?.split(" ft.")?.get(0)!!.trim()
            .replace("/", "%2F")
            .replace(" ", "+")
        }/+images")

        return "https://last.fm/music/${songArtist.get(0)
            ?.lowercase()?.split(" ft.")?.get(0)!!.trim()
            .replace("/", "%2F")
            .replace(" ", "+")
        }/+images"

    }

    class MyataPlaylist(uri: String, img: Uri){
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
            if (intent != null) {
                if(ifNeedToListenReciever)
                    isPlaying.value = intent.action == "play"
                ifNeedToListenReciever = true
            }
        }
    }
}
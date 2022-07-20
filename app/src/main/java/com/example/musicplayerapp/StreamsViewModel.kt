package com.example.musicplayerapp

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
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


    var currentSongLive = MutableLiveData<String?>()
    var currentAuthorLive = MutableLiveData<String?>()
    var isPlaying = false
    //lateinit var playlistIds: MutableList<Int>
    var playlistList: MutableList<YandexPlaylist> = mutableListOf()
    var currentStreamLive = MutableLiveData<String?>()
    var currentImgLinkLive = MutableLiveData<String?>()
    private val client = OkHttpClient()


    fun getPlaylists() = viewModelScope.launch {
        //val playlistResponse = Retrofitinstance.api.getPlaylistsList()

        requestYandex()

    }

    suspend fun requestYandex() = withContext(Dispatchers.IO){

        val gson = Gson()
        val request = Request.Builder()
            .url("https://music.yandex.ru/handlers/library.jsx?owner=shaldin.voice&filter=playlists&likeFilter=favorite&kidsSubPage=&playlistsWithoutContent=true&likedPlaylistsPage=0&lang=ru&external-domain=music.yandex.ru&overembed=false&ncrnd=0.7012712362054048")
            .header("Accept", "application/json")
            .header("Connection", "keep-alive")
            .header("Host", "music.yandex.ru")
            .header("Sec-fetch-site", "same-origin")
            .header("Accept-Language", "ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7")
            .header("Sec-Fetch-Mode", "cors")
            .header("Sec-Fetch-Dest", "empty")
            .header("X-Current-UID", "963565570")
            .header("X-Requested-With", "XMLHttpRequest")
            .header("X-Retpath-Y", " https://music.yandex.ru/users/shaldin.voice/playlists")
            .header("sec-ch-ua", " \".Not/A)Brand\";v=\"99\", \"Google Chrome\";v=\"103\", \"Chromium\";v=\"103\"")
            .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/103.0.0.0 Safari/537.36")
            .header("sec-ch-ua-mobile", " ?0")
            .header("sec-ch-ua-platform", "\"Windows\"")
            .header("Referer", "https://music.yandex.ru/users/shaldin.voice/playlists")
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")
            val response = gson.fromJson(response.body?.string(), Map::class.java).get("playlistIds") as List<Int>

            var playlistIds = response.toString()

//                for (i in playlistIds){
//                    val request = Request.Builder()
//                        .url("https://music.yandex.ru/handlers/playlist.jsx?owner=shaldin.voice&kinds=${i.toString()}&light=true&madeFor=&withLikesCount=true&forceLogin=true&lang=ru&external-domain=music.yandex.ru&overembed=false&ncrnd=0.5244382377662269")
//                        .header("Accept", "application/json")
//                        .header("Connection", "keep-alive")
//                        .header("Host", "music.yandex.ru")
//                        .header("Sec-fetch-site", "same-origin")
//                        .header("Accept-Language", "ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7")
//                        .header("Sec-Fetch-Mode", "cors")
//                        .header("Sec-Fetch-Dest", "empty")
//                        .header("X-Current-UID", "963565570")
//                        .header("X-Requested-With", "XMLHttpRequest")
//                        .header("X-Retpath-Y", " https://music.yandex.ru/users/shaldin.voice/playlists/${i.toString()}")
//                        .header("sec-ch-ua", " \".Not/A)Brand\";v=\"99\", \"Google Chrome\";v=\"103\", \"Chromium\";v=\"103\"")
//                        .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/103.0.0.0 Safari/537.36")
//                        .header("sec-ch-ua-mobile", " ?0")
//                        .header("sec-ch-ua-platform", "\"Windows\"")
//                        .header("Referer", "https://music.yandex.ru/users/shaldin.voice/playlists/${i.toString()}")
//                        .build()
//                    Log.e("PLLST", i.toString())
//
//                    client.newCall(request).execute().use { response ->
//                        if (!response.isSuccessful) throw IOException("Unexpected code $response")
//                        else {
//                            val playlistInfo = response.body?.string()
//                            Log.d("INFO", playlistInfo.toString())
//                        }
//
//                        //playlistList.add(YandexPlaylist("","",""))
//                    }
//                }

            playlistIds = playlistIds.replace(".0", "").removePrefix("[").removeSuffix("]")

            val formBody = MultipartBody.Builder()
                .addFormDataPart("ids", playlistIds)
                .addFormDataPart("owner", "662251307")
                .addFormDataPart("withLikesCount", "true")
                .addFormDataPart("lang","ru")
                .addFormDataPart("sign", "0bec53997140ce1c76ebd7440529b24286a6a450:1658280778434")
                .addFormDataPart("experiments", "{\"ABTestIds\":\"\",\"WebDontPayPopup\":\"control\",\"WebGenerativeTab\":\"default\",\"WebInteractiveSplashscreenWithTrackTimeLimit\":\"30sec\",\"WebNewImport\":\"on\",\"WebTVMusicYnison\":\"default\",\"WebTouchReact2021\":\"on\",\"WebYMConnect\":\"default\",\"adv\":\"newMinimalBlock\",\"barBelowExperiment\":\"default\",\"boostConfigExperiment62b17ef2b96e38262e0a69a3\":\"default\",\"boostConfigExperiment62b181379f048212d1693f3e\":\"default\",\"boostConfigExperiment62b186a4b96e38262e0a69c2\":\"on\",\"boostConfigExperiment62b1ad969f048212d16942dd\":\"on\",\"boostConfigExperiment62c2e06013e44c7382e0fac9\":\"on\",\"boostConfigExperiment62d0790a01632b329f00d41d\":\"default\",\"boostConfigExperiment62d12246d72869181def93d2\":\"default\",\"boostConfigExperiment62d129a07ee63376cf113243\":\"on\",\"boostConfigExperiment62d57bb899942e132944552c\":\"on\",\"iframeNewReact\":\"default\",\"miniBrick\":\"default\",\"musicCrackdownTiming\":\"default\",\"musicMobileWebLocked\":\"default\",\"musicPrice\":\"default\",\"musicSearchRanking\":\"default\",\"playlistBoostExperiment607289c805a7dd7ae28a8b04\":\"default\",\"playlistBoostExperiment607289d405a7dd7ae28a8b07\":\"on\",\"playlistBoostExperiment62b52e604607ba1e1cbf3747\":\"default\",\"playlistBoostExperiment62bfdb364c0095096ad69fe9\":\"default\",\"playlistBoostExperiment62d42482d4a78465c8e180a4\":\"on\",\"playlistBoostExperiment62d424b2d4a78465c8e180a6\":\"on\",\"playlistBoostExperiment62d424cad4a78465c8e180a8\":\"on\",\"playlistBoostExperiment62d424e0d4a78465c8e180aa\":\"default\",\"playlistBoostExperiment62d424f7d4a78465c8e180ac\":\"on\",\"playlistBoostExperiment62d425127b708f0b4df2073f\":\"on\",\"playlistBoostExperiment62d57bb299942e132944552a\":\"on\",\"plusWeb\":\"on\",\"useHlsTracks\":\"default\",\"webAntiMusicBlockNaGlavnoi\":\"on\",\"webBannerRefrash\":\"default\",\"webBarBelowRubilnik\":\"default\",\"webChromeCast\":\"default\",\"webSidebarBanner\":\"default\",\"webStationsHeadLink\":\"default\"}")
                .addFormDataPart( "external-domain", "music.yandex.ru")
                .addFormDataPart("overembed", "false")
                .build()

            val requestList = Request.Builder()
                .url("https://music.yandex.ru/handlers/playlists-list.jsx")
                .post(formBody)
                .header("Accept", "application/json")
                .header("Connection", "keep-alive")
                .header("Host", "music.yandex.ru")
                .header("Sec-fetch-site", "same-origin")
                .header("Accept-Language", "ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7")
                .header("Sec-Fetch-Mode", "cors")
                .header("Sec-Fetch-Dest", "empty")
                .header("X-Current-UID", "963565570")
                .header("X-Requested-With", "XMLHttpRequest")
                .header("X-Retpath-Y", " https://music.yandex.ru/users/shaldin.voice/playlists")
                .header("sec-ch-ua", " \".Not/A)Brand\";v=\"99\", \"Google Chrome\";v=\"103\", \"Chromium\";v=\"103\"")
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/103.0.0.0 Safari/537.36")
                .header("sec-ch-ua-mobile", " ?0")
                .header("sec-ch-ua-platform", "\"Windows\"")
                .header("Referer", "https://music.yandex.ru/users/shaldin.voice/playlists")
                .build()

            //empty response body
            client.newCall(requestList).execute().use { response2 ->
                if (!response2.isSuccessful) throw IOException("Unexpected code $response")
                else {
                    val resp = response2.body?.string()

                    Log.e("RESPONSE", resp.toString())
                }
            }
        }
    }

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
                        gson.fromJson(response.body?.string(), Map::class.java).get("/${currentStreamLive.value}") as Map<String, String?>
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

    class YandexPlaylist(name: String, uri: Uri, img: Uri){
        val name: String = name
        val uri: Uri = uri
        val img = img
    }
}
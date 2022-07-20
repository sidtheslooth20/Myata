package com.example.musicplayerapp.retrofitthings

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers

interface YaApi {
    @Headers(
        "Accept: application/json, text/javascript, */*",
        "Accept-Encoding: gzip, deflate, br",
        "Accept-Language:ru, en",
        "access-control-allow-methods:POST",
        "Sec-Fetch-Mode:cors",
        "X-Current-UID:userUid",
        "X-Requested-With:XMLHttpRequest",
        "X-Retpath-Y:https://music.yandex.ru/users/shaldin.voice/playlists",
        "origin:https://music.yandex.ru",
        "referer:https://music.yandex.ru/users/shaldin.voice/playlists"
    )
    @GET("/handlers/library.jsx")
    suspend fun getPlaylistsList(

    ):Response<YaResponse>
}
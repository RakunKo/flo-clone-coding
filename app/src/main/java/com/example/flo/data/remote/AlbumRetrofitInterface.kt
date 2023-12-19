package com.example.flo.data.remote

import retrofit2.Call
import retrofit2.http.GET

interface AlbumRetrofitInterface {

    @GET("/albums")
    fun getAlbumList() :Call<AlbumResponse>
}
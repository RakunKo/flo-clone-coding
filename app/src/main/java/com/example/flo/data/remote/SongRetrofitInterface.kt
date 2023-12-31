package com.example.flo.data.remote

import retrofit2.Call
import retrofit2.http.*

interface SongRetrofitInterface {
    @GET("/songs")
    fun getSongs(): Call<SongResponse>
}
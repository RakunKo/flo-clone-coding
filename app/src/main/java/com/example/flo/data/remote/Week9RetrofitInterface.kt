package com.example.flo.data.remote

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface Week9RetrofitInterface {

    @GET("/albums/{albumIdx}")
    fun getWeek9List(@Path("albumIdx") albumIdx :Int) :Call<Week9Response>
}
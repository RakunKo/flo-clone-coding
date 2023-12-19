package com.example.flo.data.remote

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchRetrofitInterface {

    @GET("/2.0")
    fun getSearchList(
        @Query("method") method : String,
        @Query("track") track : String,
        @Query("api_key") key : String,
        @Query("format") format : String
    ) :Call<SearchResponse>
}
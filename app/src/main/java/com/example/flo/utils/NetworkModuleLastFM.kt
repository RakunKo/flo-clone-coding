package com.example.flo.utils

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


fun getRetrofitLASTFM(api_key :String, search : String): Retrofit {
    val URL = "https://ws.audioscrobbler.com"
    val retrofitLASTFM = Retrofit.Builder().baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create()).build()

    return retrofitLASTFM
}
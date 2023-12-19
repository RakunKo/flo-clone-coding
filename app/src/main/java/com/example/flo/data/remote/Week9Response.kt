package com.example.flo.data.remote

import com.google.gson.annotations.SerializedName

data class Week9Response(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: ArrayList<AlbumSongL>
)


data class AlbumSongL(
    @SerializedName("songIdx") val songIdx: Int,
    @SerializedName("title") val title: String,
    @SerializedName("singer") val singer: String,
    @SerializedName("isTitleSong") val isTitleSong:String,
)
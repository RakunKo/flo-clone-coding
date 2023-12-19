package com.example.flo.data.remote

import com.google.gson.annotations.SerializedName

data class AlbumResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: AlbumList
)

data class AlbumList(
    @SerializedName("albums") val albums: List<AlbumL>
)

data class AlbumL(
    @SerializedName("albumIdx") val albumIdx: Int,
    @SerializedName("title") val title: String,
    @SerializedName("singer") val singer: String,
    @SerializedName("coverImgUrl") val coverImgUrl:String,
)
package com.example.flo.data.remote

import com.google.gson.annotations.SerializedName

data class SearchResponse(
    @SerializedName("results") val results: Results,
)

data class Results(
    @SerializedName("opensearch:Query") val query: Info,
    @SerializedName("opensearch:totalResults") val totalResults: String,
    @SerializedName("opensearch:startIndex") val startIndex: String,
    @SerializedName("opensearch:itemsPerPage") val itemsPerPage: String,
    @SerializedName("trackmatches") val trackmatches: Track,
)

data class Track(
    @SerializedName("track") val track: List<TrackInfo>
)

data class TrackInfo(
    @SerializedName("name") val name: String,
    @SerializedName("artist") val artist: String,
    @SerializedName("url") val url: String,
    @SerializedName("streamable") val streamable: String, // FIXME 값을 문자열로 변경
    @SerializedName("listeners") val listeners: String, // FIXME 값을 문자열로 변경
    @SerializedName("image") val image: List<ImageInfo>,
    @SerializedName("mbid") val mbid: String
)

data class ImageInfo(
    @SerializedName("#text") val text: String,
    @SerializedName("size") val size: String
)

data class Info(
    @SerializedName("#text") val text: String,
    @SerializedName("role") val role: String,
    @SerializedName("startPage") val startPage: String // FIXME 값을 문자열로 변경
)

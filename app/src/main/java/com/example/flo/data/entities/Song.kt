package com.example.flo.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

//database 엔티티 생성
@Entity(tableName = "SongTable")
data class Song(
    val title : String? = "",
    val singer : String? = "",
    var second :Int =0,
    var playTime:Int = 0,
    var isPlaying :Boolean = false,
    var music : String? = "",
    var albumImg : Int? = null,
    var isLike : Boolean = false,
    var albumId : Int =0
) {
    @PrimaryKey(autoGenerate = true) var id : Int = 0
}

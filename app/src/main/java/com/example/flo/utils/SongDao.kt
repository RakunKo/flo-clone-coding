package com.example.flo.utils

import androidx.room.*
import com.example.flo.data.entities.Song

//database 다루는 것
@Dao
interface SongDao {
    @Insert
    fun insert(song: Song)
    @Update
    fun update(song: Song)
    @Delete
    fun delete(song: Song)
    @Query("SELECT * FROM SONGTABLE")
    fun getSongs(): List<Song>

    @Query("SELECT * FROM SONGTABLE WHERE id = :id")
    fun getSong(id : Int): Song

    @Query("SELECT * FROM SONGTABLE WHERE title = :title")
    fun getSongFromTitle(title : String) : Song

    @Query("UPDATE SONGTABLE SET isLike = :isLike WHERE id = :id")
    fun updateIsLikeByID(isLike:Boolean, id:Int)

    @Query("SELECT * FROM SongTable WHERE isLike = :isLike")
    fun getLikedSongs (isLike : Boolean) :List<Song>
}
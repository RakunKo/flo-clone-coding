package com.example.flo.ui.song.foreground

import android.content.Context
import androidx.room.*
import com.example.flo.data.entities.Album
import com.example.flo.data.entities.Like
import com.example.flo.data.entities.Song
import com.example.flo.data.entities.User
import com.example.flo.utils.AlbumDao
import com.example.flo.utils.SongDao
import com.example.flo.utils.UserDao

@Database(entities = [Song::class, User::class, Like::class, Album::class], version = 2)
abstract class SongDatabase: RoomDatabase() {
    abstract fun SongDao() : SongDao
    abstract fun UserDao(): UserDao
    abstract fun AlbumDao(): AlbumDao

    companion object {
        private var instance : SongDatabase? = null
        @Synchronized
        fun getInstance(context: Context): SongDatabase? {
            if (instance ==null) {
                synchronized(SongDatabase::class){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        SongDatabase::class.java,
                        "song-database"
                    ).allowMainThreadQueries().build()  //main thread에 넘긴다.
                }
            }
            return instance
        }
    }
}
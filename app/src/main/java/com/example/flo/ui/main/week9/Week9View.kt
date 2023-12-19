package com.example.flo.ui.main.week9

import com.example.flo.data.remote.AlbumSongL

interface Week9View {
    fun onGetSongLoading()
    fun onGetWeek9Success(code:Int, result: ArrayList<AlbumSongL>)
    fun onGetWeek9Failure(code:Int, message : String)
}
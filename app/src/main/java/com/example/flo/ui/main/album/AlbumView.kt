package com.example.flo.ui.main.album

import com.example.flo.data.remote.AlbumList

interface AlbumView {
    fun onGetAlbumSuccess(code:Int, result: AlbumList)
    fun onGetAlbumFailure(code:Int, message : String)
}
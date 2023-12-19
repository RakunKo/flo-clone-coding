package com.example.flo.ui.main.search

import com.example.flo.data.remote.TrackInfo

interface SearchView {

    fun onGetSongLoading()
    fun onGetSearchSuccess(res : List<TrackInfo>)
    fun onGetSearchFailure(code:Int, message : String)
}
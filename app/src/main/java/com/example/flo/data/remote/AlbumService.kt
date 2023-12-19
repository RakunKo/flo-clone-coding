package com.example.flo.data.remote

import android.util.Log
import com.example.flo.ui.main.album.AlbumView
import com.example.flo.utils.getRetrofit
import retrofit2.*
class AlbumService {
    private lateinit var albumView: AlbumView
    fun setAlbumView(albumView: AlbumView){
        this.albumView = albumView
    }

    fun getAlbums(){
        val AlbumService = getRetrofit().create(AlbumRetrofitInterface::class.java)

        AlbumService.getAlbumList().enqueue(object:Callback<AlbumResponse>{
            override fun onResponse(call: Call<AlbumResponse>, response: Response<AlbumResponse>) {
                Log.d("album response", response.isSuccessful.toString())
                if (response.isSuccessful && response.code() == 200) {
                    val albumResponse: AlbumResponse = response.body()!!

                    when (val code = albumResponse.code) {
                        1000 -> {
                            albumView.onGetAlbumSuccess(code, albumResponse.result!!)
                        }
                        else -> albumView.onGetAlbumFailure(code, albumResponse.message)
                    }
                }
            }

            override fun onFailure(call: Call<AlbumResponse>, t: Throwable) {
                albumView.onGetAlbumFailure(400, "네트웨크 오류가 발생했습니다.")
            }
        })
    }
}
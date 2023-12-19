package com.example.flo.data.remote

import android.util.Log
import com.example.flo.ui.main.look.LookView
import com.example.flo.utils.getRetrofit
import retrofit2.*
class SongService {
    private lateinit var lookView: LookView
    fun setLookView(lookView: LookView){
        this.lookView = lookView
    }

    fun getSongs(){
        val SongService = getRetrofit().create(SongRetrofitInterface::class.java)

        lookView.onGetSongLoading()

        SongService.getSongs().enqueue(object:Callback<SongResponse>{
            override fun onResponse(call: Call<SongResponse>, response: Response<SongResponse>) {
                if (response.isSuccessful && response.code() == 200) {
                    val songResponse: SongResponse = response.body()!!

                    Log.d("SONG/RESPONSE", songResponse.toString())
                    when (val code = songResponse.code) {
                        1000 -> {
                            lookView.onGetSongSuccess(code, songResponse.result!!)
                        }
                        else -> lookView.onGetSongFailure(code, songResponse.message)
                    }
                }
            }

            override fun onFailure(call: Call<SongResponse>, t: Throwable) {
                lookView.onGetSongFailure(400, "네트웨크 오류가 발생했습니다.")
            }
        })
    }
}
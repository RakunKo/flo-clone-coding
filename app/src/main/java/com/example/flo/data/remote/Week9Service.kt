package com.example.flo.data.remote

import android.util.Log
import com.example.flo.ui.main.week9.Week9View
import com.example.flo.utils.getRetrofit
import retrofit2.*
class Week9Service {
    private lateinit var week9View: Week9View
    fun setWeek9View(week9View: Week9View){
        this.week9View = week9View
    }

    fun getWeek9(albumIdx :Int){
        val Week9Service = getRetrofit().create(Week9RetrofitInterface::class.java)

        Week9Service.getWeek9List(albumIdx = albumIdx).enqueue(object:Callback<Week9Response>{
            override fun onResponse(call: Call<Week9Response>, response: Response<Week9Response>) {

                if (response.isSuccessful && response.code() == 200) {
                    val week9Response: Week9Response = response.body()!!
                    Log.d("album response", week9Response.result.toString())
                    when (val code = week9Response.code) {
                        1000 -> {
                            week9View.onGetWeek9Success(code, week9Response.result!!)
                        }
                        else -> week9View.onGetWeek9Failure(code, week9Response.message)
                    }
                }
            }

            override fun onFailure(call: Call<Week9Response>, t: Throwable) {
                week9View.onGetWeek9Failure(400, "네트웨크 오류가 발생했습니다.")
            }
        })
    }
}
package com.example.flo.data.remote

import android.util.Log
import com.example.flo.ui.main.search.SearchView
import com.example.flo.utils.getRetrofitLASTFM
import retrofit2.*
class SearchService {
    private lateinit var searchView: SearchView
    fun setSearchView(searchView: SearchView){
        this.searchView = searchView
    }

    fun getSearch(api_key :String, search : String){
        val serachService = getRetrofitLASTFM(api_key, search).create(SearchRetrofitInterface::class.java)

        Log.d("search", serachService.toString())
        serachService.getSearchList(method = "track.search", track = search, key = api_key,  format = "json").enqueue(object:Callback<SearchResponse>{
            override fun onResponse(call: Call<SearchResponse>, response: Response<SearchResponse>) {

                if (response.isSuccessful && response.code() == 200) {
                    val searchResponse: SearchResponse = response.body()!!
                    if (searchResponse.results.totalResults != null) {
                        searchView.onGetSearchSuccess(searchResponse.results.trackmatches.track)
                    }
                    else {
                        searchView.onGetSearchFailure(400, "fail")
                    }
                }
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                searchView.onGetSearchFailure(400, t.toString())
            }
        })
    }
}
package com.example.flo.ui.main.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flo.data.remote.SearchService
import com.example.flo.data.remote.TrackInfo
import com.example.flo.databinding.FragmentSearchBinding

class SearchFragment : Fragment(), SearchView {

    lateinit var binding: FragmentSearchBinding
    val api_key = "222eabe094b2aa2de09baead078b50ca"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        binding.searchButSubmit.setOnClickListener {
            val search = binding.searchEt.text.toString()  //찾을 것
            getSearch(search)
        }

        return binding.root
    }

    private fun getSearch(search : String) {
        val searchService = SearchService()
        searchService.setSearchView(this)

        searchService.getSearch(api_key, search)
    }
    private fun initRecyclerView(res : List<TrackInfo>) {
        val searchRVAdapter = SearchRVAdapter(requireContext(), res)
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.searchRv.layoutManager = layoutManager
        binding.searchRv.adapter = searchRVAdapter
    }

    override fun onGetSongLoading() {
        binding.searchLoading.visibility = View.VISIBLE
    }
    override fun onGetSearchSuccess(res: List<TrackInfo>) {
        binding.searchLoading.visibility = View.GONE
        initRecyclerView(res)
    }
    override fun onGetSearchFailure(code: Int, message: String) {
        binding.searchLoading.visibility = View.GONE
        Log.d("fail", message)
    }
}
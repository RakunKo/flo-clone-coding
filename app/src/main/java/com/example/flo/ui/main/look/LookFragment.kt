package com.example.flo.ui.main.look

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.flo.data.remote.FloChartResult
import com.example.flo.data.remote.SongService
import com.example.flo.databinding.FragmentLookBinding

class LookFragment : Fragment(), LookView {

    lateinit var binding: FragmentLookBinding
    private lateinit var floCharAdapter : SongRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLookBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        getSongs()
    }
    private fun getSongs() {
        val songService = SongService()
        songService.setLookView(this)

        songService.getSongs()
    }
    private fun initRecyclerView(result : FloChartResult) {
        floCharAdapter = SongRVAdapter (requireContext(), result)

        binding.lookFloChartRv.adapter = floCharAdapter
    }

    override fun onGetSongLoading() {
        binding.lookLoadingPb.visibility = View.VISIBLE
    }

    override fun onGetSongSuccess(code: Int, result: FloChartResult) {
        binding.lookLoadingPb.visibility = View.GONE
        initRecyclerView(result)
    }

    override fun onGetSongFailure(code: Int, message: String) {
        binding.lookLoadingPb.visibility = View.GONE
        Log.d("LOOK_FRAG/SONG_RESPONSE", message)
    }
}
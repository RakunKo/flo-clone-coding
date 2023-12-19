package com.example.flo.ui.main.week9

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flo.data.remote.AlbumSongL
import com.example.flo.data.remote.Week9Service
import com.example.flo.databinding.FragmentWeek9ListBinding

class Week9ListFragment(private val albumIdx : Int) : Fragment(), Week9View {
    lateinit var binding : FragmentWeek9ListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWeek9ListBinding.inflate(inflater,container, false)

        return binding.root
    }
    override fun onStart() {
        super.onStart()
        getWeek9()
    }
    private fun getWeek9() {
        val week9Service = Week9Service()
        week9Service.setWeek9View(this)

        week9Service.getWeek9(albumIdx)
    }
    private fun initRecyclerView(result : ArrayList<AlbumSongL>) {
        val week9RVAdapter = Week9RvAdapter(requireContext(), result)
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.week9Rv.layoutManager = layoutManager
        binding.week9Rv.adapter = week9RVAdapter
    }

    override fun onGetSongLoading() {
        binding.week9LoadingPb.visibility = View.VISIBLE
    }

    override fun onGetWeek9Success(code: Int, result: ArrayList<AlbumSongL>) {
        binding.week9LoadingPb.visibility = View.GONE
        initRecyclerView(result)
    }

    override fun onGetWeek9Failure(code: Int, message: String) {
        binding.week9LoadingPb.visibility = View.GONE
        Log.d("LOOK_FRAG/SONG_RESPONSE", message)
    }

}
package com.example.flo.ui.main.week9

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.flo.R
import com.example.flo.data.remote.AlbumL
import com.example.flo.databinding.FragmentWeek9Binding
import com.example.flo.ui.main.MainActivity
import com.example.flo.ui.main.home.HomeFragment
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson

class Week9Fragment : Fragment() {
    lateinit var binding : FragmentWeek9Binding
    private var gson: Gson = Gson()
    private val info = arrayListOf("수록곡", "상세정보", "영상")
    lateinit var album : AlbumL
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWeek9Binding.inflate(inflater,container,false)

        binding.week9BackIv.setOnClickListener{
            (context as MainActivity).supportFragmentManager.beginTransaction().replace(R.id.main_frm, HomeFragment()).commitAllowingStateLoss()
        }

        val albumJson = arguments?.getString("album")
        album = gson.fromJson(albumJson, AlbumL::class.java)  //초기화

        binding.week9MusicTitleTv.text = album.title
        binding.week9SingerNameTv.text = album.singer
        Glide.with(requireContext()).load(album.coverImgUrl).into(binding.week9AlbumIv)

        val albumAdapter = Week9VPAdapter(this, album.albumIdx)
        binding.week9ContentVp.adapter = albumAdapter
        binding.week9ContentVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        TabLayoutMediator(binding.week9ContentTb, binding.week9ContentVp) {
            tab, position->
            tab.text = info[position]
        }.attach()

        return binding.root
    }
}
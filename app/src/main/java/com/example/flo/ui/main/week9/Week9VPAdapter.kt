package com.example.flo.ui.main.week9

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.flo.ui.main.album.detail.AlbumDetailFragment
import com.example.flo.ui.main.album.video.AlbumVideoFragment

class Week9VPAdapter(fragment: Fragment, private val albumIdx : Int) :FragmentStateAdapter(fragment){
    override fun getItemCount(): Int =3

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0-> Week9ListFragment(albumIdx)
            1-> AlbumDetailFragment()
            else-> AlbumVideoFragment()
        }
    }
}
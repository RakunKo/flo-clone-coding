package com.example.flo.ui.main.album

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.flo.ui.main.album.detail.AlbumDetailFragment
import com.example.flo.ui.main.album.song.AlbumSongFragment
import com.example.flo.ui.main.album.video.AlbumVideoFragment

class AlbumVPAdapter(fragment: Fragment) :FragmentStateAdapter(fragment){
    override fun getItemCount(): Int =3

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0-> AlbumSongFragment()
            1-> AlbumDetailFragment()
            else-> AlbumVideoFragment()
        }
    }
}
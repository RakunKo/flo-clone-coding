package com.example.flo.ui.main.locker

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.flo.ui.main.locker.album.LockerAlbumFragment
import com.example.flo.ui.main.locker.file.LockerFileFragment
import com.example.flo.ui.main.locker.saved.LockerSaveFragment

class LockerVPAdapter(fragment: Fragment) :FragmentStateAdapter(fragment){
    override fun getItemCount(): Int =3

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0-> LockerSaveFragment()
            1-> LockerFileFragment()
            else -> LockerAlbumFragment()
        }
    }
}
package com.example.flo.ui.main.locker.album

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flo.ui.song.foreground.SongDatabase
import com.example.flo.data.entities.Album
import com.example.flo.databinding.FragmentLockerAlbumBinding

class LockerAlbumFragment : Fragment() {
    lateinit var binding : FragmentLockerAlbumBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLockerAlbumBinding.inflate(inflater, container, false)

        binding.lockerAlbumRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        val albumDB = SongDatabase.getInstance(requireContext())!!

        val lockeralbumRVAdapter = LockerAlbumRVAdapter()
        binding.lockerAlbumRv.adapter = lockeralbumRVAdapter
        lockeralbumRVAdapter.addAlbums(albumDB.AlbumDao().getLikedAlbums(getJwt()) as ArrayList<Album>)

        lockeralbumRVAdapter.setMyItemClickListener(object:
            LockerAlbumRVAdapter.MyItemClickListener {
            override fun onRemoveSong(albumId: Int) {
                albumDB.AlbumDao().disLikedAlbum(getJwt(), albumId)
            }
        })
        return binding.root
    }

    private fun getJwt() :Int{
        val spf = activity?.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)  //프래그먼트에서 spf 사용법
        return spf!!.getInt("jwt",0)
    }
}
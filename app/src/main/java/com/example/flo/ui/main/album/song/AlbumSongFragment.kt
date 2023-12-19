package com.example.flo.ui.main.album.song

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.flo.data.entities.Album
import com.example.flo.databinding.FragmentSongBinding

class AlbumSongFragment : Fragment() {
    lateinit var binding : FragmentSongBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSongBinding.inflate(inflater,container, false)
        binding.songMixoffTg.setOnClickListener() {
            setToggleStatus(true)
        }
        binding.songMixonTg.setOnClickListener() {
            setToggleStatus(false)
        }
        return binding.root
    }
    fun setSongList(album: Album) {
        
    }
    fun setToggleStatus(isPlaying : Boolean) {
        if (isPlaying) {
            binding.songMixonTg.visibility = View.VISIBLE
            binding.songMixoffTg.visibility = View.GONE
        }
        else {
            binding.songMixonTg.visibility = View.GONE
            binding.songMixoffTg.visibility = View.VISIBLE
        }
    }

}
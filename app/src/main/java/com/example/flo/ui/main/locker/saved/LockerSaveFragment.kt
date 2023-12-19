package com.example.flo.ui.main.locker.saved

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flo.ui.main.locker.bottom.BottomSheetFragment
import com.example.flo.R
import com.example.flo.ui.song.foreground.SongDatabase
import com.example.flo.databinding.FragmentLockerSaveBinding
import com.example.flo.data.entities.Song

class LockerSaveFragment : Fragment() {
    lateinit var binding : FragmentLockerSaveBinding
    lateinit var songDB : SongDatabase
    var isSelect : Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLockerSaveBinding.inflate(inflater, container, false)

        binding.lockerRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        songDB = SongDatabase.getInstance(requireContext())!!

        val lockerRVAdapter = LockerRVAdapter()
        binding.lockerRv.adapter = lockerRVAdapter
        lockerRVAdapter.addSongs(songDB.SongDao().getLikedSongs(true) as ArrayList<Song>)

        lockerRVAdapter.setMyItemClickListener(object: LockerRVAdapter.MyItemClickListener {
            override fun onRemoveSong(songId: Int) {
                songDB.SongDao().updateIsLikeByID(false, songId)
            }
        })

        binding.lockerSecletAllLayout.setOnClickListener {
            setSelectAllLayout(isSelect)
        }



        return binding.root
    }

    fun setSelectAllLayout(isSelect : Boolean) {
        if(!isSelect) {  //off 상태에서 클릭
            binding.lockerSecletAllImg.setImageResource(R.drawable.btn_playlist_select_on)
            binding.lockerSecletAllText.text = "선택해제"
            binding.lockerSecletAllText.setTextColor(Color.parseColor("#3f3fff"))
            val bottomSheet = BottomSheetFragment(this, LockerRVAdapter())
            bottomSheet.show(parentFragmentManager, bottomSheet.tag)
            this.isSelect = true
        }
        else {
            binding.lockerSecletAllImg.setImageResource(R.drawable.btn_playlist_select_off)
            binding.lockerSecletAllText.text = "전체선택"
            binding.lockerSecletAllText.setTextColor(Color.parseColor("#000000"))
            this.isSelect = false
        }
    }


}
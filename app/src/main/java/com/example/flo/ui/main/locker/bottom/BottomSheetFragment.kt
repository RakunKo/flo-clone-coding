package com.example.flo.ui.main.locker.bottom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.flo.R
import com.example.flo.ui.song.foreground.SongDatabase
import com.example.flo.data.entities.Song
import com.example.flo.databinding.BottomSheetDialogBinding
import com.example.flo.ui.main.locker.saved.LockerRVAdapter
import com.example.flo.ui.main.locker.saved.LockerSaveFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetFragment(private val lockerSaveFragment: LockerSaveFragment, private val lockerRVAdapter: LockerRVAdapter) : BottomSheetDialogFragment() {
    lateinit var binding : BottomSheetDialogBinding
    lateinit var songDB: SongDatabase

    val songs = arrayListOf<Song>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.bottom_sheet_dialog, container, false)
        binding = BottomSheetDialogBinding.bind(view)


        binding.bottomSheetDeleteBtn.setOnClickListener {
            dismiss()  // bottom dialog 숨기기
            getDB()
            lockerSaveFragment.setSelectAllLayout(true) //전체해제 원래대로 바꾸기
        }
        return view
    }
    private fun getDB() {
        songs.clear()
        songDB = SongDatabase.getInstance(requireContext())!!
        songs.addAll(songDB.SongDao().getSongs())
        //like 데이터베이스에서 수정
        for (i in 0..songs.size-1) {
            if (songs[i].isLike) {
                songDB.SongDao().updateIsLikeByID(!songs[i].isLike, songs[i].id)  //선택된 것 모두 false로 바꾸기
            }
        }
        lockerRVAdapter.editList()
    }
}
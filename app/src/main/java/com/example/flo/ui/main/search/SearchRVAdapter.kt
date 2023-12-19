package com.example.flo.ui.main.search

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.flo.data.remote.TrackInfo
import com.example.flo.databinding.ItemLockerSaveBinding

class SearchRVAdapter(val context: Context, val res : List<TrackInfo>) : RecyclerView.Adapter<SearchRVAdapter.ViewHolder>() {


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemLockerSaveBinding = ItemLockerSaveBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d("resultRv", res.toString())
        //holder.bind(result.songs[position])
        holder.title.text = res[position].name
        holder.singer.text = res[position].artist

        if(res[position].image[1].text == "" || res[position].image[1].text == null){

        } else {
            Glide.with(context).load(res[position].image[1].text).into(holder.coverImg)
        }

    }

    override fun getItemCount(): Int = res.size


    inner class ViewHolder(val binding: ItemLockerSaveBinding) : RecyclerView.ViewHolder(binding.root){

        val title : TextView = binding.itemLockerTitleTv
        val singer : TextView = binding.itemLockerSingerTv
        val coverImg : ImageView = binding.itemLockerCoverImgIv

//        fun bind(song: FloChartSongs){
//            if(song.coverImgUrl == "" || song.coverImgUrl == null) {
//            } else {
//                Glide.with(context).load(song.coverImgUrl).into(binding.itemSongImgIv)
//            }
//
//            binding.itemSongTitleTv.text = song.title
//            binding.itemSongSingerTv.text = song.singer
//        }
    }

    interface MyItemClickListener{
        fun onRemoveSong(songId: Int)
    }

    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
        mItemClickListener = itemClickListener
    }
}
package com.example.flo.ui.main.week9

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.flo.data.remote.AlbumSongL
import com.example.flo.databinding.ItemWeek9Binding

class Week9RvAdapter(val context: Context, val result : ArrayList<AlbumSongL>) : RecyclerView.Adapter<Week9RvAdapter.ViewHolder>() {


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemWeek9Binding = ItemWeek9Binding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d("resultRv", result.toString())
        //holder.bind(result.songs[position])
        holder.title.text = result[position].title
        holder.singer.text = result[position].singer
        holder.idx.text= result[position].songIdx.toString()
        if (result[position].isTitleSong == "T") {
            holder.binding.songListTitle01Tv.visibility = View.VISIBLE
        }else {
            holder.binding.songListTitle01Tv.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int = result.size


    inner class ViewHolder(val binding: ItemWeek9Binding) : RecyclerView.ViewHolder(binding.root){

        val title : TextView = binding.songMusicTitle01Tv
        val singer : TextView = binding.songSingerName01Tv
        val idx : TextView = binding.songListOrder01Tv

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
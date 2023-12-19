package com.example.flo.ui.main.home.album

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.flo.data.remote.AlbumL
import com.example.flo.data.remote.AlbumList
import com.example.flo.databinding.ItemAlbumBinding

class HomeAlbumRvAdapter(val context: Context, val result : AlbumList) : RecyclerView.Adapter<HomeAlbumRvAdapter.ViewHolder>() {


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemAlbumBinding = ItemAlbumBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        Log.d("RecyclerView", "onCreateViewHolder")
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //holder.bind(result.songs[position])
        if(result.albums[position].coverImgUrl == "" || result.albums[position].coverImgUrl == null){
        } else {
            Log.d("image",result.albums[position].coverImgUrl )
            Glide.with(context).load(result.albums[position].coverImgUrl).into(holder.coverImg)
        }
        holder.title.text = result.albums[position].title
        holder.singer.text = result.albums[position].singer
        holder.binding.itemAlbum.setOnClickListener {
            mItemClickListener.onItemClick(result.albums[position])
        }
    }

    override fun getItemCount(): Int = result.albums.size


    inner class ViewHolder(val binding: ItemAlbumBinding) : RecyclerView.ViewHolder(binding.root){

        val coverImg : ImageView = binding.itemAlbumCoverImgIv
        val title : TextView = binding.itemAlbumTitleTv
        val singer : TextView = binding.itemAlbumSingerTv

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
        fun onItemClick(result : AlbumL)
    }

    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
        mItemClickListener = itemClickListener
    }
}
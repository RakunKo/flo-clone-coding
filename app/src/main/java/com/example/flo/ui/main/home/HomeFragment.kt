package com.example.flo.ui.main.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.flo.R
import com.example.flo.ui.song.foreground.SongDatabase
import com.example.flo.ui.main.week9.Week9Fragment
import com.example.flo.utils.communicateInerface
import com.example.flo.data.entities.Album
import com.example.flo.data.remote.AlbumL
import com.example.flo.data.remote.AlbumList
import com.example.flo.data.remote.AlbumService
import com.example.flo.databinding.FragmentHomeBinding
import com.example.flo.ui.main.MainActivity
import com.example.flo.ui.main.album.AlbumFragment
import com.example.flo.ui.main.album.AlbumRVAdapter
import com.example.flo.ui.main.album.AlbumView
import com.example.flo.ui.main.home.album.HomeAlbumRvAdapter
import com.example.flo.ui.main.home.panel.BannerVPAdapter
import com.example.flo.ui.main.home.panel.HomeVPAdapter
import com.google.gson.Gson

class HomeFragment : Fragment(), communicateInerface, AlbumView {
    var curPosition = 0
    lateinit var binding: FragmentHomeBinding
    private lateinit var albumListAdapter: HomeAlbumRvAdapter
    private lateinit var albumDB : SongDatabase
    private var nowPos = 0
    private val albums = arrayListOf<Album>()  //전체 앨범

    val handler = Handler(Looper.getMainLooper()){
        setPage()
        true
    }
    override fun sendData(album: Album) {
        if (activity is MainActivity) {
            val activity = activity as MainActivity
            activity.setMiniPlayerAlbum(album.id)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        albumDB = SongDatabase.getInstance(requireContext())!!
        albums.addAll(albumDB.AlbumDao().getAlbums())


        val albumRVAdapter = AlbumRVAdapter(albums)
        binding.homeTodayMusicAlbumRv.adapter = albumRVAdapter
        binding.homeTodayMusicAlbumRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)



        albumRVAdapter.setMyItemClickListener(object: AlbumRVAdapter.MyItemClickListener{
            override fun onItemClick(album: Album) {
                changeAlbumFragment(album)
            }

            override fun getItemClick(album: Album) {
                sendData(album)
            }
        })

        val homeAdapter = HomeVPAdapter(this)

        binding.homePannelBackgroundVp.adapter = homeAdapter
        binding.homePannelBackgroundVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        binding.homePannelBackgroundCi.setViewPager(binding.homePannelBackgroundVp)


        binding.homeAdverVp.adapter = BannerVPAdapter(this)
        binding.homeAdverVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        val thread = Thread(PagerRunnable())
        thread.start()


        return binding.root
    }
    override fun onStart() {
        super.onStart()
        getAlbums()
    }

    private fun changeAlbumFragment(album: Album) {
        (context as MainActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, AlbumFragment().apply {
                arguments = Bundle().apply {
                    val gson = Gson()
                    val albumJson = gson.toJson(album)
                    putString("album", albumJson)
                }
            })
            .commitAllowingStateLoss()
    }


    fun setPage() {
        if (curPosition == 3) curPosition = 0
        binding.homePannelBackgroundVp.setCurrentItem(curPosition, true)
        curPosition+=1
    }

    inner class PagerRunnable : Runnable {
        override  fun run() {
            while(true) {
                Thread.sleep(2000)
                handler.sendEmptyMessage(0)
            }
        }
    }

    private fun getAlbums() {
        val albumService = AlbumService()
        albumService.setAlbumView(this)

        albumService.getAlbums()
    }
    private fun initRecyclerView(result : AlbumList) {
        albumListAdapter = HomeAlbumRvAdapter (requireContext(), result)
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.homeWeek9AlbumRv.layoutManager = layoutManager
        binding.homeWeek9AlbumRv.adapter = albumListAdapter

        albumListAdapter.setMyItemClickListener(object: HomeAlbumRvAdapter.MyItemClickListener {
            override fun onItemClick(result: AlbumL) {
                changeAlbumFragment2(result)
            }
        })
    }
    private fun changeAlbumFragment2(result: AlbumL) {
        (context as MainActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, Week9Fragment().apply {
                arguments = Bundle().apply {
                    val gson = Gson()
                    val albumJson = gson.toJson(result)
                    putString("album", albumJson)
                }
            })
            .commitAllowingStateLoss()
    }

    override fun onGetAlbumSuccess(code: Int, result: AlbumList) {
        initRecyclerView(result)
    }

    override fun onGetAlbumFailure(code: Int, message: String) {
        Log.d("HOME_FRAG/ALBUM_RESPON", message)
    }
}
package com.example.flo.ui.main

import android.app.Activity
import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.flo.ui.main.home.HomeFragment
import com.example.flo.ui.main.locker.LockerFragment
import com.example.flo.ui.main.look.LookFragment
import com.example.flo.R
import com.example.flo.ui.main.search.SearchFragment
import com.example.flo.ui.song.SongActivity
import com.example.flo.ui.song.foreground.SongDatabase
import com.example.flo.data.entities.Album
import com.example.flo.data.entities.Song
import com.example.flo.databinding.ActivityMainBinding
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {
    private var mediaPlayer : MediaPlayer? = null

    lateinit var binding: ActivityMainBinding
    private var song: Song = Song()   //현재곡
    lateinit var timer: Timer
    private var gson: Gson = Gson()

    lateinit var albumDB : SongDatabase
    lateinit var songDB : SongDatabase
    private var nowPos = 0
    val songs = arrayListOf<Song>()  //전체곡
    val albums = arrayListOf<Album>()  //전체 앨범

    val getResultText = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            Toast.makeText(getApplicationContext(), result.data?.getStringExtra("title"), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        inputDummyAlbums()  //album data 삽입
        inputDummySongs()   //song data 삽입
        initBottomNavigation()

        binding.mainPlayerCl.setOnClickListener{
            val editor = getSharedPreferences("song", MODE_PRIVATE).edit()
            editor.remove("songId")
            editor.remove("songSec")
            editor.putInt("songId",song.id)
            editor.putInt("songSec",song.second)
            editor.apply()

            val intent = Intent(this, SongActivity::class.java)
            startActivity(intent)
        }

        binding.mainBtnNext.setOnClickListener {
            moveSong(1)
        }
        binding.mainBtnPrevious.setOnClickListener {
            moveSong(-1)
        }
        binding.mainMiniplayerBtn.setOnClickListener {
            playSong()
        }
        initData()
        Log.d("MAIN/JWT_TO_SERVER", getJwt().toString())
    }

    private fun getJwt() :String?{
        val spf = getSharedPreferences("auth2", MODE_PRIVATE)
        return spf!!.getString("jwt","")
    }
    override fun onPause() {
        super.onPause()
        binding.mainMiniplayerBtn.setImageResource(R.drawable.btn_miniplayer_play)
        song.isPlaying = false
        timer.isPlaying = false
        mediaPlayer?.pause()
        val editor = getSharedPreferences("song", MODE_PRIVATE).edit()
        editor.remove("songId")
        editor.remove("songSec")
        editor.putInt("songId",song.id)
        editor.putInt("songSec",song.second)
        editor.apply()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.interrupt()
        mediaPlayer?.pause()
        val editor = getSharedPreferences("song", MODE_PRIVATE).edit()
        editor.remove("songId")
        editor.remove("songSec")
        editor.putInt("songId",song.id)
        editor.putInt("songSec",song.second)
        editor.apply()
    }

    override fun onResume() {
        super.onResume()
        val spf = getSharedPreferences("song", MODE_PRIVATE)
        val songId = spf.getInt("songId", 0)
        val songSec = spf.getInt("songSec", 0)
        nowPos = getPlayingSongPosition(songId)
        song = songs[nowPos]
        song.second = songSec
        resetMedia()
        mediaPlayer?.seekTo(song.second * 1000 + 1200)
        setMiniPlayerRe(song)
    }

    private fun getPlayingSongPosition(songId : Int) :Int {
        for(i in 0 until songs.size) {
            if (songs[i].id == songId) return i
        }
        return 0
    }

    private fun startTimer() {
        timer = Timer(song.playTime, song.isPlaying)
        timer.start()
    }
    private fun playSong() {
        if (song.isPlaying == false) {
            binding.mainMiniplayerBtn.setImageResource(R.drawable.btn_miniplay_pause)
            song.isPlaying = true
            mediaPlayer?.start()
            timer.isPlaying = true
        }
        else {
            binding.mainMiniplayerBtn.setImageResource(R.drawable.btn_miniplayer_play)
            song.isPlaying = false
            mediaPlayer?.pause()
            timer.isPlaying = false
        }
    }
    private fun resetMedia() {
        mediaPlayer?.reset()
        val music = resources.getIdentifier(song.music, "raw", this.packageName)
        mediaPlayer = MediaPlayer.create(this, music)
    }
    private fun moveSong(direct :Int) {    //이전곡 다음곡 이동하는 함수
        if (nowPos + direct <0) {
            Toast.makeText(this, "first song", Toast.LENGTH_SHORT).show()
            return
        }
        if (nowPos + direct >= songs.size) {
            Toast.makeText(this, "last song", Toast.LENGTH_SHORT).show()
            return
        }

        nowPos += direct

        setMiniPlayerBtn()
    }
    private fun setMiniPlayerBtn() {   //노래 이전, 다음 누르면
        song = songs[nowPos]   //바뀐 노래를 현재 노래로 설정
        song.second = 0
        timer.interrupt()
        startTimer()
        resetMedia()
        binding.mainMiniplayerBtn.setImageResource(R.drawable.btn_miniplayer_play)
        binding.mainMiniplayerTitleTv.text = song.title
        binding.mainMiniplayerSingerTv.text = song.singer
        binding.mainProgressSb.progress = 0
    }
    fun setMiniPlayerAlbum(albumId : Int) {   //앨범 선택시 초기화
        nowPos = getSongPosition(albumId)
        song = songs[nowPos]  //데이터베이스에서 데이터 찾기
        song.second = 0
        timer.interrupt()
        startTimer()
        resetMedia()
        playSong()
        binding.mainMiniplayerTitleTv.text = song.title
        binding.mainMiniplayerSingerTv.text = song.singer
        binding.mainProgressSb.progress = 0
    }

    private fun getSongPosition(albumId : Int) :Int {
        for(i in 0 until songs.size) {
            if (songs[i].albumId == albumId) return i   //항상 첫번째 곡이 선택된다.
        }
        return 0
    }

    private fun setMiniPlayer(song: Song) {   //그전 데이터로 초기화
        binding.mainMiniplayerBtn.setImageResource(R.drawable.btn_miniplayer_play)
        binding.mainMiniplayerTitleTv.text = song.title
        binding.mainMiniplayerSingerTv.text = song.singer
        binding.mainProgressSb.progress = (song.second*100000)/ song.playTime

        startTimer()
    }
    private fun setMiniPlayerRe(song: Song) {   //그전 데이터로 초기화
        binding.mainMiniplayerBtn.setImageResource(R.drawable.btn_miniplayer_play)
        binding.mainMiniplayerTitleTv.text = song.title
        binding.mainMiniplayerSingerTv.text = song.singer
        binding.mainProgressSb.progress = (song.second*100000)/ song.playTime
    }
    private fun initData() {
        val spf = getSharedPreferences("song", MODE_PRIVATE)  //spf에 id 저장
        val songId = spf.getInt("songId", 0)
        val songSec = spf.getInt("songSec", 0)

        songDB = SongDatabase.getInstance(this)!!
        albumDB = SongDatabase.getInstance(this)!!
        songs.addAll(songDB.SongDao().getSongs())
        albums.addAll(albumDB.AlbumDao().getAlbums())

        song = if (songId == 0) {
            songDB.SongDao().getSong(1)   //기본 값
        } else {
            songDB.SongDao().getSong(songId)
        }
        song.second =songSec
        resetMedia()
        mediaPlayer?.seekTo(song.second * 1000 + 1200)
        setMiniPlayer(song)
    }

    private fun initBottomNavigation(){

        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, HomeFragment())
            .commitAllowingStateLoss()

        binding.mainBnv.setOnItemSelectedListener{ item ->
            when (item.itemId) {

                R.id.homeFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, HomeFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.lookFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, LookFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
                R.id.searchFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, SearchFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
                R.id.lockerFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, LockerFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
            }
            false
        }
    }

    private fun inputDummySongs() {
        songDB = SongDatabase.getInstance(this)!!
        val songs = songDB.SongDao().getSongs()

        if (songs.isNotEmpty()) return  //데이터 있으면 종료

        songDB.SongDao().insert(
            Song(
                "Lilac",
                "아이유 (IU)",
                0,
                200,
                false,
                "lilac",
                R.drawable.img_album_exp2,
                false,
                1,
            )
        )

        songDB.SongDao().insert(
            Song(
                "Flu",
                "아이유 (IU)",
                0,
                200,
                false,
                "flu",
                R.drawable.img_album_exp2,
                false,
                1,
            )
        )
        songDB.SongDao().insert(
            Song(
                "Coin",
                "아이유 (IU)",
                0,
                200,
                false,
                "flu",
                R.drawable.img_album_exp2,
                false,
                1,
            )
        )
        songDB.SongDao().insert(
            Song(
                "봄 안녕 봄",
                "아이유 (IU)",
                0,
                200,
                false,
                "flu",
                R.drawable.img_album_exp2,
                false,
                1,
            )
        )

        songDB.SongDao().insert(
            Song(
                "Butter",
                "방탄소년단 (BTS)",
                0,
                190,
                false,
                "butter",
                R.drawable.img_album_exp,
                false,
                2,
            )
        )

        songDB.SongDao().insert(
            Song(
                "Next Level",
                "에스파 (AESPA)",
                0,
                210,
                false,
                "nextlevel",
                R.drawable.img_album_exp3,
                false,
                3,
            )
        )


        songDB.SongDao().insert(
            Song(
                "Boy with Luv",
                "방탄소년단 (BTS)",
                0,
                230,
                false,
                "boywithluv",
                R.drawable.img_album_exp4,
                false,
                4,
            )
        )


        songDB.SongDao().insert(
            Song(
                "BBoom BBoom",
                "모모랜드 (MOMOLAND)",
                0,
                240,
                false,
                "bboom",
                R.drawable.img_album_exp5,
                false,
                5,
            )
        )

        songDB.SongDao().insert(
            Song(
                "Weekend",
                "태연 (Tae Yeon)",
                0,
                240,
                false,
                "weekend",
                R.drawable.img_album_exp6,
                false,
                6,
            )
        )

        val _songs = songDB.SongDao().getSongs()
        Log.d("DB data", _songs.toString())
    }
    private fun inputDummyAlbums() {
        albumDB = SongDatabase.getInstance(this)!!
        val albums = albumDB.AlbumDao().getAlbums()

        if (albums.isNotEmpty()) return  //데이터 있으면 종료

        albumDB.AlbumDao().insert(
            Album (
                "IU 5th Album 'LILAC",
                "아이유 (IU)" ,
                R.drawable.img_album_exp2,
            )
        )
        albumDB.AlbumDao().insert(
            Album (
                "Butter",
                "방탄소년단 (BTS)" ,
                R.drawable.img_album_exp,
            )
        )
        albumDB.AlbumDao().insert(
            Album (
                "IScreaM Vol.10 : Next Level Remixes",
                "에스파 (aespa)" ,
                R.drawable.img_album_exp3,
            )
        )
        albumDB.AlbumDao().insert(
            Album (
                "MAP OF THE SEOUL : PERSONA",
                "방탄소년단 (BTS)" ,
                R.drawable.img_album_exp4,
            )
        )
        albumDB.AlbumDao().insert(
            Album (
                "GREAT!",
                "모모랜드 (MomoLand)" ,
                R.drawable.img_album_exp5,
            )
        )
        val _albums = albumDB.AlbumDao().getAlbums()
        Log.d("DB data", _albums.toString())
    }
    inner class Timer(private val playTime:Int,var isPlaying: Boolean = true) :Thread() {
        private var second : Int = song.second
        private var mills:Float = (second * 1000).toFloat()

        override fun run() {
            super.run()
            try {
                while(true) {
                    if (song.second >= playTime) {
                        song.second = 0
                        mills = 0f
                        resetMedia()
                        runOnUiThread{
                            playSong()
                        }
                    }
                    if(isPlaying) {
                        sleep(50)
                        mills += 50

                        runOnUiThread {
                            binding.mainProgressSb.progress = ((mills/playTime)*100).toInt()
                        }
                        if (mills%1000 == 0f) {
                            song.second++
                        }
                    }
                }
            }catch(e:InterruptedException) {
                Log.d("Song","쓰레가 죽었습니다 ${e.message}")
            }
        }
    }
}
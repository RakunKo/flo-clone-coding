package com.example.flo.ui.song

import android.app.Activity
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.flo.ui.song.foreground.Foreground
import com.example.flo.R
import com.example.flo.ui.song.foreground.SongDatabase
import com.example.flo.data.entities.Song
import com.example.flo.databinding.ActivitySongBinding
import com.example.flo.ui.main.MainActivity

//week7
class SongActivity : AppCompatActivity() {

    lateinit var binding: ActivitySongBinding
    lateinit var timer: Timer
    private var mediaPlayer : MediaPlayer? = null

    val songs = arrayListOf<Song>()
    lateinit var songDB: SongDatabase
    var song : Song = Song()    //현재 재생중인 노래
    var nowPos = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.songDownIb.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("title", binding.songTitleIb.text.toString())
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
        initClickListener()
        initPlayList()
        initSong()

    }



    override fun onPause() {
        super.onPause()
        setPlayerStatus(false)
        song.second = ((binding.songProgressSb.progress * song.playTime)/100)/1000
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val editor = sharedPreferences.edit() //에디터
        editor.remove("songId")
        editor.remove("songSec")
        editor.putInt("songId", song.id)
        editor.putInt("songSec", song.second)
        editor.apply()

    }

    override fun onResume() {
        super.onResume()
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val pos = sharedPreferences.getInt("songId", 0)
        nowPos = getPlayingSongPosition(pos)
        song = songs[nowPos]
        // UI 및 노래 상태를 설정
        setPlayer(song)
    }
    override fun onDestroy() {
        super.onDestroy()
        timer.interrupt()
        mediaPlayer?.release()
        mediaPlayer = null
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val editor = sharedPreferences.edit() //에디터
        editor.remove("songId")
        editor.remove("songSec")
        editor.putInt("songId", song.id)
        editor.putInt("songSec", song.second)
        editor.apply()
    }

    private fun initPlayList() {
        songDB = SongDatabase.getInstance(this)!!
        songs.addAll(songDB.SongDao().getSongs())
    }
    private fun serviceStart() {
        Log.d("서비스", " 시작")
        val intent = Intent(this, Foreground::class.java)
        ContextCompat.startForegroundService(this, intent)
    }

    fun serviceStop() {
        Log.d("서비스", " 종료")
        val intent = Intent(this, Foreground::class.java)
        stopService(intent)
    }

    private fun initSong() {
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val songId = sharedPreferences.getInt("songId", 0)  //현재 미니플레이어에 있는 곡의 아이디 가져오기
        val songSec = sharedPreferences.getInt("songSec", 0)
        nowPos = getPlayingSongPosition(songId)
        song = songs[nowPos]   //현재곡 할당
        song.second = songSec
        startTimer()
        setPlayer(song)
    }

    private fun getPlayingSongPosition(songId : Int) :Int {
        for(i in 0 until songs.size) {
            if (songs[i].id == songId) return i
        }
        return 0
    }

    private fun setPlayer(song : Song) {
        binding.songTitleIb.text = song.title
        binding.songWriterIb.text = song.singer
        binding.songStartTimeTv.text = String.format("%02d:%02d", song.second/60, song.second%60)
        binding.songEndTimeTv.text = String.format("%02d:%02d", song.playTime/60, song.playTime%60)
        binding.songProgressSb.progress = (song.second*1000/song.playTime)*100
        binding.songCoverIb.setImageResource(song.albumImg!!)
        if (mediaPlayer == null) resetMedia()
        else {
            if (song.second != 0) mediaPlayer?.seekTo(song.second * 1000 + 1200)
        }

        if(song.isLike) binding.songLikeIv.setImageResource(R.drawable.ic_my_like_on)
        else binding.songLikeIv.setImageResource(R.drawable.ic_my_like_off)

        setPlayerStatus(song.isPlaying)
    }

    private fun resetMedia() {
        mediaPlayer?.reset()
        val music = resources.getIdentifier(song.music, "raw", this.packageName)
        mediaPlayer = MediaPlayer.create(this, music)
    }

    private fun startTimer() {
        timer = Timer(song.playTime, song.isPlaying)
        timer.start()
    }

    inner class Timer(private val playTime:Int,var isPlaying: Boolean = true) :Thread() {
        private var second : Int = song.second
        private var mills:Float = (second * 1000).toFloat()

        override fun run() {
            super.run()
            try {
                while(true) {
                    if (second >= playTime) {
                        second = 0
                        mills = 0f
                        resetMedia()
                        if (binding.songRepeatIv.visibility == View.VISIBLE) {  //한번 재생
                            runOnUiThread{
                                setPlayerStatus(false)
                            }
                        }
                        else {  //반복재생->완성
                            runOnUiThread{
                                setPlayerStatus(true)
                            } // 노래 다시 시작
                        }
                    }
                    if(isPlaying) {
                        sleep(50)
                        mills += 50

                        runOnUiThread {
                            binding.songProgressSb.progress = ((mills/playTime)*100).toInt()
                        }
                        if (mills%1000 == 0f) {
                            runOnUiThread {
                                binding.songStartTimeTv.text = String.format("%02d:%02d", second/60, second%60)
                            }
                            second++
                        }
                    }
                }
            }catch(e:InterruptedException) {
                Log.d("Song","쓰레가 죽었습니다 ${e.message}")
            }
        }
    }

    private fun initClickListener() {   //클릭 리스너 모음!
        binding.songMiniplayerIv.setOnClickListener {
            serviceStart()
            setPlayerStatus(true)
        }
        binding.songPauseIv.setOnClickListener {
            setPlayerStatus(false)
        }

        binding.songRepeatIv.setOnClickListener {
            setRepeatStatus(false)
        }
        binding.songRepeatOnIv.setOnClickListener {
            setRepeatStatus(true)
        }

        binding.songRandomIv.setOnClickListener {
            setRandomStatus(false)
        }
        binding.songRandomOnIv.setOnClickListener {
            setRandomStatus(true)
        }

        //이전곡 , 다음곡
        binding.songNextIv.setOnClickListener{
            moveSong(1)
        }
        binding.songPreviousIv.setOnClickListener {
            moveSong(-1)
        }

        //좋아요 버튼
        binding.songLikeIv.setOnClickListener {
            setLike(songs[nowPos].isLike)
        }
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
        song = songs[nowPos]  //현재곡 갱신
        timer.interrupt()
        resetMedia()
        startTimer()
        setPlayer(song)
    }

    private fun setLike(isLike:Boolean) {
        song.isLike = !isLike
        songDB.SongDao().updateIsLikeByID(!isLike, song.id)

        //custom toast message
        var v1 = layoutInflater.inflate(R.layout.layout_toast, null)
        var text  : TextView = v1.findViewById(R.id.tvSample)       //toast layout에서 textview 가져오기

        val t2 = Toast.makeText(this, "", Toast.LENGTH_SHORT)
        t2.setGravity(Gravity.FILL_HORIZONTAL, 0, 800)

        if(!isLike) {
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_on)

            text.text ="좋아요 한 곡에 담았습니다."
            t2.view = v1 // setView 메서드를 사용하여 레이아웃을 설정
            t2.show()
        }
        else {
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_off)

            text.text ="좋아요를 취소했습니다."
            t2.view = v1 // setView 메서드를 사용하여 레이아웃을 설정
            t2.show()
        }
    }

    fun setPlayerStatus(isPlaying : Boolean) {
        song.isPlaying = isPlaying
        timer.isPlaying = isPlaying

        if (isPlaying) {
            binding.songMiniplayerIv.visibility = View.GONE
            binding.songPauseIv.visibility = View.VISIBLE
            mediaPlayer?.start()
        }
        else {
            binding.songMiniplayerIv.visibility = View.VISIBLE
            binding.songPauseIv.visibility = View.GONE
            if (mediaPlayer?.isPlaying == true) {
                mediaPlayer?.pause()
            }
        }
    }

    fun setRepeatStatus(isPlaying : Boolean) {
        if (isPlaying) {
            binding.songRepeatIv.visibility = View.VISIBLE
            binding.songRepeatOnIv.visibility = View.GONE
        }
        else {
            binding.songRepeatIv.visibility = View.GONE
            binding.songRepeatOnIv.visibility = View.VISIBLE
        }
    }

    fun setRandomStatus(isPlaying : Boolean) {
        if (isPlaying) {
            binding.songRandomIv.visibility = View.VISIBLE
            binding.songRandomOnIv.visibility = View.GONE
        }
        else {
            binding.songRandomIv.visibility = View.GONE
            binding.songRandomOnIv.visibility = View.VISIBLE
        }
    }
}
package com.nick.wedding.surpport

import android.app.Application
import android.content.Context
import android.media.MediaPlayer
import com.nick.wedding.R

object WuBaiMediaPlayer {

    lateinit var application : Context
    lateinit var mediaPlayer : MediaPlayer

    fun setMediaPlayerContext(context: Context) {
        this.application = context
        mediaPlayer = MediaPlayer.create(application, R.raw.wu_bai_till_the_end_of_time_1)
        mediaPlayer.isLooping = true
    }

    /** @param Int R.raw.id */
    fun setBackgroudSong(songId: Int){
        mediaPlayer = MediaPlayer.create(application, songId)
    }

    fun stopMediaPlayer(){
        if (mediaPlayer.isPlaying)
            mediaPlayer.pause()
    }

    fun startMediaPlayer(){
        if (!mediaPlayer.isPlaying)
            mediaPlayer.start()
    }

}
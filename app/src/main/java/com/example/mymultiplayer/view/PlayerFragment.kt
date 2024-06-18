package com.example.mymultiplayer.view

import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.fragment.app.Fragment
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.Player.STATE_BUFFERING
import androidx.media3.common.Player.STATE_ENDED
import androidx.media3.common.Player.STATE_READY
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.hls.HlsMediaSource
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import com.example.mymultiplayer.databinding.FragmentPlayerBinding
import com.example.mymultiplayer.model.Video
import com.example.mymultiplayer.model.getAllVideos
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.lang.Thread.sleep
import java.util.Timer
import java.util.TimerTask

class PlayerFragment : Fragment() {
    private val TAG = PlayerFragment::class.simpleName
    private var binding: FragmentPlayerBinding? = null
    private val mediaUrl = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyrides.mp4"
    private var player: ExoPlayer? = null
    private val dataSourceFactory: DataSource.Factory = DefaultHttpDataSource.Factory()
    private lateinit var videoList: ArrayList<Video>
    private var indexVDO = 0;

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(TAG, "onCreateView")
        binding = FragmentPlayerBinding.inflate(inflater, container, false)
        val view = binding?.root

        videoList = getAllVideos(requireActivity())
        Log.d(TAG, "videoList.size: " + videoList.size)

        initPlayer()
        binding?.tvStop?.setOnClickListener {
            player?.playWhenReady = false;
            player?.seekTo(0)
            indexVDO = 0;
        }
        binding?.tvPlay?.setOnClickListener {
            play() //tvPlay
        }
        binding?.tvPause?.setOnClickListener {
            pause()
        }

        return view
    }

    override fun onPause() {
        super.onPause()
        pause()
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "play onResume")
        play() //onResume
    }

    override fun onDestroy() {
        super.onDestroy()
        releasePlayer()
    }

    @OptIn(UnstableApi::class)
    private fun initPlayer() {
        player = ExoPlayer.Builder(requireActivity()).build().apply {
                    val mediaItem = MediaItem.fromUri(videoList[indexVDO].artUri)
                    setMediaItem(mediaItem)
                    prepare()
                    addListener(playerListener)
                }
    }

    @OptIn(UnstableApi::class)
    private fun getHlsMediaSource(): MediaSource {
        return HlsMediaSource.Factory(dataSourceFactory).createMediaSource(MediaItem.fromUri(mediaUrl))
    }

    @OptIn(UnstableApi::class)
    private fun getProgressiveMediaSource(): MediaSource {
        return ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(MediaItem.fromUri(Uri.parse(mediaUrl)))
    }

    private fun releasePlayer() {
        player?.apply {
            playWhenReady = false
            release()
        }
        player = null
    }

    private fun pause() {
        player?.playWhenReady = false
    }

    private fun play() {
        player?.playWhenReady = true
    }

    private fun restartPlayer() {
        player?.seekTo(0)
        player?.playWhenReady = true
    }

    private val playerListener = object : Player.Listener {
        override fun onPlaybackStateChanged(playbackState: Int) {
            super.onPlaybackStateChanged(playbackState)
            when (playbackState) {
                STATE_ENDED -> {
                    Log.d(TAG, "STATE_ENDED")
                    restartPlayer()
                }
                STATE_READY -> {
                    Log.d(TAG, "STATE_READY onPlaybackStateChanged playWhenReady: " + player?.playWhenReady)
                    if(player?.playWhenReady == true) {
                        binding?.playerView?.player = player
                        Timer().schedule(object : TimerTask() { //set timer to prevent pre-initializing portrait vdo
                            override fun run() {
                                requireActivity().runOnUiThread{
                                    play() //onPlaybackStateChanged
                                    binding?.playerView?.visibility = VISIBLE
                                }
                            }
                        }, 100)

                    }
                }
                STATE_BUFFERING -> {
                    Log.d(TAG, "STATE_BUFFERING")
                }
                Player.STATE_IDLE -> {
                    Log.d(TAG, "STATE_IDLE")
                }
            }
        }
    }
}
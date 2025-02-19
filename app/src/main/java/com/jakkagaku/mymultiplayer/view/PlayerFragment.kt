package com.jakkagaku.mymultiplayer.view

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
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
import com.jakkagaku.mymultiplayer.databinding.FragmentPlayerBinding
import com.jakkagaku.mymultiplayer.model.MediaModel
import com.jakkagaku.mymultiplayer.viewmodel.MediaViewModel
import java.util.Timer
import java.util.TimerTask

class PlayerFragment : Fragment() {
    private val TAG = PlayerFragment::class.simpleName
    private var binding: FragmentPlayerBinding? = null
    private lateinit var mediaViewModel: MediaViewModel
    private val mediaUrl = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyrides.mp4"
    private var player: ExoPlayer? = null
    private val dataSourceFactory: DataSource.Factory = DefaultHttpDataSource.Factory()
    private lateinit var mediaModelList: ArrayList<MediaModel>
    private var indexMedia = 0;

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(TAG, "onCreateView")
        binding = FragmentPlayerBinding.inflate(inflater, container, false)
        val view = binding?.root
        return view
    }

    @RequiresApi(Build.VERSION_CODES.Q) override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "onViewCreated")
        super.onViewCreated(view, savedInstanceState)
        mediaViewModel = ViewModelProvider(requireActivity()).get(MediaViewModel::class.java)
        mediaModelList = mediaViewModel.getAllMedia(requireActivity())
        Log.d(TAG, "videoList.size: " + mediaModelList.size)
        initPlayer()
        binding?.tvStop?.setOnClickListener {
            player?.playWhenReady = false;
            player?.seekTo(0)
            indexMedia = 0;
        }
        binding?.tvPlay?.setOnClickListener { play() } //tvPlay
        binding?.tvPause?.setOnClickListener {
            pause()
        }
        binding?.tvNext?.setOnClickListener {
            indexMedia = calculateNextIndex(indexMedia, mediaModelList.size)
            Log.d(TAG, "indexVDO: $indexMedia")
            val mediaItem = MediaItem.fromUri(mediaModelList[indexMedia].artUri)
            player?.setMediaItem(mediaItem)
            player?.prepare()
        }
        binding?.tvPrev?.setOnClickListener {
            indexMedia = calculatePrevIndex(indexMedia, mediaModelList.size)
            Log.d(TAG, "indexVDO: $indexMedia")
            val mediaItem = MediaItem.fromUri(mediaModelList[indexMedia].artUri)
            player?.setMediaItem(mediaItem)
            player?.prepare()
        }

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

    override fun onStop() {
        super.onStop()
        binding?.playerView?.visibility = INVISIBLE
        releasePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    @OptIn(UnstableApi::class) private fun initPlayer() {
        Log.d(TAG, "mediaModelList.size: " + mediaModelList.size);
        if (mediaModelList.size > 0) {
            player = ExoPlayer.Builder(requireActivity()).build().apply {
                val mediaItem = MediaItem.fromUri(mediaModelList[indexMedia].artUri)
                setMediaItem(mediaItem)
                prepare()
                addListener(playerListener)
            }
        }
    }

    @OptIn(UnstableApi::class) private fun getHlsMediaSource(): MediaSource {
        return HlsMediaSource.Factory(dataSourceFactory).createMediaSource(MediaItem.fromUri(mediaUrl))
    }

    @OptIn(UnstableApi::class) private fun getProgressiveMediaSource(): MediaSource {
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
                    if (player?.playWhenReady == true) {
                        binding?.playerView?.player = player
                        Timer().schedule(object : TimerTask() { //set timer to prevent pre-initializing portrait vdo
                            override fun run() {
                                requireActivity().runOnUiThread {
                                    play() //onPlaybackStateChanged
                                    //TODO binding?.playerView?.visibility = VISIBLE
                                    val curLTitle = mediaModelList[indexMedia].title
                                    Log.d(TAG, "curLTitle: $curLTitle")
                                    binding?.textViewTitle?.text = "Title: "  + curLTitle
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

        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
            super.onMediaItemTransition(mediaItem, reason)
            val curLTitle = mediaModelList[indexMedia].title
            Log.d(TAG, "curLTitle: $curLTitle")
            binding?.textViewTitle?.text = "Title: "  + curLTitle
        }
    }

    fun calculateNextIndex(index: Int, size: Int): Int {
        try {
            var i = index
            i++
            if (i >= size) {
                i = size - 1
            } else if (i < 0) {
                i = 0
            }
            return i
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(TAG, "e: $e")
        }
        return 0;
    }

    fun calculatePrevIndex(index: Int, size: Int): Int {
        try {
            var i = index
            i--
            if (i < 0) {
                i = 0
            } else if (i > size) {
                i = size - 1
            }
            return i
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(TAG, "e: $e")
        }
        return 0;
    }
}
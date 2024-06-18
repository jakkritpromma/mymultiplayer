package com.example.mymultiplayer.view

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.fragment.app.Fragment
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
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

class PlayerFragment : Fragment() {
    private val TAG = PlayerFragment::class.simpleName
    private var binding: FragmentPlayerBinding? = null
    private val mediaUrl = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyrides.mp4"
    private var player: ExoPlayer?= null
    private val dataSourceFactory: DataSource.Factory = DefaultHttpDataSource.Factory()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(TAG, "onCreateView")
        binding = FragmentPlayerBinding.inflate(inflater, container, false)
        val view = binding?.root
        initPlayer()
        return view
    }
    override fun onPause() {
        super.onPause()
        pause()
    }

    override fun onResume() {
        super.onResume()
        play()
    }

    override fun onDestroy() {
        super.onDestroy()
        releasePlayer()
    }

    @OptIn(UnstableApi::class)
    private fun initPlayer(){
        player = ExoPlayer.Builder(requireActivity())
                .build()
                .apply {
                    val source = if (mediaUrl.contains("m3u8"))
                        getHlsMediaSource()
                    else
                        getProgressiveMediaSource()
                    setMediaSource(source)
                    prepare()
                    addListener(playerListener)
                }
    }

    @OptIn(UnstableApi::class)
    private fun getHlsMediaSource(): MediaSource {
        return HlsMediaSource.Factory(dataSourceFactory).
        createMediaSource(MediaItem.fromUri(mediaUrl))
    }

    @OptIn(UnstableApi::class)
    private fun getProgressiveMediaSource(): MediaSource{
        return ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(MediaItem.fromUri(Uri.parse(mediaUrl)))
    }

    private fun releasePlayer(){
        player?.apply {
            playWhenReady = false
            release()
        }
        player = null
    }

    private fun pause(){
        player?.playWhenReady = false
    }

    private fun play(){
        player?.playWhenReady = true
    }

    private fun restartPlayer(){
        player?.seekTo(0)
        player?.playWhenReady = true
    }

    private val playerListener = object: Player.Listener {
        override fun onPlaybackStateChanged(playbackState: Int) {
            super.onPlaybackStateChanged(playbackState)
            when(playbackState){
                STATE_ENDED -> restartPlayer()
                STATE_READY -> {
                    binding?.playerView?.player = player
                    play()
                }
            }
        }
    }
}
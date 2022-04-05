package com.codeChallenge.olympicChannel.util

import android.content.Context
import android.net.Uri

import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource


class SimplePlayer {
    private var simpleExoPlayer: SimpleExoPlayer? = null
    fun initPLayer(context: Context) {

        val loadControl = DefaultLoadControl.Builder()
            .setBufferDurationsMs(25000, 50000, 1500, 2000).createDefaultLoadControl()

        val extensionRendererMode =
            DefaultRenderersFactory.EXTENSION_RENDERER_MODE_PREFER

        val renderersFactory =
            DefaultRenderersFactory(context).setExtensionRendererMode(extensionRendererMode)


        simpleExoPlayer =
            SimpleExoPlayer.Builder(context, renderersFactory).setLoadControl(loadControl).build()

    }

    fun setMediaSource(link: String) {
        val dataSourceFactory = DefaultHttpDataSource.Factory()
        val mediaSource = HlsMediaSource.Factory(dataSourceFactory).createMediaSource(
            MediaItem.fromUri(link)
        )
        simpleExoPlayer?.addMediaSource(mediaSource)

        startPlayer()
    }

    fun startPlayer() {
        simpleExoPlayer?.prepare()
    }

    fun stopPlayer() {
        simpleExoPlayer?.stop()
    }

    fun releasePlayer() {
        simpleExoPlayer?.release()
    }

    fun addListenerToPlayer(
        onErrorListener: ((error: PlaybackException) -> Unit)? = null,
        onPlayerStart: (() -> Unit)? = null,
        onPlayerEnded: (() -> Unit)? = null
    ) {
        simpleExoPlayer?.addListener(object : Player.Listener {
            override fun onPlayerError(error: PlaybackException) {
                super.onPlayerError(error)
                onErrorListener?.invoke(error)
            }

            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                when(playbackState){
                    Player.STATE_READY -> {
                        onPlayerStart?.invoke()
                    }
                    Player.STATE_ENDED -> {
                        onPlayerEnded?.invoke()
                    }
                    Player.STATE_BUFFERING -> {

                    }
                    Player.STATE_IDLE -> {

                    }
                }
            }
        })
    }

    fun setPlayerFromView(playerView: PlayerView) {
        playerView.apply {
            player = simpleExoPlayer
        }

    }


}
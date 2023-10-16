package com.webnexahindicartoon

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.SparseArray
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.util.containsKey
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaItem.fromUri
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.MergingMediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.ui.PlayerView
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.maxrave.kotlinyoutubeextractor.State
import com.maxrave.kotlinyoutubeextractor.YTExtractor
import com.maxrave.kotlinyoutubeextractor.YtFile
import com.webnexahindicartoon.adapter.playerVideoAdapter
import com.webnexahindicartoon.databinding.ActivityPlayerBinding
import com.webnexahindicartoon.modal.videosModal
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


@UnstableApi
class PlayerActivity : AppCompatActivity() {

    private lateinit var playerView: PlayerView
    private lateinit var player: ExoPlayer
    lateinit var ytFiles: SparseArray<YtFile>
    lateinit var binding: ActivityPlayerBinding
    private lateinit var videoAdapter: playerVideoAdapter
    private var isFullScreen = false

    @SuppressLint("StaticFieldLeak", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)

        setContentView(binding.root)

//        val videoId = intent.getStringExtra("videoId")
        val videoLink = intent.getStringExtra("videoLink")
        val videoTitle = intent.getStringExtra("videoTitle")
        val videoCate = intent.getStringExtra("videoCate")
        val videoCateImage = intent.getStringExtra("videoCateImage")
        val videoViews = intent.getStringExtra("videoViews")

        val receivedVideosList =
            intent.getSerializableExtra("videosList") as ArrayList<videosModal>?

        binding.videoTitle.text = videoTitle
        binding.videoSubtitle.text = "$videoViews views"
        binding.cname.text = videoCate

        Glide.with(this).load(videoCateImage)
            .into(binding.channelLogo)

        playerView = findViewById(R.id.player_view)

//        val progress=playerView.findViewById<ProgressBar>(R.id.progress)

        player = ExoPlayer.Builder(this).build()
        playerView.player = player

        val videoId = videoLink?.let { extractYouTubeVideoId(it) }
        val yt = YTExtractor(con = this, CACHING = false, LOGGING = false, retryCount = 3)

        binding.videoRecycleView.layoutManager =
            LinearLayoutManager(
                this@PlayerActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
        videoAdapter = playerVideoAdapter(this@PlayerActivity, ArrayList())
        binding.videoRecycleView.adapter = videoAdapter
        if (receivedVideosList != null) {
            videoAdapter.updateVideo(receivedVideosList)
        }

        GlobalScope.launch {
            if (videoId != null) {
                yt.extract(videoId)
            }

            Handler(Looper.getMainLooper()).post {

                if (yt.state == State.SUCCESS) {

                    ytFiles = yt.getYTFiles()!!
                   

                    if (ytFiles.containsKey(133) && ytFiles[133]?.url != null) {
                        val videoUrl = ytFiles.get(133).url
                        val audioUrl = ytFiles.get(140).url
                        val audioSrc: ProgressiveMediaSource =
                            ProgressiveMediaSource.Factory(DefaultHttpDataSource.Factory())
                                .createMediaSource(
                                    fromUri(Uri.parse(audioUrl))
                                )
                        val videoSrc: ProgressiveMediaSource =
                            ProgressiveMediaSource.Factory(DefaultHttpDataSource.Factory())
                                .createMediaSource(
                                    fromUri(Uri.parse(videoUrl))
                                )


                        player.setMediaSource(MergingMediaSource(true, videoSrc, audioSrc), true)

                        player.prepare()
                        player.playWhenReady = true
                        if (!player.isPlaying) {
                            player.play()
                        }
                        val currentPosition = player.currentPosition

                        val newPosition = currentPosition + 1000

                        player.seekTo(newPosition)
                    } else if (ytFiles.containsKey(134) && ytFiles[134]?.url != null) {
                        val videoUrl = ytFiles.get(134)?.url
                        val audioUrl = ytFiles.get(140)?.url

                        val dataSourceFactory = DefaultHttpDataSource.Factory()

                        val videoSource = ProgressiveMediaSource.Factory(dataSourceFactory)
                            .createMediaSource(fromUri(Uri.parse(videoUrl)))

                        val audioSource = ProgressiveMediaSource.Factory(dataSourceFactory)
                            .createMediaSource(fromUri(Uri.parse(audioUrl)))

                        val mergingSource = MergingMediaSource(videoSource, audioSource)

                        player.setMediaSource(mergingSource)

                        player.prepare()
                        player.playWhenReady = true
                        player.prepare()
                        if (!player.isPlaying) {
                            player.play()
                        }
                        val currentPosition = player.currentPosition

                        val newPosition = currentPosition + 1000

                        player.seekTo(newPosition)
                    } else {
                      
                        val videoUrl = ytFiles.get(18)?.url
                        val mediaItem: MediaItem = fromUri(Uri.parse(videoUrl))

                        player.addMediaItem(mediaItem)
                        player.prepare()
                        player.playWhenReady = true
                        if (!player.isPlaying) {
                            player.play()
                        }

                        val currentPosition = player.currentPosition

                        val newPosition = currentPosition + 1000

                        player.seekTo(newPosition)

                    }
                }
            }
        }

        playerView.setFullscreenButtonClickListener {
            if (isFullScreen) {
                exitFullscreen()
                isFullScreen = false
                playerView.layoutParams.height =
                    resources.getDimensionPixelSize(R.dimen.player_view_height)
            } else {
                enterFullscreen()
                isFullScreen = true
                playerView.layoutParams.height = ConstraintLayout.LayoutParams.MATCH_PARENT

            }
        }
//        player.addListener(
//            object : Player.Listener {
//                override fun onIsPlayingChanged(isPlaying: Boolean) {
//                    if (isPlaying) {
////                        progress.visibility=View.GONE
//
//                    } else {
////                       progress.visibility=View.VISIBLE
//                    }
//                }
//            }
//        )


    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (isFullScreen) {
            exitFullscreen()
        } else {
            super.onBackPressed()
        }
    }

    override fun onPause() {
        super.onPause()
        player.pause()
    }

    override fun onStop() {
        super.onStop()
        player.release()
    }

    fun extractYouTubeVideoId(youtubeUrl: String): String? {
        var videoId: String? = null

        val patterns = listOf(
            "https?://(?:www\\.)?youtube\\.com/watch\\?v=([\\w-]+)",
            "https?://(?:www\\.)?youtube\\.com/watch\\?t=\\d+&v=([\\w-]+)",
            "https?://(?:www\\.)?youtube\\.com/v/([\\w-]+)",
            "https?://(?:www\\.)?youtube\\.com/embed/([\\w-]+)",
            "https?://youtu\\.be/([\\w-]+)"
        )

        for (pattern in patterns) {
            val regex = Regex(pattern)
            val matchResult = regex.find(youtubeUrl)
            if (matchResult != null && matchResult.groupValues.size > 1) {
                videoId = matchResult.groupValues[1]
                break
            }
        }

        return videoId
    }

    private fun enterFullscreen() {

        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        playerView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)


    }

    private fun exitFullscreen() {
        // Show system UI, set portrait orientation, and adjust layout back to normal
        window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        playerView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
    }
}

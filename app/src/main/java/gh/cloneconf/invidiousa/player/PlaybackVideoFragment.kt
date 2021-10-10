package gh.cloneconf.invidiousa.player

import android.net.Uri
import android.os.Bundle
import androidx.leanback.app.VideoSupportFragment
import androidx.leanback.app.VideoSupportFragmentGlueHost
import androidx.leanback.media.MediaPlayerAdapter
import androidx.leanback.media.PlaybackTransportControlGlue
import androidx.leanback.widget.PlaybackControlsRow
import androidx.lifecycle.lifecycleScope
import gh.cloneconf.invidiousa.api.Invidious
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/** Handles video playback with media controls. */
class PlaybackVideoFragment : VideoSupportFragment() {

    private lateinit var mTransportControlGlue: PlaybackTransportControlGlue<MediaPlayerAdapter>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch(Dispatchers.IO) {
            val info = Invidious.video(requireArguments().getString("id")!!)

            withContext(Dispatchers.Main) {
                val glueHost = VideoSupportFragmentGlueHost(this@PlaybackVideoFragment)
                val playerAdapter = MediaPlayerAdapter(activity)
                playerAdapter.setRepeatAction(PlaybackControlsRow.RepeatAction.INDEX_NONE)

                mTransportControlGlue = PlaybackTransportControlGlue(activity, playerAdapter)
                mTransportControlGlue.host = glueHost
                mTransportControlGlue.title = info.title
                mTransportControlGlue.subtitle = info.description
                mTransportControlGlue.playWhenPrepared()

                playerAdapter.setDataSource(Uri.parse(info.url))
            }
        }


    }

    override fun onPause() {
        super.onPause()
        mTransportControlGlue.pause()
    }
}
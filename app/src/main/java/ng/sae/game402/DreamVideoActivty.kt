package ng.sae.game402

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.Player.REPEAT_MODE_OFF
import com.google.android.exoplayer2.ui.StyledPlayerView
import ng.sae.game402.ui.theme.Game402Theme

class DreamVideoActivty : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Game402Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DreamVideoScreen()
                }
            }
        }
    }
}


@Composable
fun DreamVideoScreen() {

    val context = LocalContext.current

    val firstVideoUri = Uri.parse("asset:///dreamvid.mp4")

    val mediaItem = MediaItem.Builder()
        .setUri(firstVideoUri)
        .build()
    val exoPlayer = remember(context, mediaItem) {
        ExoPlayer.Builder(context)
            .build()
            .also { exoPlayer ->
                exoPlayer.setMediaItem(mediaItem)
                exoPlayer.prepare()
                exoPlayer.playWhenReady = true
                exoPlayer.repeatMode = REPEAT_MODE_OFF
            }
    }

    exoPlayer.addListener(object : Player.Listener {
        override fun onPlaybackStateChanged(playbackState: Int) {
            when (playbackState) {
                Player.STATE_ENDED -> {
//                    context.startActivity(Intent(context, GameActivity::class.java))
                }
            }
        }
    })

    DisposableEffect(
        AndroidView(factory = {
            StyledPlayerView(context).apply {
                player = exoPlayer
                useController = false
            }
        })
    ) {
        onDispose {
            exoPlayer.release()
        }
    }


}
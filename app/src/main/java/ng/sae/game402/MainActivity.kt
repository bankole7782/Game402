package ng.sae.game402

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ng.sae.game402.ui.theme.Game402Theme
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

var globalMediaPlayer: MediaPlayer? = null

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Declaring and Initializing
        // the MediaPlayer to play g402s.mp3
        val context = applicationContext
        val audioUri = getUriFromAsset(context, "g402s.mp3")

        if (globalMediaPlayer == null)  {
            val mMediaPlayer = MediaPlayer.create(this, audioUri)
            globalMediaPlayer = mMediaPlayer

            globalMediaPlayer?.setOnCompletionListener(MediaPlayer.OnCompletionListener { mPlayer ->
                mPlayer.reset()
                if (audioUri != null) {
                    mPlayer.setDataSource(context, audioUri)
                }
                mPlayer.prepare()
                mPlayer.start()
            })

            globalMediaPlayer!!.start()
        }

        setContent {
            Game402Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    GameIntroScreen()
                }
            }
        }
    }
}


@Composable
fun GameIntroScreen() {
    val context = LocalContext.current
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.g402bg),
            contentDescription = "background",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.matchParentSize()
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Game402!", fontSize = 30.sp, fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = "A game about speaking!",
            )
            Spacer(modifier = Modifier.height(5.dp))

            Button(onClick = {
                context.startActivity(Intent(context, DreamVideoActivity::class.java))
            }) {
                Text("Begin")
            }
        }
    }

}


private fun getUriFromAsset(context: Context, assetFileName: String): Uri? {
    val assetManager = context.assets
    var inputStream: InputStream? = null
    var outputStream: FileOutputStream? = null
    var tempFile: File? = null

    return try {
        inputStream = assetManager.open(assetFileName)
        tempFile = File.createTempFile("temp_asset", null, context.cacheDir)
        outputStream = FileOutputStream(tempFile)

        inputStream.copyTo(outputStream)

        Uri.fromFile(tempFile)
    } catch (e: IOException) {
        e.printStackTrace()
        null
    } finally {
        inputStream?.close()
        outputStream?.close()
        tempFile?.deleteOnExit()
    }
}
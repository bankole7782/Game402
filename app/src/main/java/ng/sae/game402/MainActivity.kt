package ng.sae.game402

import android.content.Intent
import android.os.Bundle
import android.util.Log
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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Game402Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainGameScreen()
                }
            }
        }
    }
}


@Composable
fun MainGameScreen() {
    val context = LocalContext.current
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.g402bg),
            contentDescription = "background",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.matchParentSize()
        )

        Column(
            modifier = Modifier.fillMaxSize()
                .padding(10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "God Asks:", fontSize = 40.sp, fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(text = "What did you See?", fontSize = 30.sp,)
            Text(text = "In order my child?", fontSize = 30.sp,)
            Spacer(modifier = Modifier.height(5.dp))

            val data = remember { mutableStateOf(listOf("house", "car", "goat")) }
            val state = rememberReorderableLazyListState(onMove = { from, to ->
                data.value = data.value.toMutableList().apply {
                    add(to.index, removeAt(from.index))
                }
            })
            LazyColumn(
                state = state.listState,
                modifier = Modifier
                    .reorderable(state)
                    .detectReorderAfterLongPress(state)
            ) {
                items(data.value.size, { it }) { item ->
                    ReorderableItem(state, key = item) { isDragging ->
                        val elevation = animateDpAsState(if (isDragging) 16.dp else 0.dp)
                        Column(
                            modifier = Modifier
                                .shadow(elevation.value)
//                        .background(Color.Gray)
                        ) {
                            Text(
                                data.value[item], fontSize = 30.sp,
                                modifier = Modifier
                                    .padding(10.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(5.dp))

            Button(onClick = {
                Log.v("values", data.toString())
            }) {
                Text("Speak")
            }
        }
    }

}

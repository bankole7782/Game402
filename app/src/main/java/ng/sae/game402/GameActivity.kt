package ng.sae.game402

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
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

class GameActivity : ComponentActivity() {
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
    val toSort = """
    30 apples, 12 bananas, 20 oranges
    3 laptops, 2 phones, 1 charger
    1 boy, 4 rats, 20 snakes
    50 books, 1 certificate, 1 pen
    2 hammers, 40 nails, 6 pins
    300 mangoes, 1 plate of rice, 2 cups of water
    1 movie, 1 song, 1 book
    3 girls, 20 apples, 1 spaghetti
    100 snails, 1 chicken, 2 cups of water
    40 trousers, 2 cloth material, 1 cloth-styles book
    Heaven, kings' palace, school
    Sun, Trees, Air
    Preparation, Unread Books, a gun
    University, Secondary School, Primary School
    a Job, 1000 dollars, a plate of rice
    """.trimIndent()

    var toSortList: ArrayList<List<String>> = ArrayList<List<String>>()
    val partsOfToSort = toSort.split("\n")
    for (elem in partsOfToSort) {
        val parts = elem.split(",")
        var toInsert = ArrayList<String>()
        for (p in parts) {
            toInsert.add(p.trim())
        }
        toSortList.add(toInsert)
    }

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

            var displayIndex = 0
            val stateFile = File(context.getExternalFilesDir(""), "state.txt")
            if (stateFile.exists()) {
                val rawStateData = stateFile.readText()
                displayIndex = rawStateData.toInt() + 1
                if (displayIndex >= toSortList.size) {
                    displayIndex = 0

                }
            }
            val data = remember { mutableStateOf( toSortList[displayIndex].shuffled()) }
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
                Log.v("data value", data.value.toString())
                Log.v("toSortList[2]", toSortList[displayIndex].toString())

                if (toSortList[displayIndex].equals(data.value)) {
//                    Log.v("values", "true")
                    Toast.makeText(context,
                        "Good my child",
                        Toast.LENGTH_SHORT).show()
                    context.startActivity(Intent(context, DreamVideoActivity::class.java))
                    stateFile.writeText((displayIndex).toString())
                } else {
                    Toast.makeText(context,
                        "Speak in order my child. Touch and hold for a while then drag to reorder",
                        Toast.LENGTH_SHORT).show()
                }

            }) {
                Text("Speak")
            }
        }
    }

}

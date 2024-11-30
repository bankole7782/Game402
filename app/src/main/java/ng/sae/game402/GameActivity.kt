package ng.sae.game402

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import ng.sae.game402.ui.theme.Game402Theme
//import org.burnoutcrew.reorderable.ReorderableItem
//import org.burnoutcrew.reorderable.detectReorderAfterLongPress
//import org.burnoutcrew.reorderable.rememberReorderableLazyListState
//import org.burnoutcrew.reorderable.reorderable
import java.io.File
import java.util.Collections


private val itemHeightDp = 60.dp
private var itemHeightPx = 0

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
    Heaven, 1 palace, 1 school
    Sun, Trees, Air
    Preparation, Unread Books, 1 gun
    1 Job, 1000 dollars, 1 plate of rice
    6 shoes, 10 sandals, 1 shirt
    1 English Book, 1 Maths book, 1 Physics book
    1 factory, 17 farms, 200 bands
    1 trailer of cement, 1 trailer of rocks, 2 trailers of sand
    1 bank job, 1 military job, 3 chef jobs
    2 songs, 10 talks, 1 blog post
    20 bikes, 1 car, 3 men
    1 bag of rice, 3 bags of maize, 3 bags of beans
    1 trailer, 3 vehicles, 20 bikes
    2 laptops, 40 spanners, 60 nuts
    1 builder, 1 teacher, 3 programmers
    browser program, music player, text editor
    keyboard, touchscreen, mouse
    1 cow, 1 goat, 3 snails
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

            myList = toSortList[displayIndex].shuffled().toMutableList()

            DragToReorderList(Modifier)

            Spacer(modifier = Modifier.height(5.dp))

            Button(onClick = {

//                Log.v("data value", data.value.toString())
//                Log.v("toSortList[2]", toSortList[displayIndex].toString())
//
                if (toSortList[displayIndex].equals(myList)) {
//                    Log.v("values", "true")
                    Toast.makeText(context,
                        "Good my child",
                        Toast.LENGTH_LONG).show()
                    context.startActivity(Intent(context, DreamVideoActivity::class.java))
                    stateFile.writeText((displayIndex).toString())
                } else {
                    Toast.makeText(context,
                        "Speak in order my child. Touch and hold for a while then drag to reorder",
                        Toast.LENGTH_LONG).show()
                }

                Log.v("myList", myList.toString())

            }) {
                Text("Speak")
            }
        }
    }

}

private var myList = buildList {
    for (i in 0..20) {
        add("Item $i")
    }
}.toMutableList()

@Composable
fun DragToReorderList(modifier: Modifier) {
    val listState = rememberLazyListState()
    val isPlaced = remember { mutableStateOf(false) }
    val currentIndex = remember { mutableIntStateOf(-1) }
    val destinationIndex = remember { mutableIntStateOf(0) }
    val slideStates = remember {
        mutableStateMapOf<String, SlideState>()
            .apply {
                myList.associateWith { SlideState.NONE }.also { putAll(it) }
            }
    }

    LaunchedEffect(isPlaced.value) {
        if (isPlaced.value) {
            launch {
                if (currentIndex.intValue != destinationIndex.intValue) {
                    Collections.swap(myList, currentIndex.intValue, destinationIndex.intValue)
                    slideStates.apply {
                        myList.associateWith { SlideState.NONE }.also { putAll(it) }
                    }
                }
                isPlaced.value = false
            }
        }
    }

    with(LocalDensity.current) {
        itemHeightPx = itemHeightDp.toPx().toInt()
    }

    LazyColumn(state = listState) {
        items(myList.size) { idx ->

            val myItem = myList.getOrNull(idx) ?: return@items

            val slideState = slideStates[myItem] ?: SlideState.NONE

            val verticalTranslation by animateIntAsState(
                targetValue = when (slideState) {
                    SlideState.UP -> -itemHeightPx
                    SlideState.DOWN -> itemHeightPx
                    else -> 0
                },
                label = "drag_to_reorder_vertical_translation"
            )

            key(myItem) {
                Box(
                    modifier = Modifier
                        .fillParentMaxWidth()
                        .height(itemHeightDp)
                        .padding(horizontal = 12.dp)
                        .dragToReorder(
                            item = myItem,
                            itemList = myList,
                            itemHeight = itemHeightPx,
                            updateSlideState = { param: String, state: SlideState -> slideStates[param] = state },
                            onStartDrag = { index -> currentIndex.intValue = index },
                            onStopDrag = { currIndex: Int, destIndex: Int ->
                                isPlaced.value = true
                                currentIndex.intValue = currIndex
                                destinationIndex.intValue = destIndex
                            })
                        .offset { IntOffset(0, verticalTranslation) }
                ) {
                    Text(
                        text = myItem, fontSize=25.sp, modifier = Modifier
                            .fillMaxWidth()
//                            .background(Color.LightGray)
                            .border(2.dp, Color.LightGray, RoundedCornerShape(15.dp))
                            .padding(8.dp)
                            .padding(start=20.dp)
                    )
                }
            }
        }
    }
}
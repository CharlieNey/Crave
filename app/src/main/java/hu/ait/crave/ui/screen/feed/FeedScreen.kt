package hu.ait.crave.ui.screen.feed


import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.icons.filled.Face6
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import hu.ait.crave.R
import hu.ait.crave.data.Post
import hu.ait.crave.ui.screen.eggyolkColor
import hu.ait.crave.ui.screen.recipe.RecipeScreen
import hu.ait.crave.ui.theme.Eggshell
import hu.ait.crave.ui.theme.Eggyoke
import hu.ait.crave.ui.theme.LightYellow
import hu.ait.crave.ui.theme.Orange
import hu.ait.crave.ui.theme.Yellow80

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(
    feedScreenViewModel: FeedScreenViewModel = viewModel(),
    onNavigateToWritePost: () -> Unit,
    onNavigateToMyRecipeScreen: () -> Unit
)  {
    val postListState = feedScreenViewModel.postsList().collectAsState(
        initial = MainScreenUIState.Init)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentSize(Alignment.Center)
                    ) {
                        Text(
                            text = "Crave",
                            fontFamily = FontFamily(Font(R.font.aovelsansrounded_rddl)),
                            fontSize = 55.sp,
                            modifier = Modifier
                                .padding(16.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
                actions = {
                    IconButton(
                        onClick = {
                            onNavigateToMyRecipeScreen()
                        }
                    ) {
                        Icon(Icons.Filled.Face6, contentDescription = "Info")
                    }
                }
            )

        },
        floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        onNavigateToWritePost()
                    },
                    containerColor = Orange,
                    shape = CutCornerShape(20.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Add,
                        contentDescription = "Add",
                        tint = Color.White,
                    )
                }
        }
    ) {Box(
        modifier = Modifier
            .fillMaxSize()
            .background(eggyolkColor) // Change to your "eggyolk" color
    ) {
        Column(modifier = Modifier.padding(it)) {
            // Rest of your code
            if (postListState.value == MainScreenUIState.Init) {
                Text(
                    text = "Init...",
                    fontFamily = FontFamily(Font(R.font.aovelsansrounded_rddl))
                )
            } else if (postListState.value is MainScreenUIState.Success) {
                LazyColumn() {
                    items((postListState.value as MainScreenUIState.Success).postList) {
                        PostCard(
                            post = it.post,
                            onRemoveItem = {
                                feedScreenViewModel.deletePost(it.postId)
                            },
                            onLikeClick = {
                                feedScreenViewModel.likePost(it.postId)
                            },
                            onDislikeClick = {
                                feedScreenViewModel.unlikePost(it.postId)
                            },
                            currentUserId = feedScreenViewModel.currentUserId
                        )
                    }
                }
            }
        }
    }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostCard(
    post: Post.Post,
    onRemoveItem: () -> Unit = {},
    onLikeClick: () -> Unit = {},
    onDislikeClick: () -> Unit = {},
    currentUserId: String = ""
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        colors = CardDefaults.cardColors(
            containerColor =
            //MaterialTheme.colorScheme.surfaceVariant,
            Eggshell
        ),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        modifier = Modifier.padding(5.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp)
        ) {
            Row(
                modifier = Modifier.padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Text(
                        text = post.author,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily(Font(R.font.aovelsansrounded_rddl))

                    )

                    Spacer(modifier = Modifier.size(10.dp))

                    Text(
                        text = post.title,
                        fontSize = 20.sp,
                        fontFamily = FontFamily(Font(R.font.aovelsansrounded_rddl))
                    )

//                    Text(
//                        text = post.body,
//                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (currentUserId.equals(post.uid)) {
                        Icon(
                            imageVector = Icons.Outlined.Delete,
                            contentDescription = "Delete",
                            modifier = Modifier.clickable {
                                onRemoveItem()
                            },
                            tint = Color.Red
                        )
                    }

                    Spacer(modifier = Modifier.size(20.dp))


                    if (!post.likedBy.contains(currentUserId)) {
                        val heartPainter = painterResource(id = R.drawable.heart)

                        Icon(painter = heartPainter,
                            contentDescription = "Like",
                            modifier = Modifier.clickable {
                                onLikeClick()
                            }
                                .size(23.dp)
                        )
                    } else {
                        val filledHeartPainter = painterResource(id = R.drawable.heartfilled)

                        Icon(painter = filledHeartPainter,
                            contentDescription = "Liked",
                            modifier = Modifier.clickable {
                                onDislikeClick()
                            }
                                .size(23.dp)

                        )
                    }
                    Spacer(modifier = Modifier.size(10.dp))
                    Text(post.likes.toString(), fontFamily = FontFamily(Font(R.font.aovelsansrounded_rddl)))
                }


                IconButton(
                    onClick = { expanded = !expanded }
                ) {
                    Icon(
                        imageVector = if (expanded)
                            Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = if (expanded) {
                            "Less"
                        } else {
                            "More"
                        }
                    )
                }

                // Show the description if expanded

            }
            if (expanded) {
                Spacer(modifier = Modifier.size(10.dp))
                Text(text = post.body, fontFamily = FontFamily(Font(R.font.aovelsansrounded_rddl)))
            }

        }


        if (post.imgUrl != "") {
            Log.e("camera", "photo exists...")
            AsyncImage(
                model = post.imgUrl,
                modifier = Modifier.size(100.dp, 100.dp),
                contentDescription = "selected image"
            )
        }

    }
}



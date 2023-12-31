package hu.ait.crave.ui.screen.feed


import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import hu.ait.crave.R
import hu.ait.crave.data.Post
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
                            .wrapContentSize()

                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.craverectangle),
                            contentDescription = stringResource(R.string.logo),
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
                        Icon(Icons.Filled.Face6, contentDescription = stringResource(R.string.info))
                    }
                }
            )

        },
        floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        onNavigateToWritePost()
                    },
                    containerColor = Yellow80,
                    shape = CutCornerShape(20.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Add,
                        contentDescription = stringResource(R.string.add),
                        tint = Color.White,
                    )
                }
        }
    ) {Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White) // Change to your "eggyolk" color
    ) {
        Column(modifier = Modifier.padding(it)) {
            // Rest of your code
            if (postListState.value == MainScreenUIState.Init) {
                Text(
                    text = "Init...",
                    fontFamily = FontFamily(Font(R.font.opensans))
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
            Yellow80
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
                        fontFamily = FontFamily(Font(R.font.opensans))

                    )

                    Spacer(modifier = Modifier.size(10.dp))

                    Text(
                        text = post.title,
                        fontSize = 20.sp,
                        fontFamily = FontFamily(Font(R.font.opensans))
                    )

                }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (currentUserId == post.uid) {
                        Icon(
                            imageVector = Icons.Outlined.Delete,
                            contentDescription = stringResource(R.string.delete),
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
                            contentDescription = stringResource(R.string.like),
                            modifier = Modifier
                                .clickable {
                                    onLikeClick()
                                }
                                .size(23.dp)
                        )
                    } else {
                        val filledHeartPainter = painterResource(id = R.drawable.heartfilled)

                        Icon(painter = filledHeartPainter,
                            contentDescription = stringResource(R.string.liked),
                            modifier = Modifier
                                .clickable {
                                    onDislikeClick()
                                }
                                .size(23.dp)

                        )
                    }
                    Spacer(modifier = Modifier.size(10.dp))
                    Text(post.likes.toString(), fontFamily = FontFamily(Font(R.font.opensans)))
                }


                IconButton(
                    onClick = { expanded = !expanded }
                ) {
                    Icon(
                        imageVector = if (expanded)
                            Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = if (expanded) {
                            stringResource(R.string.less)
                        } else {
                            stringResource(R.string.more)
                        }
                    )
                }

                // Show the description if expanded

            }
            if (expanded) {
                Spacer(modifier = Modifier.size(10.dp))
                Text(text = stringResource(R.string.description1) + post.body, fontFamily = FontFamily(Font(R.font.opensans)))
                Text(text = stringResource(R.string.ingredients2) + post.ingredients, fontFamily = FontFamily(Font(R.font.opensans)))
            }

        }


        if (post.imgUrl != "") {
            Log.e("camera", "photo exists...")
            AsyncImage(
                model = post.imgUrl,
                modifier = Modifier.size(100.dp, 100.dp),
                contentDescription = stringResource(R.string.selected_image)
            )
        }

    }
}



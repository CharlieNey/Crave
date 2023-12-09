package hu.ait.crave.ui.screen.feed


import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import hu.ait.crave.R
import hu.ait.crave.data.Post
import hu.ait.crave.ui.screen.recipe.RecipeScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(
    feedScreenViewModel: FeedScreenViewModel = viewModel(),
    onNavigateToWritePost: () -> Unit,
    onNavigateToMyRecipeScreen: () -> Unit
) {
    val postListState = feedScreenViewModel.postsList().collectAsState(
        initial = MainScreenUIState.Init)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crave") },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor =
                    MaterialTheme.colorScheme.secondaryContainer
                ),
                actions = {
                    IconButton(
                        onClick = {
                            onNavigateToMyRecipeScreen()
                        }
                    ) {
                        Icon(Icons.Filled.Info, contentDescription = "Info")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onNavigateToWritePost()
                },
                containerColor = MaterialTheme.colorScheme.secondary,
                shape = RoundedCornerShape(16.dp),
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = "Add",
                    tint = Color.White,
                )
            }
        }
    ) {
        Column(modifier = Modifier.padding(it)) {

            if (postListState.value == MainScreenUIState.Init) {
                Text(text = "Init...")
            } else if (postListState.value is MainScreenUIState.Success) {
                LazyColumn() {
                    items((postListState.value as MainScreenUIState.Success).postList){
                        PostCard(post = it.post,
                            onRemoveItem = {
                                feedScreenViewModel.deletePost(it.postId)
                            },
                            onLikeClick = {
                                feedScreenViewModel.likePost(it.postId)
                            },
                            currentUserId = feedScreenViewModel.currentUserId)
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
    currentUserId: String = ""
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
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
                        fontWeight = FontWeight.Bold

                    )

                    Spacer(modifier = Modifier.size(10.dp))

                    Text(
                        text = post.title,
                        fontSize = 20.sp
                    )

                    Text(
                        text = post.body,
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (currentUserId.equals(post.uid)) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Delete",
                            modifier = Modifier.clickable {
                                onRemoveItem()
                            },
                            tint = Color.Red
                        )
                    }

                    if (!post.likedBy.contains(currentUserId)) {
                        Icon(imageVector = Icons.Outlined.ThumbUp,
                            contentDescription = "Like",
                            modifier = Modifier.clickable {
                                    onLikeClick()
                            }
                        )
                    }
                    else {
                        Icon(imageVector = Icons.Filled.ThumbUp,
                            contentDescription = "Liked")
                    }
                    Text(post.likes.toString())
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
}
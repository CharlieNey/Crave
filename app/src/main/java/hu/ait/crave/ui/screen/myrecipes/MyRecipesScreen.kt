package hu.ait.crave.ui.screen.myrecipes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.rounded.Add
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import hu.ait.crave.R
import hu.ait.crave.ui.screen.eggyolkColor
import hu.ait.crave.ui.screen.feed.FeedScreenViewModel
import hu.ait.crave.ui.screen.feed.MainScreenUIState
import hu.ait.crave.ui.screen.feed.PostCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyRecipesScreen(
    feedScreenViewModel: FeedScreenViewModel = viewModel(),

    onNavigateToFeedScreen: () -> Unit
) {
    val postListState = feedScreenViewModel.postsList().collectAsState(
        initial = MainScreenUIState.Init)
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(R.string.my_recipes),
                        modifier = Modifier
                            .fillMaxSize()
                            .wrapContentSize(Alignment.Center),
                        style = TextStyle(
                            fontFamily = FontFamily(Font(R.font.opensans)),
                            fontSize = 45.sp
                        )
                    )
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Color.White
                ),
                actions = {
                    IconButton(
                        onClick = {
                            onNavigateToFeedScreen()
                        }
                    ) {
                        Icon(Icons.Filled.Home, contentDescription = "Info")
                    }
                }
            )
        },
        ) {
        Column(modifier = Modifier
            .background(eggyolkColor)
            .fillMaxSize()
            .padding(it)) {

            if (postListState.value == MainScreenUIState.Init) {
                Text(text = stringResource(R.string.init))
            } else if (postListState.value is MainScreenUIState.Success) {
                LazyColumn() {
                    items((postListState.value as MainScreenUIState.Success).postList){
                        var currentUserId = feedScreenViewModel.currentUserId
                        if (currentUserId.equals(it.post.uid)) {
                            PostCard(
                                post = it.post,
                                onRemoveItem = {
                                    feedScreenViewModel.deletePost(it.postId)
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

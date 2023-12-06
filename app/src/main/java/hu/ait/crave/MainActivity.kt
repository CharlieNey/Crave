package hu.ait.crave

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import hu.ait.crave.ui.navigation.Screen
import hu.ait.crave.ui.screen.feed.FeedScreen
import hu.ait.crave.ui.screen.login.LoginScreen
import hu.ait.crave.ui.screen.writepost.WritePostScreen
import hu.ait.crave.ui.theme.CraveTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CraveTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavGraph()
                }
            }
        }
    }
}



@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Feed.route)
                }
            )
        }
        composable(Screen.Feed.route) {
            FeedScreen(
                onNavigateToWritePost = {
                    navController.navigate(Screen.WritePost.route)
                }
            )
        }
        composable(Screen.WritePost.route) {
            WritePostScreen()
        }
    }
}

package hu.ait.crave.ui.screen.writepost

import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import hu.ait.crave.R
import hu.ait.crave.ui.navigation.Screen
import java.io.File


@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun WritePostScreen(
    writePostScreenViewModel: WritePostScreenViewModel = viewModel(),
    onNavigateToFeedScreen: () -> Unit
) {
    var postTitle by remember { mutableStateOf("") }
    var postBody by remember { mutableStateOf("") }

    val context = LocalContext.current

    var hasImage by remember {
        mutableStateOf(false)
    }
    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            hasImage = success
        }
    )
    val cameraPermissionState = rememberPermissionState(
        android.Manifest.permission.CAMERA
    )




    Column(
        modifier = Modifier.padding(20.dp)
    ) {
        OutlinedTextField(value = postTitle,
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = "Post title") },
            onValueChange = {
                postTitle = it
            }
        )
        OutlinedTextField(value = postBody,
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = "Post body") },
            onValueChange = {
                postBody = it
            }
        )

        // permission here...
        if (cameraPermissionState.status.isGranted) {
            Button(onClick = {
                val uri = ComposeFileProvider.getImageUri(context)
                imageUri = uri
                cameraLauncher.launch(uri) // opens the built in camera
            }) {
                Text(text = "Take photo")
            }
        } else {
            Column() {
                val permissionText = if (cameraPermissionState.status.shouldShowRationale) {
                    "Please reconsider giving the camera persmission it is needed if you want to take photo for the message"
                } else {
                    "Give permission for using photos with items"
                }
                Text(text = permissionText)
                Button(onClick = {
                    cameraPermissionState.launchPermissionRequest()
                }) {
                    Text(text = "Request permission")
                }
            }
        }


        if (hasImage && imageUri != null) {
            AsyncImage(model = imageUri,
                modifier = Modifier.size(200.dp, 200.dp),
                contentDescription = "selected image")
        }

        Button(onClick = {
            if (imageUri == null) {
                writePostScreenViewModel.uploadPost(postTitle, postBody)
            } else {
                writePostScreenViewModel
                    .uploadPostImage(
                        context.contentResolver,
                        imageUri!!,
                        postTitle,
                        postBody
                    )
            }
           onNavigateToFeedScreen()
        }) {
            Text(text = "Upload")
        }

        when (writePostScreenViewModel.writePostUiState) {
            is WritePostUiState.LoadingPostUpload -> CircularProgressIndicator()
            is WritePostUiState.PostUploadSuccess -> {
                Text(text = "Post uploaded.")
            }
            is WritePostUiState.ErrorDuringPostUpload ->
                Text(text =
                "${(writePostScreenViewModel.writePostUiState as WritePostUiState.ErrorDuringPostUpload).error}")

            is WritePostUiState.LoadingImageUpload -> CircularProgressIndicator()
            is WritePostUiState.ImageUploadSuccess -> {
                Text(text = "Image uploaded, starting post upload.")
            }
            is WritePostUiState.ErrorDuringImageUpload ->
                Text(text = "${(writePostScreenViewModel.writePostUiState as WritePostUiState.ErrorDuringImageUpload).error}")


            else -> {}
        }
    }
}

class ComposeFileProvider : FileProvider(
    R.xml.filepaths
) {
    companion object {
        fun getImageUri(context: Context): Uri {
            val directory = File(context.cacheDir, "images")
            directory.mkdirs()
            val file = File.createTempFile(
                "selected_image_",
                ".jpg",
                directory,
            )
            val authority = context.packageName + ".fileprovider"
            return getUriForFile(
                context,
                authority,
                file,
            )
        }
    }
}
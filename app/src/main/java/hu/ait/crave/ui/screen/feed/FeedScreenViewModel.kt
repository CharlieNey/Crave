package hu.ait.crave.ui.screen.feed

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import hu.ait.crave.data.Post
import hu.ait.crave.ui.screen.writepost.WritePostScreenViewModel


import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

class FeedScreenViewModel : ViewModel() {
    var currentUserId: String = Firebase.auth.currentUser!!.uid

    fun deletePost(postKey: String) {
        FirebaseFirestore.getInstance().collection(
            WritePostScreenViewModel.COLLECTION_POSTS
        ).document(postKey).delete()
    }


    fun likePost(postId: String) {
        val postRef = FirebaseFirestore.getInstance()
            .collection(WritePostScreenViewModel.COLLECTION_POSTS)
            .document(postId)

        postRef.update(
                    mapOf(
                        "likes" to FieldValue.increment(1),
                        "likedBy" to FieldValue.arrayUnion(currentUserId)
                    ))
    }

    fun unlikePost(postId: String) {
        val postRef = FirebaseFirestore.getInstance()
            .collection(WritePostScreenViewModel.COLLECTION_POSTS)
            .document(postId)

        postRef.update(
            mapOf(
                "likes" to FieldValue.increment(-1),
                "likedBy" to FieldValue.arrayRemove(currentUserId)
            ))
    }

    fun postsList() = callbackFlow {
        val snapshotListener =
            FirebaseFirestore.getInstance().collection(WritePostScreenViewModel.COLLECTION_POSTS)
                .addSnapshotListener() { snapshot, e ->
                    val response = if (snapshot != null) {
                        val postList = snapshot.toObjects(Post.Post::class.java)
                        val postWithIdList = mutableListOf<Post.PostWithId>()

                        postList.forEachIndexed { index, post ->
                            postWithIdList.add(Post.PostWithId(snapshot.documents[index].id, post))
                        }

                        MainScreenUIState.Success(
                            postWithIdList
                        )
                    } else {
                        MainScreenUIState.Error(e?.message.toString())
                    }

                    trySend(response) // emit this value through the flow
                }
        awaitClose {
            snapshotListener.remove()
        }
    }
}


sealed interface MainScreenUIState {
    object Init : MainScreenUIState
    data class Success(val postList: List<Post.PostWithId>) : MainScreenUIState
    data class Error(val error: String?) : MainScreenUIState
}
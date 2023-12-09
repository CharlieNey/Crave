package hu.ait.crave.data

class Post {
    data class Post(
        var uid: String = "",
        var author: String = "",
        var title: String = "",
        var body: String = "",
        var imgUrl: String = "",
        val likes: Int = 0,
        val likedBy: List<String> = emptyList()


    )

    data class PostWithId(
        val postId: String,
        val post: Post
    )
}
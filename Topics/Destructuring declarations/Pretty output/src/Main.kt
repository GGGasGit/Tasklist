data class Comment(val id: Int, val body: String, val author: String)

fun printComments(commentsData: MutableList<Comment>) {
    commentsData.forEach { println("Author: ${it.author}; Text: ${it.body}") }
}
class Article(val name: String, val pages: Int, val author: String) {
    operator fun component1(): String = name
    operator fun component2(): Int = pages
    operator fun component3(): String = author
}

fun getArticleByName(articles: MutableList<Article>, name: String): Article? {
    var index = -1
    for ((title, ) in articles) {
        index += 1
        if (title == name) return articles[index]
    }
    return null
}
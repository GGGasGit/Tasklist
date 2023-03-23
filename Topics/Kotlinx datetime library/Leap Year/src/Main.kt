import kotlinx.datetime.*

fun isLeapYear(year: String): Boolean {
    return try {
        val date = Instant.parse("$year-02-29T01:00:00Z")
        true
    } catch (e: RuntimeException) {
        false
    }
}

fun main() {
    val year = readln()
    println(isLeapYear(year))
}
import kotlinx.datetime.*

fun nextMonth(date: String): String {
    val instant = Instant.parse(date)
    val period: DateTimePeriod = DateTimePeriod(months = 1)
    return instant.plus(period, TimeZone.UTC).toString()
}

fun main() {
    val date = readln()
    println(nextMonth(date))
}
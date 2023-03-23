import kotlinx.datetime.*

fun howManyDays(year1: Int, month1: Int, day1: Int, year2: Int, month2: Int, day2: Int): Int {
    val month1String = if (month1 < 10) "0$month1" else "$month1"
    val month2String = if (month2 < 10) "0$month2" else "$month2"
    val day1String = if (day1 < 10) "0$day1" else "$day1"
    val day2String = if (day2 < 10) "0$day2" else "$day2"
    val instant1 = Instant.parse("$year1-$month1String-${day1String}T00:00:00Z")
    val instant2 = Instant.parse("$year2-$month2String-${day2String}T00:00:00Z")
    return instant1.until(instant2, DateTimeUnit.DAY, TimeZone.UTC).toInt()
}

fun main() {
    val (year1, month1, day1) = readln().split(" ").map { it.toInt() }
    val (year2, month2, day2) = readln().split(" ").map { it.toInt() }

    println(howManyDays(year1, month1, day1, year2, month2, day2))
}
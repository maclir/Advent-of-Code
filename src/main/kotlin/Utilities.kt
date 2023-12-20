import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun Pair<Int, Int>.manhattanDistance(to: Pair<Int, Int>) = abs(first - to.first) + abs(second - to.second)

fun Int.leastCommonMultiple(other: Int): Int {
    val smaller = min(this, other)
    val bigger = max(this, other)
    var multiple = 1
    while (bigger * multiple % smaller != 0) multiple++
    return bigger * multiple
}

fun Long.leastCommonMultiple(other: Long): Long {
    val smaller = min(this, other)
    val bigger = max(this, other)
    var multiple = 1
    while (bigger * multiple % smaller != 0L) multiple++
    return bigger * multiple
}

fun List<Int>.leastCommonMultiple(): Long {
    var result = this[0].toLong()
    for (i in 1 until this.size) {
        result = result.leastCommonMultiple(this[i].toLong())
    }
    return result
}

fun <E> Iterable<E>.indicesOf(e: E) = mapIndexedNotNull { index, elem -> index.takeIf { elem == e } }

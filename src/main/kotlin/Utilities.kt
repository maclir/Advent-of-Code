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

fun <E> Iterable<E>.indicesOf(e: E)
        = mapIndexedNotNull{ index, elem -> index.takeIf{ elem == e } }

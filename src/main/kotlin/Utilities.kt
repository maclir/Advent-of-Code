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

enum class Direction {
    UP {
        override fun move(current: Node, maxRow: Int, maxCol: Int) =
            if (current.row - 1 < 0) null else Node(current.row - 1, current.col)

        override fun moveIgnoreBounds(current: Node) = Node(current.row - 1, current.col)

        override fun multiplier() = -1L to 0L
    },
    DOWN {
        override fun move(current: Node, maxRow: Int, maxCol: Int) =
            if (current.row + 1 > maxRow) null else Node(current.row + 1, current.col)

        override fun moveIgnoreBounds(current: Node) = Node(current.row + 1, current.col)

        override fun multiplier() = 1L to 0L
    },
    RIGHT {
        override fun move(current: Node, maxRow: Int, maxCol: Int) =
            if (current.col + 1 > maxCol) null else Node(current.row, current.col + 1)

        override fun moveIgnoreBounds(current: Node) = Node(current.row, current.col + 1)

        override fun multiplier() = 0L to 1L
    },
    LEFT {
        override fun move(current: Node, maxRow: Int, maxCol: Int) =
            if (current.col - 1 < 0) null else Node(current.row, current.col - 1)

        override fun moveIgnoreBounds(current: Node) = Node(current.row, current.col - 1)

        override fun multiplier() = 0L to -1L
    };

    abstract fun move(current: Node, maxRow: Int, maxCol: Int): Node?
    abstract fun moveIgnoreBounds(current: Node): Node
    abstract fun multiplier(): Pair<Long, Long>
}

data class Node(val row: Int, val col: Int)

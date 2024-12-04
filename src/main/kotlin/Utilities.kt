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

fun <E> List<E>.safeAccess(index: Int) = if (index in indices) this[index] else null

enum class Direction {
    UP {
        override fun move(current: Node, maxRow: Int, maxCol: Int) =
            if (current.row - 1 < 0) null else Node(current.row - 1, current.col)

        override fun moveIgnoreBounds(current: Node, step: Int) = Node(current.row - step, current.col)

        override fun multiplier() = -1L to 0L
    },
    DOWN {
        override fun move(current: Node, maxRow: Int, maxCol: Int) =
            if (current.row + 1 > maxRow) null else Node(current.row + 1, current.col)

        override fun moveIgnoreBounds(current: Node, step: Int) = Node(current.row + step, current.col)

        override fun multiplier() = 1L to 0L
    },
    RIGHT {
        override fun move(current: Node, maxRow: Int, maxCol: Int) =
            if (current.col + 1 > maxCol) null else Node(current.row, current.col + 1)

        override fun moveIgnoreBounds(current: Node, step: Int) = Node(current.row, current.col + step)

        override fun multiplier() = 0L to 1L
    },
    LEFT {
        override fun move(current: Node, maxRow: Int, maxCol: Int) =
            if (current.col - 1 < 0) null else Node(current.row, current.col - 1)

        override fun moveIgnoreBounds(current: Node, step: Int) = Node(current.row, current.col - step)

        override fun multiplier() = 0L to -1L
    };

    abstract fun move(current: Node, maxRow: Int, maxCol: Int): Node?
    abstract fun moveIgnoreBounds(current: Node, step: Int = 1): Node
    abstract fun multiplier(): Pair<Long, Long>
}

enum class DiagonalDirection {
    UP_RIGHT {
        override fun move(current: Node, step: Int) = Node(current.row - step, current.col + step)
    },
    UP_LEFT {
        override fun move(current: Node, step: Int) = Node(current.row - step, current.col - step)
    },
    DOWN_RIGHT {
        override fun move(current: Node, step: Int) = Node(current.row + step, current.col + step)
    },
    DOWN_LEFT {
        override fun move(current: Node, step: Int) = Node(current.row + step, current.col - step)
    };

    abstract fun move(current: Node, step: Int = 1): Node
}

data class Node(val row: Int, val col: Int) {
    var visited: Boolean = false
    fun move(direction: Direction, step: Int = 1) = direction.moveIgnoreBounds(this, step)
    fun move(direction: DiagonalDirection, step: Int = 1) = direction.move(this, step)
}

fun List<List<Char>>.atNode(node: Node) = this[node.row][node.col]
fun List<List<Char>>.atNodeSafe(node: Node) = this.safeAccess(node.row)?.safeAccess(node.col)

fun <T> List<T>.combinations(size: Int): List<List<T>> = when (size) {
    0 -> listOf(listOf())
    else -> flatMapIndexed { idx, element -> drop(idx + 1).combinations(size - 1).map { listOf(element) + it } }
}

fun String.intLines(delimiters: String = " ") = this.lines().map { it.split(delimiters).map { digits -> digits.toInt() } }

fun String.longLines(delimiters: String = " ") = this.lines().map { it.split(delimiters).map { digits -> digits.toLong() } }
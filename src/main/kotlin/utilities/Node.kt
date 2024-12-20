package utilities

interface Direction {
    abstract fun move(current: Node, step: Int = 1): Node
}

enum class BaseDirection : Direction {
    UP {
        override fun opposite() = DOWN
        override fun turnClockwise() = RIGHT
        override fun move(current: Node, step: Int) = Node(current.row - step, current.col)
    },
    DOWN {
        override fun opposite() = UP
        override fun turnClockwise() = LEFT
        override fun move(current: Node, step: Int) = Node(current.row + step, current.col)
    },
    RIGHT {
        override fun opposite() = LEFT
        override fun turnClockwise() = DOWN
        override fun move(current: Node, step: Int) = Node(current.row, current.col + step)
    },
    LEFT {
        override fun opposite() = RIGHT
        override fun turnClockwise() = UP
        override fun move(current: Node, step: Int) = Node(current.row, current.col - step)
    };

    abstract fun turnClockwise(): BaseDirection
    abstract fun opposite(): BaseDirection
    fun isHorizontal() = this == RIGHT || this == LEFT
    fun isVertical() = !isHorizontal()
}

enum class DiagonalDirection : Direction {
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
}

val allDirections: List<Direction> = BaseDirection.entries.plus(DiagonalDirection.entries)

data class Node(val row: Int, val col: Int) {
    fun move(direction: Direction, step: Int = 1) = direction.move(this, step)
    fun move(rowStep: Int, colStep: Int) = Node(row + rowStep, col + colStep)
    fun adjacent() = BaseDirection.entries.map { direction -> direction.move(this) }
    fun surrounding() = allDirections.map { direction -> direction.move(this) }
    fun <T> isInMap(map: List<List<T>>) = row in 0..map.lastIndex && col in 0..map[0].lastIndex
    fun warpInMapMove(map: List<List<Any>>, rowStep: Int, colStep: Int) = this.move(rowStep, colStep).warpInMap(map)
    fun warpInMap(map: List<List<Any>>) = Node(
        (row % map.size + map.size) % map.size,
        (col % map[0].size + map[0].size) % map[0].size,
    )
}

fun <T> List<List<T>>.atNode(node: Node) = this[node.row][node.col]
fun <T> List<List<T>>.atNodeSafe(node: Node) = this.safeAccess(node.row)?.safeAccess(node.col)
fun <T> List<List<T>>.atNodeOrDefault(node: Node, default: T) =
    this.safeAccess(node.row)?.safeAccess(node.col) ?: default

fun <T> List<MutableList<T>>.setNode(node: Node, t: T): List<MutableList<T>> {
    this[node.row][node.col] = t
    return this
}

fun <E> List<List<E>>.forEachNode(action: (node: Node, E) -> Unit) {
    this.forEachIndexed { rIndex, r ->
        r.forEachIndexed { cIndex, c ->
            action(Node(rIndex, cIndex), c)
        }
    }
}
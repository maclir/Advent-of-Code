package utilities

interface Direction {
    abstract fun move(current: Node, step: Int = 1): Node
}

enum class BaseDirection : Direction {
    UP {
        override fun move(current: Node, step: Int) = Node(current.row - step, current.col)
    },
    DOWN {
        override fun move(current: Node, step: Int) = Node(current.row + step, current.col)
    },
    RIGHT {
        override fun move(current: Node, step: Int) = Node(current.row, current.col + step)
    },
    LEFT {
        override fun move(current: Node, step: Int) = Node(current.row, current.col - step)
    };

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
    fun adjacent() = BaseDirection.entries.map { direction -> direction.move(this) }
    fun surrounding() = allDirections.map { direction -> direction.move(this) }
}

fun List<List<Char>>.atNode(node: Node) = this[node.row][node.col]
fun List<List<Char>>.atNodeSafe(node: Node) = this.safeAccess(node.row)?.safeAccess(node.col)
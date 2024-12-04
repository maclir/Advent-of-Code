package utilities

enum class Direction {
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

    abstract fun move(current: Node, step: Int = 1): Node
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
    fun move(direction: Direction, step: Int = 1) = direction.move(this, step)
    fun move(direction: DiagonalDirection, step: Int = 1) = direction.move(this, step)
}

fun List<List<Char>>.atNode(node: Node) = this[node.row][node.col]
fun List<List<Char>>.atNodeSafe(node: Node) = this.safeAccess(node.row)?.safeAccess(node.col)
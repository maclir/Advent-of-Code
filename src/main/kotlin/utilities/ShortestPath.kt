package utilities

import java.util.*


fun <T> Node.shortestPath(map: List<List<T>>, checkWall: (nodeValue: T) -> Boolean): Map<Node, Int> {
    val start = this
    val queue: LinkedList<Node> = LinkedList<Node>()
    queue.add(start)

    val dist = mutableMapOf<Node, Int>()
    dist[Node(start.row, start.col)] = 0

    while (!queue.isEmpty()) {
        val node = queue.removeFirst()
        BaseDirection.entries.forEach { direction ->
            val newNode = node.move(direction)
            if (!newNode.isInMap(map)) return@forEach
            if (dist.contains(newNode)) return@forEach

            if (!checkWall(map.atNode(newNode))) {
                dist[newNode] = 1 + dist[node]!!
                queue.add(newNode)
            }
        }
    }
    return dist
}

fun <T> Node.shortestPath(map: List<List<T>>, end: Node, checkWall: (nodeValue: T) -> Boolean): Int {
    val start = this
    val queue: LinkedList<Node> = LinkedList<Node>()
    queue.add(start)

    val dist = mutableMapOf<Node, Int>()
    dist[Node(start.row, start.col)] = 0

    while (!queue.isEmpty()) {
        val node = queue.removeFirst()
        BaseDirection.entries.forEach { direction ->
            val newNode = node.move(direction)
            if (!newNode.isInMap(map)) return@forEach
            if (dist.contains(newNode)) return@forEach

            if (!checkWall(map.atNode(newNode))) {
                if (newNode == end) return 1 + dist[node]!!
                dist[newNode] = 1 + dist[node]!!
                queue.add(newNode)
            }
        }
    }
    return -1
}

fun <T> Node.shortestPathsDirections(
    map: List<List<T>>,
    checkWall: (nodeValue: T) -> Boolean
): Map<Node, List<List<BaseDirection>>> {
    val nodeDirections = mutableMapOf<Node, MutableList<List<BaseDirection>>>()

    val queue: LinkedList<Pair<List<BaseDirection>, List<Node>>> = LinkedList<Pair<List<BaseDirection>, List<Node>>>()
    queue.add(listOf<BaseDirection>() to listOf(this))

    while (!queue.isEmpty()) {
        val (nodePathDirections, nodePath) = queue.removeFirst()
        val node = nodePath.last()
        val nodeDistance = nodePath.size

        BaseDirection.entries.forEach { direction ->
            val newNode = node.move(direction)
            if (!newNode.isInMap(map)) return@forEach
            if (checkWall(map.atNode(newNode))) return@forEach

            val existingNewNodePathList = nodeDirections.getOrPut(newNode) {
                mutableListOf()
            }
            if (existingNewNodePathList.isEmpty() || existingNewNodePathList.first().size >= nodeDistance) {
                val newDirections = nodePathDirections.toMutableList().also { it.add(direction) }
                val newPath = nodePath.toMutableList().also { it.add(newNode) }
                existingNewNodePathList.add(newDirections)
                queue.add(newDirections to newPath)
            }
        }
    }

    nodeDirections[this] = mutableListOf()
    return nodeDirections
}
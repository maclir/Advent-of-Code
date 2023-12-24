package y2023.d23

import Direction
import Node
import atNode
import java.io.File
import kotlin.system.measureTimeMillis

private fun main() {
    val input = File(
//        "src/main/kotlin/y2023/d23/Input-test.txt"
        "src/main/kotlin/y2023/d23/Input.txt"
    ).readText(Charsets.UTF_8)

    println("${
        measureTimeMillis {
            println(
                "part1: ${part1(input)}"
            )
        }
    } ms\n")

    println("${
        measureTimeMillis {
            println(
                "part2: ${part2(input)}"
            )

        }
    } ms")
}

private fun part1(input: String): Int {
    val map = input.lines().map { it.toCharArray().toList() }

    val graph = mutableMapOf<Node, List<Node>>()
    map.forEachIndexed { rowIndex, row ->
        row.forEachIndexed { colIndex, _ ->
            val node = Node(rowIndex, colIndex)
            if (map.atNode(node) == '#') return@forEachIndexed
            val edges = mutableListOf<Node>()
            when (map.atNode(node)) {
                '<' -> edges.add(node.move(Direction.LEFT))
                '>' -> edges.add(node.move(Direction.RIGHT))
                'v' -> edges.add(node.move(Direction.DOWN))
                '^' -> edges.add(node.move(Direction.UP))
                else -> {
                    edges.addAll(
                        Direction.values()
                            .mapNotNull { direction -> direction.move(node, map.lastIndex, map[0].lastIndex) }
                            .filter { map.atNode(it) != '#' })
                }
            }

            graph[node] = edges
        }
    }

    fun longestPath(start: Node, end: Node, visited: MutableSet<Node>): Int {
        if (start == end) return 0
        val neighbors = graph[start]?.filter { it !in visited } ?: return Int.MIN_VALUE
        if (neighbors.isEmpty()) return Int.MIN_VALUE

        visited.add(start)
        val maxLength = neighbors.maxOf { neighbor -> 1 + longestPath(neighbor, end, visited) }
        visited.remove(start)

        return maxLength
    }

    return longestPath(Node(0, 1), Node(map.lastIndex, map[0].lastIndex - 1), mutableSetOf())
}

private fun part2(input: String): Int {
    val map = input.lines().map { it.toCharArray().toList() }

    val graph = mutableMapOf<Node, List<Node>>()
    map.forEachIndexed { rowIndex, row ->
        row.forEachIndexed { colIndex, _ ->
            val node = Node(rowIndex, colIndex)
            if (map.atNode(node) == '#') return@forEachIndexed
            graph[node] =
                Direction.values().mapNotNull { direction -> direction.move(node, map.lastIndex, map[0].lastIndex) }
                    .filter { map.atNode(it) != '#' }
        }
    }

    graph.forEach { (nodeKey, neighbors) ->
        graph[nodeKey] = neighbors.map { neighbor -> graph.keys.first { it == neighbor } }
    }

    fun longestPath(start: Node, end: Node): Int {
        if (start == end) return 0
        val neighbors = graph[start]?.filter { !it.visited } ?: return Int.MIN_VALUE
        if (neighbors.isEmpty()) return Int.MIN_VALUE

        start.visited = true
        val maxLength = neighbors.maxOf { neighbor -> 1 + longestPath(neighbor, end) }
        start.visited = false

        return maxLength
    }

    return longestPath(Node(0, 1), Node(map.lastIndex, map[0].lastIndex - 1))
}
package y2024.d10

import utilities.*
import java.io.File
import kotlin.system.measureTimeMillis

private fun main() {
    val input = File(
//        "src/main/kotlin/y2024/d10/Input-test.txt"
        "src/main/kotlin/y2024/d10/Input.txt"
    ).readText(Charsets.UTF_8)

    println(
        "${
        measureTimeMillis {
            println(
                "part1: ${part1(input)}"
            )
        }
    } ms\n")

    println(
        "${
        measureTimeMillis {
            println(
                "part2: ${part2(input)}"
            )

        }
    } ms")
}

private fun part1(input: String): Int {
    val map = input.intLines("")
    var sum = 0
    map.forEachIndexed { rowIndex, row ->
        row.forEachIndexed { colIndex, height ->
            if (height != 0) return@forEachIndexed
            sum += countReachable9s(map, Node(rowIndex, colIndex)).size
        }
    }
    return sum
}

private fun countReachable9s(map: List<List<Int>>, currentNode: Node): Set<Node> {
    val currentHeight = map.atNode(currentNode)
    if (currentHeight == 9) return setOf(currentNode)
    val reachable9s = mutableSetOf<Node>()
    BaseDirection.entries.forEach { direction ->
        val nextNode = currentNode.move(direction)
        if (map.atNodeSafe(nextNode) == currentHeight + 1) {
            reachable9s.addAll(countReachable9s(map, nextNode))
        }
    }
    return reachable9s
}

private fun part2(input: String): Int {
    val map = input.intLines("")
    var sum = 0
    map.forEachIndexed { rowIndex, row ->
        row.forEachIndexed { colIndex, height ->
            if (height != 0) return@forEachIndexed
            sum += countPaths(map, Node(rowIndex, colIndex))
        }
    }
    return sum
}

private fun countPaths(map: List<List<Int>>, currentNode: Node): Int {
    val currentHeight = map.atNode(currentNode)
    if (currentHeight == 9) return 1
    var validPaths = 0
    BaseDirection.entries.forEach { direction ->
        val nextNode = currentNode.move(direction)
        if (map.atNodeSafe(nextNode) == currentHeight + 1) {
            validPaths += countPaths(map, nextNode)
        }
    }
    return validPaths
}
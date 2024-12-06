package y2024.d06

import utilities.*
import java.io.File
import kotlin.system.measureTimeMillis

private fun main() {
    val input = File(
//        "src/main/kotlin/y2024/d06/Input-test.txt"
        "src/main/kotlin/y2024/d06/Input.txt"
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
    var currentNode = Node(0, 0)
    var currentDirection = BaseDirection.UP
    val map = input.lines().mapIndexed { rowIndex, row ->
        val colIndex = row.indexOf('^')
        if (colIndex != -1) {
            currentNode = Node(rowIndex, colIndex)
        }
        row.toCharArray().toList().map {
            if (it == '^') '.' else it
        }
    }

    val visited = mutableSetOf(currentNode)
    while (true) {
        val nextNode = currentNode.move(currentDirection)
        when (map.atNodeSafe(nextNode)) {
            '.' -> {
                currentNode = nextNode
                visited.add(currentNode)
            }

            '#' -> {
                currentDirection = currentDirection.turnClockwise()
            }

            null -> return visited.size
        }
    }
}

private fun part2(input: String): Int {
    var currentNode = Node(0, 0)
    var currentDirection = BaseDirection.UP
    val map = input.lines().mapIndexed { rowIndex, row ->
        val colIndex = row.indexOf('^')
        if (colIndex != -1) {
            currentNode = Node(rowIndex, colIndex)
        }
        row.toCharArray().toList().map {
            if (it == '^') '.' else it
        }
    }

    var count = 0
     map.forEachIndexed { rowIndex, row ->
        row.forEachIndexed { colIndex, c ->
            if (c == '.') {
                if (isStuckInLoop(map, Node(currentNode.row, currentNode.col), Node(rowIndex, colIndex))) count++
            }
        }
    }
    return count
}

private fun isStuckInLoop(map: List<List<Char>>, node: Node, block: Node): Boolean {
    var currentNode = node
    var currentDirection = BaseDirection.UP
    val visited = mutableSetOf(currentNode to currentDirection)
    while (true) {
        val nextNode = currentNode.move(currentDirection)
        if (nextNode == block) currentDirection = currentDirection.turnClockwise()
        else when (map.atNodeSafe(nextNode)) {

            '.' -> {
                currentNode = nextNode
                if (visited.contains(currentNode to currentDirection)) return true
                visited.add(currentNode to currentDirection)
            }

            '#' -> {
                currentDirection = currentDirection.turnClockwise()
            }

            null -> {
                return false
            }
        }
    }
}
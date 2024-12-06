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
        row.toCharArray().toList().mapIndexed { colIndex, c ->
            if (c == '^') {
                currentNode = Node(rowIndex, colIndex)
                '.'
            } else c
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
        row.toCharArray().toList().mapIndexed { colIndex, c ->
            if (c == '^') {
                currentNode = Node(rowIndex, colIndex)
                '.'
            } else c
        }
    }

    val initialNode = currentNode
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

            null -> break
        }
    }
    return visited.minus(initialNode).count {
        isStuckInLoop(map, Node(initialNode.row, initialNode.col), it)
    }
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
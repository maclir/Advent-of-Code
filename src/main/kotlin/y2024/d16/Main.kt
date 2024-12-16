package y2024.d16

import utilities.*
import java.io.File
import kotlin.system.measureTimeMillis

private fun main() {
    val input = File(
//        "src/main/kotlin/y2024/d16/Input-test.txt"
        "src/main/kotlin/y2024/d16/Input.txt"
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
    lateinit var currentNode: Node
    val map = input.lines().mapIndexed { rowIndex, row ->
        row.toCharArray().toList().mapIndexed { colIndex, c ->
            if (c == 'S') {
                currentNode = Node(rowIndex, colIndex)
                '.'
            } else c
        }
    }

    return finMinimumPath(map, currentNode, BaseDirection.LEFT, 0, setOf())
}

private val scoreAtPositions = mutableMapOf<Node, Int>()
private var scoreAtEnd = Int.MAX_VALUE
private val minimPaths = mutableSetOf<Node>()

private fun finMinimumPath(
    map: List<List<Char>>,
    currentNode: Node,
    currentDirection: BaseDirection,
    currentScore: Int,
    path: Set<Node>,
): Int {
    if (currentScore > scoreAtEnd) return Int.MAX_VALUE

    val scoreAtPosition = scoreAtPositions[currentNode] ?: Int.MAX_VALUE
    val possibleDirections = mutableSetOf<BaseDirection>()
    if (scoreAtPosition < currentScore) {
        possibleDirections.add(currentDirection)
        if (scoreAtPosition < currentScore - 1000) return Int.MAX_VALUE
    } else {
        possibleDirections.addAll(BaseDirection.entries)
        possibleDirections.remove(currentDirection.opposite())
        scoreAtPositions[currentNode] = currentScore
    }

    val newPath = path.toMutableSet()
    newPath.add(currentNode)
    if (map.atNodeSafe(currentNode) == 'E') {
        if (currentScore < scoreAtEnd) minimPaths.clear()
        scoreAtEnd = currentScore
        minimPaths.addAll(newPath)
        return currentScore
    }

    return possibleDirections.minOf { direction ->
        val nextNode = currentNode.move(direction)
        if (map.atNodeOrDefault(nextNode, '#') == '#') return@minOf Int.MAX_VALUE

        if (direction == currentDirection) finMinimumPath(map, nextNode, direction, currentScore + 1, newPath)
        else finMinimumPath(map, nextNode, direction, currentScore + 1000 + 1, newPath)
    }
}

private fun part2(input: String): Int {
    return minimPaths.size
}
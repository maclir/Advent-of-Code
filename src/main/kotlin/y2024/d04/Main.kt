package y2024.d04

import utilities.*
import java.io.File
import kotlin.system.measureTimeMillis

private fun main() {
    val input = File(
//        "src/main/kotlin/y2024/d04/Input-test.txt"
        "src/main/kotlin/y2024/d04/Input.txt"
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
    val grid = input.lines().map { it.toCharArray().toList() }
    var count = 0
    for (row in grid.indices) {
        for (col in grid[0].indices) {
            if (grid[row][col] == 'X') {
                val currentNode = Node(row, col)
                for (direction in BaseDirection.entries) {
                    if (
                        grid.atNodeSafe(currentNode.move(direction, 1)) == 'M' &&
                        grid.atNodeSafe(currentNode.move(direction, 2)) == 'A' &&
                        grid.atNodeSafe(currentNode.move(direction, 3)) == 'S'
                    ) count++
                }
                for (direction in DiagonalDirection.entries) {
                    if (
                        grid.atNodeSafe(currentNode.move(direction, 1)) == 'M' &&
                        grid.atNodeSafe(currentNode.move(direction, 2)) == 'A' &&
                        grid.atNodeSafe(currentNode.move(direction, 3)) == 'S'
                    ) count++
                }
            }
        }
    }
    return count
}

private fun part2(input: String): Int {
    val grid = input.lines().map { it.toCharArray().toList() }
    var count = 0
    for (row in grid.indices) {
        for (col in grid[0].indices) {
            if (grid[row][col] == 'A') {
                val currentNode = Node(row, col)
                var found = false
                for (direction in DiagonalDirection.entries) {
                    if (
                        grid.atNodeSafe(currentNode.move(direction, 1)) == 'M' &&
                        grid.atNodeSafe(currentNode.move(direction, -1)) == 'S'
                    ) {
                        if (found) count++
                        else found = true
                    }
                }
            }
        }
    }
    return count
}

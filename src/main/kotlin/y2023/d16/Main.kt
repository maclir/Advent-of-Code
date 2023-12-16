package y2023.d16

import java.io.File
import kotlin.math.max
import kotlin.system.measureTimeMillis

private fun main() {
    val input = File(
//        "src/main/kotlin/y2023/d16/Input-test.txt"
        "src/main/kotlin/y2023/d16/Input.txt"
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

private enum class Direction { UP, RIGHT, DOWN, LEFT }
private enum class Mirror {
    FORWARD_SLASH {
        override fun reflect(direction: Direction) = when (direction) {
            Direction.UP -> listOf(Direction.RIGHT)
            Direction.RIGHT -> listOf(Direction.UP)
            Direction.DOWN -> listOf(Direction.LEFT)
            Direction.LEFT -> listOf(Direction.DOWN)
        }
    },
    BACK_SLASH {
        override fun reflect(direction: Direction) = when (direction) {
            Direction.UP -> listOf(Direction.LEFT)
            Direction.RIGHT -> listOf(Direction.DOWN)
            Direction.DOWN -> listOf(Direction.RIGHT)
            Direction.LEFT -> listOf(Direction.UP)
        }
    },
    HORIZONTAL {
        override fun reflect(direction: Direction) = when (direction) {
            Direction.UP -> listOf(Direction.RIGHT, Direction.LEFT)
            Direction.RIGHT -> listOf(Direction.RIGHT)
            Direction.DOWN -> listOf(Direction.RIGHT, Direction.LEFT)
            Direction.LEFT -> listOf(Direction.LEFT)
        }
    },
    VERTICAL {
        override fun reflect(direction: Direction) = when (direction) {
            Direction.UP -> listOf(Direction.UP)
            Direction.RIGHT -> listOf(Direction.UP, Direction.DOWN)
            Direction.DOWN -> listOf(Direction.DOWN)
            Direction.LEFT -> listOf(Direction.UP, Direction.DOWN)
        }
    },
    DOT {
        override fun reflect(direction: Direction) = when (direction) {
            Direction.UP -> listOf(Direction.UP)
            Direction.RIGHT -> listOf(Direction.RIGHT)
            Direction.DOWN -> listOf(Direction.DOWN)
            Direction.LEFT -> listOf(Direction.LEFT)
        }
    };

    abstract fun reflect(direction: Direction): List<Direction>
}

private fun part1(input: String): Int {
    val map = input.lines().map { it.toCharArray().map {c ->
        when (c) {
            '/' -> Mirror.FORWARD_SLASH
            '\\' -> Mirror.BACK_SLASH
            '|' -> Mirror.VERTICAL
            '-' -> Mirror.HORIZONTAL
            else -> Mirror.DOT
        }
    }.toList() }
    val visited = mutableSetOf<Pair<Pair<Int, Int>, Direction>>()
    val energized = mutableSetOf<Pair<Int, Int>>()

    val queue = mutableListOf<Pair<Pair<Int, Int>, Direction>>()
    queue.add(Pair(0, 0) to Direction.RIGHT)
    while (queue.isNotEmpty()) {
        val start = queue.removeLast()
        var currentRow = start.first.first
        var currentCol = start.first.second
        var currentDirection = start.second
        while (currentRow >= 0 && currentCol >= 0 && currentRow < map.size && currentCol < map[0].size) {
            energized.add(currentRow to currentCol)
            val visitedKey = (currentRow to currentCol) to currentDirection
            if (visited.contains(visitedKey)) break
            visited.add(visitedKey)
            val newDirections = map[currentRow][currentCol].reflect(currentDirection)
            if (newDirections.size == 1) currentDirection = newDirections.first()
            else {
                currentDirection = newDirections.first()
                queue.add(Pair(currentRow, currentCol) to newDirections[1])
            }
            when (currentDirection) {
                Direction.UP -> currentRow--
                Direction.RIGHT -> currentCol++
                Direction.DOWN -> currentRow++
                Direction.LEFT -> currentCol--
            }
        }
    }

    return energized.size
}

private fun part2(input: String): Int {
    val map = input.lines().map { it.toCharArray().map {c ->
        when (c) {
            '/' -> Mirror.FORWARD_SLASH
            '\\' -> Mirror.BACK_SLASH
            '|' -> Mirror.VERTICAL
            '-' -> Mirror.HORIZONTAL
            else -> Mirror.DOT
        }
    }.toList() }

    var max = 0

    for (row in map.indices) {
        for (col in map[0].indices) {
            val starts = mutableListOf<Pair<Pair<Int, Int> ,Direction>>()
            if (row == 0) starts.add(Pair(row, col) to Direction.DOWN)
            if (col == 0) starts.add(Pair(row, col) to Direction.RIGHT)
            if (row == map.size - 1) starts.add(Pair(row, col) to Direction.UP)
            if (col == map[0].size - 1) starts.add(Pair(row, col) to Direction.LEFT)

            for (initialStart in starts) {
                val visited = mutableSetOf<Pair<Pair<Int, Int>, Direction>>()
                val energized = mutableSetOf<Pair<Int, Int>>()

                val queue = mutableListOf<Pair<Pair<Int, Int>, Direction>>()
                queue.add(initialStart.first to initialStart.second)
                while (queue.isNotEmpty()) {
                    val start = queue.removeLast()
                    var currentRow = start.first.first
                    var currentCol = start.first.second
                    var currentDirection = start.second
                    while (currentRow >= 0 && currentCol >= 0 && currentRow < map.size && currentCol < map[0].size) {
                        energized.add(currentRow to currentCol)
                        val visitedKey = (currentRow to currentCol) to currentDirection
                        if (visited.contains(visitedKey)) break
                        visited.add(visitedKey)
                        val newDirections = map[currentRow][currentCol].reflect(currentDirection)
                        if (newDirections.size == 1) currentDirection = newDirections.first()
                        else {
                            currentDirection = newDirections.first()
                            queue.add(Pair(currentRow, currentCol) to newDirections[1])
                        }
                        when (currentDirection) {
                            Direction.UP -> currentRow--
                            Direction.RIGHT -> currentCol++
                            Direction.DOWN -> currentRow++
                            Direction.LEFT -> currentCol--
                        }
                    }
                }

                max = max(energized.size, max)
            }
        }
    }


    return max
}
package y2023.d17

import java.io.File
import kotlin.math.min
import kotlin.system.measureTimeMillis

private fun main() {
    val input = File(
//        "src/main/kotlin/y2023/d17/Input-test.txt"
        "src/main/kotlin/y2023/d17/Input.txt"
    ).readText(Charsets.UTF_8)

//    println("${
//        measureTimeMillis {
//            println(
//                "part1: ${part1(input)}"
//            )
//        }
//    } ms\n")

    println("${
        measureTimeMillis {
            println(
                "part2: ${part2(input)}"
            )

        }
    } ms")
}

private enum class Direction {
    UP {
        override fun opposite() = DOWN
    },
    DOWN {
        override fun opposite() = UP
    },
    RIGHT {
        override fun opposite() = LEFT
    },
    LEFT {
        override fun opposite() = RIGHT
    };

    abstract fun opposite(): Direction
}

private data class Snapshot(val row: Int, val col: Int, val direction: Direction, val directionCounter: Int) {
    fun move(newDirection: Direction): Snapshot {
        var newRow = row
        var newCol = col
        when (newDirection) {
            Direction.UP -> newRow--
            Direction.DOWN -> newRow++
            Direction.RIGHT -> newCol++
            Direction.LEFT -> newCol--
        }

        return Snapshot(newRow, newCol, newDirection, if (direction != newDirection) 1 else directionCounter + 1)
    }
}

private fun part1(input: String): Int {
    val map = input.lines().map { it.toCharArray().map { c -> c.digitToInt() }.toList() }
    val minToSnapshots = mutableMapOf<Snapshot, Int>()
    val queue = mutableListOf(Snapshot(0, 0, Direction.DOWN, 0) to 0)
    var minToExit = Int.MAX_VALUE

    val totalSum = map.sumOf { it.sum() }
    while (queue.isNotEmpty()) {
        val (start, heatLoss) = queue.removeLast()
        if (heatLoss > totalSum) continue
        val cached = minToSnapshots[start]
        if (cached != null && cached <= heatLoss) continue
        if (minToExit <= heatLoss) continue

        if (start.row == map.size - 1 && start.col == map[0].size - 1) {
            println(minToExit)
            minToExit = min(minToExit, heatLoss)
            continue
        }

        minToSnapshots[start] = heatLoss

        val directions = Direction.values().filter { it != start.direction.opposite() }
        for (direction in directions) {
            val newSnapshot = start.move(direction)
            if (newSnapshot.directionCounter > 3) continue
            if (newSnapshot.row < 0 || newSnapshot.row >= map.size) continue
            if (newSnapshot.col < 0 || newSnapshot.col >= map[0].size) continue

            queue.add(newSnapshot to heatLoss + map[newSnapshot.row][newSnapshot.col])
        }
    }

    return minToExit
}

private fun part2(input: String): Int {
    val map = input.lines().map { it.toCharArray().map { c -> c.digitToInt() }.toList() }
    val minToSnapshots = mutableMapOf<Snapshot, Int>()
    val queue = mutableListOf(Snapshot(0, 0, Direction.DOWN, 0) to 0)
    var minToExit = Int.MAX_VALUE

    val totalSum = map.sumOf { it.sum() }
    while (queue.isNotEmpty()) {
        val (start, heatLoss) = queue.removeLast()
        if (heatLoss > totalSum) continue
        val cached = minToSnapshots[start]
        if (cached != null && cached <= heatLoss) continue
        if (minToExit <= heatLoss) continue

        if (start.row == map.size - 1 && start.col == map[0].size - 1 && start.directionCounter >= 4) {
            println(minToExit)
            minToExit = min(minToExit, heatLoss)
            continue
        }

        minToSnapshots[start] = heatLoss

        val directions = Direction.values().filter { it != start.direction.opposite() }

        for (direction in directions) {
            val newSnapshot = start.move(direction)
            if (newSnapshot.directionCounter > 10) continue
            if (start.directionCounter != 0 && start.directionCounter < 4 && newSnapshot.directionCounter == 1) continue
            if (newSnapshot.row < 0 || newSnapshot.row >= map.size) continue
            if (newSnapshot.col < 0 || newSnapshot.col >= map[0].size) continue

            queue.add(newSnapshot to heatLoss + map[newSnapshot.row][newSnapshot.col])
        }
    }

    return minToExit
}
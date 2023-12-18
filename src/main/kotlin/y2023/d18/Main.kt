package y2023.d18

import java.io.File
import kotlin.math.abs
import kotlin.system.measureTimeMillis

private fun main() {
    val input = File(
//        "src/main/kotlin/y2023/d18/Input-test.txt"
        "src/main/kotlin/y2023/d18/Input.txt"
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


private enum class Direction {
    UP {
        override fun move(current: Pair<Int, Int>) = current.first - 1 to current.second
        override fun multiplier() = -1L to 0L
    },
    DOWN {
        override fun move(current: Pair<Int, Int>) = current.first + 1 to current.second
        override fun multiplier() = 1L to 0L
    },
    RIGHT {
        override fun move(current: Pair<Int, Int>) = current.first to current.second + 1
        override fun multiplier() = 0L to 1L
    },
    LEFT {
        override fun move(current: Pair<Int, Int>) = current.first to current.second - 1
        override fun multiplier() = 0L to -1L
    };

    abstract fun move(current: Pair<Int, Int>): Pair<Int, Int>
    abstract fun multiplier(): Pair<Long, Long>
}

private data class Line(val direction: Direction, val count: Int)

private fun part1(input: String): Int {
    val lines = input.lines().map {
        val (dir, count, color) = it.split(" ")
        val direction = when (dir) {
            "L" -> Direction.LEFT
            "R" -> Direction.RIGHT
            "U" -> Direction.UP
            "D" -> Direction.DOWN
            else -> throw Exception("Unknown direction")
        }
        Line(direction, count.toInt())
    }

    val mapMap = mutableMapOf<Int, MutableMap<Int, Char>>()
    fun safeAssign(cell: Pair<Int, Int>, char: Char) {
        if (mapMap[cell.first] == null) mapMap[cell.first] = mutableMapOf()
        mapMap[cell.first]!![cell.second] = char
    }

    var currentCell = 0 to 0
    safeAssign(currentCell, '#')
    lines.forEach { line ->
        repeat(line.count) {
            currentCell = line.direction.move(currentCell)
            safeAssign(currentCell, '#')
        }
    }

    val minRow = mapMap.keys.minOf { it }
    val maxRow = mapMap.keys.maxOf { it }
    val minCol = mapMap.minOf { it.value.minOf { line -> line.key } }
    val maxCol = mapMap.maxOf { it.value.maxOf { line -> line.key } }
    val map = List(abs(minRow - maxRow) + 1) { MutableList(abs(minCol - maxCol) + 1) { '.' } }
    for (row in mapMap.keys) {
        for (col in mapMap[row]!!.keys) {
            val fixedRow = row - minRow
            val fixedCol = col - minCol
            map[fixedRow][fixedCol] = mapMap[row]!![col]!!
        }
    }

    val queue = mutableListOf<Pair<Int, Int>>()
    queue.add(1 to map[0].indexOf('#') + 1)

    while (queue.isNotEmpty()) {
        val cell = queue.removeLast()
        map[cell.first][cell.second] = '#'
        for (direction in Direction.values()) {
            val newCell = direction.move(cell)
            if (map[newCell.first][newCell.second] != '#') {
                queue.add(newCell)
            }
        }
    }
    return map.sumOf { line -> line.count { it == '#' } }
}

private fun shoelaceArea(points: List<Pair<Long, Long>>) : Long {
    var perimeter = 0L
    var area = 0L
    points.windowed(2) {(a, b) ->
        perimeter += abs(a.first - b.first) + abs(a.second - b.second)
        area += (a.second - b.second) * (a.first + b.first)
    }
    return (perimeter + area) / 2 + 1
}

private fun Pair<Long, Long>.multiply(multiplier: Long) = first * multiplier to second * multiplier
private fun Pair<Long, Long>.add(other: Pair<Long, Long>) = first + other.first to second + other.second

private fun part2(input: String): Long {
    val lines = input.lines().map {
        val (_, _, color) = it.split(" ")
        val direction = when (color.substring(2, 8).last()) {
            '0' -> Direction.RIGHT
            '1' -> Direction.DOWN
            '2' -> Direction.LEFT
            '3' -> Direction.UP
            else -> throw Exception("Unknown direction")
        }
        Line(direction, color.substring(2, 7).toInt(16))
    }

    val points = mutableListOf<Pair<Long, Long>>()
    var currentPosition = 0L to 0L
    points.add(currentPosition)
    lines.forEach { line ->
        currentPosition = line.direction.multiplier().multiply(line.count.toLong()).add(currentPosition)
        points.add(currentPosition)
    }

    return shoelaceArea(points)
}

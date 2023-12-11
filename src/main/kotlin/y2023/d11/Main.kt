package y2023.d11

import indicesOf
import java.io.File
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.system.measureTimeMillis

fun main() {
    val input = File(
//        "src/main/kotlin/y2023/d11/Input-test.txt"
        "src/main/kotlin/y2023/d11/Input.txt"
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

data class Position(val row: Int, val column: Int)

fun part1(input: String): Int {
    val map = input.lines().map {line ->
        line.toCharArray().toMutableList()
    }.toMutableList()

    val emptyRowIndices = mutableSetOf<Int>()
    val galaxyColumnIndices = mutableSetOf<Int>()
    for (i in map.indices.reversed()) {
        val indices = map[i].indicesOf('#')
        galaxyColumnIndices.addAll(indices)
        if (indices.isEmpty()) {
            emptyRowIndices.add(i)
        }
    }
    val emptyColumnIndices = mutableSetOf<Int>()
    for (i in map[0].indices.reversed()) {
        if (!galaxyColumnIndices.contains(i)) {
            emptyColumnIndices.add(i)
        }
    }

    val galaxies = mutableListOf<Position>()
    map.forEachIndexed { rowIndex, row ->
        val columns = row.indicesOf('#')
        for (columnIndex in columns) {
            galaxies.add(Position(rowIndex, columnIndex))
        }
    }

    var sum = 0
    for (i in galaxies.indices) {
        for (k in i until galaxies.size) {
            var emptyRowsCount = 0
            for (emptyRowIndex in emptyRowIndices) {
                if (emptyRowIndex in min(galaxies[i].row, galaxies[k].row) ..max(galaxies[i].row, galaxies[k].row)) {
                    emptyRowsCount++
                }
            }
            var emptyColumnsCount = 0
            for (emptyColumnIndex in emptyColumnIndices) {
                if (emptyColumnIndex in min(galaxies[i].column, galaxies[k].column) ..max(galaxies[i].column, galaxies[k].column)) {
                    emptyColumnsCount++
                }
            }
            sum += abs(galaxies[i].row - galaxies[k].row) + abs (galaxies[i].column - galaxies[k].column)
            sum += emptyRowsCount + emptyColumnsCount
        }
    }
    return sum
}

fun part2(input: String): Long {
    val map = input.lines().map {line ->
        line.toCharArray().toMutableList()
    }.toMutableList()

    val emptyRowIndices = mutableSetOf<Int>()
    val galaxyColumnIndices = mutableSetOf<Int>()
    for (i in map.indices.reversed()) {
        val indices = map[i].indicesOf('#')
        galaxyColumnIndices.addAll(indices)
        if (indices.isEmpty()) {
            emptyRowIndices.add(i)
        }
    }
    val emptyColumnIndices = mutableSetOf<Int>()
    for (i in map[0].indices.reversed()) {
        if (!galaxyColumnIndices.contains(i)) {
            emptyColumnIndices.add(i)
        }
    }

    val galaxies = mutableListOf<Position>()
    map.forEachIndexed { rowIndex, row ->
        val columns = row.indicesOf('#')
        for (columnIndex in columns) {
            galaxies.add(Position(rowIndex, columnIndex))
        }
    }

    var sum = 0L
    for (i in galaxies.indices) {
        for (k in i until galaxies.size) {
            var emptyRowsCount = 0
            for (emptyRowIndex in emptyRowIndices) {
                if (emptyRowIndex in min(galaxies[i].row, galaxies[k].row) ..max(galaxies[i].row, galaxies[k].row)) {
                    emptyRowsCount++
                }
            }
            var emptyColumnsCount = 0
            for (emptyColumnIndex in emptyColumnIndices) {
                if (emptyColumnIndex in min(galaxies[i].column, galaxies[k].column) ..max(galaxies[i].column, galaxies[k].column)) {
                    emptyColumnsCount++
                }
            }
            sum += abs(galaxies[i].row - galaxies[k].row) + abs (galaxies[i].column - galaxies[k].column)
            sum += (emptyRowsCount + emptyColumnsCount) * (1000000 - 1)
        }
    }
    return sum
}
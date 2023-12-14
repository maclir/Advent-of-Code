package y2023.d14

import java.io.File
import kotlin.system.measureTimeMillis

fun main() {
    val input = File(
//        "src/main/kotlin/y2023/d14/Input-test.txt"
        "src/main/kotlin/y2023/d14/Input.txt"
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

fun part1(input: String): Int {
    val map = input.lines().map { it.toCharArray().toMutableList() }
    val lowestRows = MutableList(map[0].size) { -1 }

    map.forEachIndexed { rowIndex, row ->
        row.forEachIndexed { cIndex, c ->
            val lowestRow = lowestRows[cIndex]
            when (c) {
                'O' -> {
                    if (lowestRow < rowIndex - 1) {
                        map[rowIndex][cIndex] = '.'
                        map[lowestRow + 1][cIndex] = 'O'
                    }
                    lowestRows[cIndex] = lowestRow + 1
                }

                '#' -> {
                    lowestRows[cIndex] = rowIndex
                }
            }
        }
    }

    var load = map.size + 1
    return map.sumOf { row ->
        load--
        row.count { it == 'O' } * (load)
    }
}

fun part2(input: String): Int {
    val map = input.lines().map { it.toCharArray().toMutableList() }

    val snapshots = mutableMapOf<List<List<Char>>, Int>()
    val targetCycles = 1000000000
    var cycle = 0
    while(true) {
        if (cycle >= targetCycles) {
            break
        }
        val lowestRows = MutableList(map[0].size) { -1 }
        for (rowIndex in map.indices) {
            for (cIndex in map[0].indices) {
                val c = map[rowIndex][cIndex]
                val lowestRow = lowestRows[cIndex]
                when (c) {
                    'O' -> {
                        if (lowestRow < rowIndex - 1) {
                            map[rowIndex][cIndex] = '.'
                            map[lowestRow + 1][cIndex] = 'O'
                        }
                        lowestRows[cIndex] = lowestRow + 1
                    }

                    '#' -> {
                        lowestRows[cIndex] = rowIndex
                    }
                }
            }
        }

        val lowestColumns = MutableList(map.size) { -1 }
        for (cIndex in map[0].indices) {
            for (rowIndex in map.indices) {
                val c = map[rowIndex][cIndex]
                val lowestColumn = lowestColumns[rowIndex]
                when (c) {
                    'O' -> {
                        if (lowestColumn < cIndex - 1) {
                            map[rowIndex][cIndex] = '.'
                            map[rowIndex][lowestColumn + 1] = 'O'
                        }
                        lowestColumns[rowIndex] = lowestColumn + 1
                    }

                    '#' -> {
                        lowestColumns[rowIndex] = cIndex
                    }
                }
            }
        }

        val highestRows = MutableList(map[0].size) { map[0].size }
        for (rowIndex in map.size - 1 downTo 0) {
            for (cIndex in map[0].indices) {
                val c = map[rowIndex][cIndex]
                val highestRow = highestRows[cIndex]
                when (c) {
                    'O' -> {
                        if (highestRow > rowIndex + 1) {
                            map[rowIndex][cIndex] = '.'
                            map[highestRow - 1][cIndex] = 'O'
                        }
                        highestRows[cIndex] = highestRow - 1
                    }

                    '#' -> {
                        highestRows[cIndex] = rowIndex
                    }
                }
            }
        }

        val highestColumns = MutableList(map.size) { map.size }
        for (cIndex in map[0].size - 1 downTo 0) {
            for (rowIndex in map.indices) {
                val c = map[rowIndex][cIndex]
                val highestColumn = highestColumns[rowIndex]
                when (c) {
                    'O' -> {
                        if (highestColumn > cIndex + 1) {
                            map[rowIndex][cIndex] = '.'
                            map[rowIndex][highestColumn - 1] = 'O'
                        }
                        highestColumns[rowIndex] = highestColumn - 1
                    }

                    '#' -> {
                        highestColumns[rowIndex] = cIndex
                    }
                }
            }
        }

        val snapshotKey = map.map { it.toList() }
        val snapshot = snapshots[snapshotKey]
        if (snapshot != null) {
            val diff = cycle - snapshot
            val diffTimes = (targetCycles - cycle) / diff
            cycle += diff * diffTimes
            snapshots.clear()
        }
        snapshots[snapshotKey] = cycle
        cycle++
    }
    var load = map.size + 1
    return map.sumOf { row ->
        load--
        row.count { it == 'O' } * (load)
    }
}
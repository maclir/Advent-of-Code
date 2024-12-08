package y2024.d08

import utilities.Node
import utilities.intLines
import utilities.longLines
import java.io.File
import kotlin.system.measureTimeMillis

private fun main() {
    val input = File(
//        "src/main/kotlin/y2024/d08/Input-test.txt"
        "src/main/kotlin/y2024/d08/Input.txt"
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
    val map = input.lines().map { row ->
        row.toCharArray().toList()
    }

    val antinodes = mutableSetOf<Node>()
    map.forEachIndexed { rowIndex, row ->
        row.forEachIndexed colLoop@{ colIndex, c ->
            if (c == '.') return@colLoop
            map.forEachIndexed { innerRowIndex, innerRow ->
                innerRow.forEachIndexed innerColLoop@{ innerColIndex, innerC ->
                    if (innerC != c) return@innerColLoop
                    if (rowIndex == innerRowIndex && colIndex == innerColIndex) return@innerColLoop
                    val antinode = Node(
                        2 * rowIndex - innerRowIndex,
                        2 * colIndex - innerColIndex
                    )
                    if (antinode.isInMap(map)) antinodes.add(antinode)
                }
            }
        }
    }

    return antinodes.size
}

private fun part2(input: String): Int {
    val map = input.lines().map { row ->
        row.toCharArray().toList()
    }
    val fixedMap = map.map { row ->
        row.map { c ->
            if (map.sumOf { innerRow -> innerRow.count{it == c} } == 1) '.'
            else c
        }
    }

    val antinodes = mutableSetOf<Node>()
    fixedMap.forEachIndexed { rowIndex, row ->
        row.forEachIndexed colLoop@{ colIndex, c ->
            if (c == '.') return@colLoop
            fixedMap.forEachIndexed { innerRowIndex, innerRow ->
                innerRow.forEachIndexed innerColLoop@{ innerColIndex, innerC ->
                    if (innerC != c) return@innerColLoop
                    var antinode = Node(rowIndex, colIndex)
                    antinodes.add(antinode)
                    val rowDistance =  innerRowIndex - rowIndex
                    val colDistance = innerColIndex - colIndex
                    if (rowDistance == 0 && colDistance == 0) return@innerColLoop
                    while (antinode.isInMap(fixedMap)) {
                        antinodes.add(antinode)
                        antinode = antinode.move(rowDistance, colDistance)
                    }
                }
            }
        }
    }

    return antinodes.size
}
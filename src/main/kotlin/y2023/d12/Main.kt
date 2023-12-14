package y2023.d12

import java.io.File
import kotlin.system.measureTimeMillis

private fun main() {
    val input = File(
//        "src/main/kotlin/y2023/d12/Input-test.txt"
        "src/main/kotlin/y2023/d12/Input.txt"
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

private data class Line(val line: String, val damageCounts: List<Int>)

private fun String.matchesWithPlaceholder(otherLine: String): Boolean {
    for (index in this.indices) {
        if (this[index] != '?' && otherLine[index] != '?' && this[index] != otherLine[index]) return false
     }
    return true
}

private fun Line.getPossibleLinesCount(): Long {
    val cache = mutableMapOf<Pair<Int, Int>, Long>()

  fun dive(diveLine: Line, startIndex: Int): Long {
        val cacheKey = Pair(startIndex, diveLine.damageCounts.size)
        val cachedResult = cache[cacheKey]
        if (cachedResult != null) {
            return cachedResult
        }

        if (!diveLine.line.substring(0, startIndex).matchesWithPlaceholder(line.substring(0, startIndex))) {
            return 0
        }

        var result = 0L


        val replacement = if (diveLine.damageCounts.size == 1 && startIndex + diveLine.damageCounts.first() == line.length) {
            CharArray(diveLine.damageCounts.first()) { '#' }.joinToString("")
        } else {
            CharArray(diveLine.damageCounts.first()) { '#' }.joinToString("") + '.'
        }

        val endIndex = startIndex + replacement.length
        if (endIndex <= line.length) {
            val newLine = diveLine.line.replaceRange(
                startIndex,
                endIndex,
                replacement
            )

            if (newLine.matchesWithPlaceholder(line) && newLine.count { it == '#' } <= damageCounts.sum()) {
                val newDiveLine = Line(newLine, diveLine.damageCounts.drop(1))
                if (newDiveLine.damageCounts.isNotEmpty()) {
                    result += dive(newDiveLine, endIndex)
                } else {
                    result++
                }
            }
            result += dive(
                Line(diveLine.line.replaceRange(startIndex, startIndex + 1, "."), diveLine.damageCounts),
                startIndex + 1
            )
        }

        cache[cacheKey] = result
        return result
    }

    return dive(this, 0)
}


private fun part1(input: String): Long {
    return input.lines().map { line ->
        val (springs, damageCounts) = line.split(" ")
        Line(springs, damageCounts.split(",").map { it.toInt() })
    }.sumOf { line ->
        line.getPossibleLinesCount()
    }
}

private fun part2(input: String): Long {
    return input.lines().map { line ->
        val (springs, damageCounts) = line.split(" ")
        val unfoldedDamageCounts = List(5) { damageCounts }.joinToString(",")
        Line(List(5) { springs }.joinToString("?"), unfoldedDamageCounts.split(",").map { it.toInt() })
    }.sumOf { line ->
        line.getPossibleLinesCount()
    }
}
package y2022.d15

import java.io.File
import kotlin.math.abs

private fun main() {
    val input = File("src/main/kotlin/y2022/d15/Input.txt").readText(Charsets.UTF_8)
//    val input = File("src/main/kotlin/y2022/d15/Input-test.txt").readText(Charsets.UTF_8)
//    println(part1(input))
    println(part2(input))
}

private fun part2(input: String): Int {
//    val upperBound = 20
    val upperBound = 4000000
    val numbers = input.lines().map { line ->
        Regex("-?[0-9]+").findAll(line).map(MatchResult::value).map { it.toInt() }.toList()
    }

    val startEnds = mutableListOf<Pair<Int, Int>>()
    for (yToCheck in 0..upperBound) {
        startEnds.clear()
        numbers.forEach { (x1, y1, x2, y2) ->
            val distance = abs(y1 - y2) + abs(x1 - x2)
            val xDistance = distance - abs(y1 - yToCheck)
            when {
                xDistance < 0 -> return@forEach
                x1 - xDistance > upperBound -> return@forEach
                x1 + xDistance < 0 -> return@forEach
                else -> startEnds.add((x1 - xDistance).coerceAtLeast(0) to (x1 + xDistance).coerceAtMost(upperBound))
            }
        }

        startEnds.sortedBy { it.first }.reduce { (accStart, accEnd), (start, end) ->
            if (accStart > 0) return yToCheck
            if (accEnd == upperBound) return@reduce 0 to upperBound
            if (start > (accEnd + 1)) {
                println("${(accEnd + 1) * 4 + (yToCheck / 1000000)}|${yToCheck % 1000000}")
                return yToCheck + (accEnd + 1) * 4
            }

            accStart to if (end > accEnd) end else accEnd
        }
    }
    return -1
}

private fun part1(input: String): Int {
    val yToCheck = 2000000
//    val yToCheck = 10

    val numbersT = input.lines().map { line ->
        Regex("-?[0-9]+").findAll(line).map(MatchResult::value).map { it.toInt() }.toList()
    }

    val offsetX = -numbersT.minOf { (x1, y1, x2, y2) ->
        x1 - (abs(y1 - y2) + abs(x1 - x2))
    }

    val numbers = numbersT.map { (x1, y1, x2, y2) ->
        listOf(x1 + offsetX, y1, x2 + offsetX, y2)
    }

    val maxX = numbers.maxOf { (x1, y1, x2, y2) ->
        x1 + abs(y1 - y2) + abs(x1 - x2)
    } + 1


    val grid = MutableList(maxX) { '.' }
    numbers.forEach { (x1, y1, x2, y2) ->
        if (y1 == yToCheck && grid[x1] == '.') {
            grid[x1] = 'S'
        }
        if (y2 == yToCheck) {
            grid[x2] = 'B'
        }
        val distance = abs(y1 - y2) + abs(x1 - x2)
        if (yToCheck !in (y1 - distance)..(y1 + distance)) return@forEach
        for (k in (x1 - distance)..(x1 + distance)) {
            val newDistance = abs(y1 - yToCheck) + abs(x1 - k)
            if (newDistance <= distance && grid[k] != 'B') {
                grid[k] = '#'
            }
        }
    }

    return grid.count { char -> char != '.' && char != 'B' }
}
package y2022.d9

import java.io.File
import kotlin.math.abs

fun main() {
    val input = File("src/main/kotlin/y2022/d9/Input.txt").readLines(Charsets.UTF_8)
//    val input = File("src/main/kotlin/y2022/d9/Input-test.txt").readLines(Charsets.UTF_8)
//    println(part1(input))
    println(part2(input))
}

fun part2(input: List<String>): Int {
    val visited = mutableSetOf<String>()

    val x = MutableList(10) { 0 }
    val y = MutableList(10) { 0 }

    visited.add("${x[9]},${y[9]}")
    input.forEach {
        val (dir, times) = it.split(" ")
        for (i in 0 until times.toInt()) {
            when (dir) {
                "R" -> x[0]++
                "U" -> y[0]++
                "L" -> x[0]--
                "D" -> y[0]--
            }
            for (z in 1..9) {
                val xDiff = abs(x[z] - x[z - 1])
                val yDiff = abs(y[z] - y[z - 1])
                when {
                    xDiff >= 2 && yDiff == 0 -> {
                        x[z] += (x[z - 1] - x[z]) / xDiff
                    }

                    xDiff == 0 && yDiff >= 2 -> {
                        y[z] += (y[z - 1] - y[z]) / yDiff
                    }

                    xDiff >= 2 && yDiff >= 1 -> {
                        x[z] += (x[z - 1] - x[z]) / xDiff
                        y[z] += (y[z - 1] - y[z]) / yDiff
                    }

                    xDiff >= 1 && yDiff >= 2 -> {
                        y[z] += (y[z - 1] - y[z]) / yDiff
                        x[z] += (x[z - 1] - x[z]) / xDiff
                    }
                }
            }
            visited.add("${x[9]},${y[9]}")
        }
    }

    return visited.size
}

fun part1(input: List<String>): Int {
    val visited = mutableSetOf<String>()

    var (tX, tY, hX, hY) = List(4) { 0 }
    visited.add("$tX,$tY")
    input.forEach {
        val (dir, times) = it.split(" ")
        for (i in 0 until times.toInt()) {
            when (dir) {
                "R" -> hX++
                "U" -> hY++
                "L" -> hX--
                "D" -> hY--
            }
            val xDiff = abs(hX - tX)
            val yDiff = abs(hY - tY)
            when {
                xDiff == 2 && yDiff == 0 -> {
                    tX += ((hX - tX) / 2)
                }

                xDiff == 0 && yDiff == 2 -> {
                    tY += ((hY - tY) / 2)
                }

                xDiff == 2 && yDiff == 1 -> {
                    tX += ((hX - tX) / 2)
                    tY += (hY - tY)
                }

                xDiff == 1 && yDiff == 2 -> {
                    tY += ((hY - tY) / 2)
                    tX += (hX - tX)
                }
            }
            visited.add("$tX,$tY")
        }
    }

    return visited.size
}

package y2015.d02

import utilities.Node
import utilities.charGrid
import utilities.intLines
import utilities.longLines
import java.io.File
import kotlin.system.measureTimeMillis

private fun main() {
    val input = File(
//        "src/main/kotlin/y2015/d02/Input-test.txt"
        "src/main/kotlin/y2015/d02/Input.txt"
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

private data class Box(
    val l: Int,
    val w: Int,
    val h: Int,
) {
    fun surfaceArea() = 2 * l * w + 2 * w * h + 2 * h * l
    fun smallestSurface() = listOf(l, w, h).sorted().dropLast(1).fold(1) { acc, i -> acc * i }
    fun volume() = l * w * h
    fun smallestPerimeter() = listOf(l, w, h).sorted().dropLast(1).fold(0) { acc, i -> acc + i * 2 }
}

private fun part1(input: String): Int {
    return input.lines().map {
        val (l, w, h) = it.split("x").map { it.toInt() }
        Box(l, w, h)
    }.sumOf { box ->
        box.surfaceArea() + box.smallestSurface()
    }
}

private fun part2(input: String): Int {
    return input.lines().map {
        val (l, w, h) = it.split("x").map { it.toInt() }
        Box(l, w, h)
    }.sumOf { box ->
        box.volume() + box.smallestPerimeter()
    }
}
package y2024.d01

import java.io.File
import kotlin.math.abs
import kotlin.system.measureTimeMillis

private fun main() {
    val input = File(
//        "src/main/kotlin/y2024/d01/Input-test.txt"
        "src/main/kotlin/y2024/d01/Input.txt"
    ).readText(Charsets.UTF_8)

    println("${
        measureTimeMillis {
            println(
//                part1(input)
                part2(input)
            )

        }
    } ms")
}

private fun part1(input: String): Int {
    val first = mutableListOf<Int>()
    val second = mutableListOf<Int>()
    input.lines().map { line ->
        val parts = line.split("   ").map { it.toInt() }
        first.add(parts[0])
        second.add(parts[1])
    }
    first.sort()
    second.sort()
    var acc = 0
    first.forEachIndexed { index, i ->
        acc += abs(i - second[index])
    }
    return acc
}

private fun part2(input: String): Int {
    val first = mutableListOf<Int>()
    val second = mutableListOf<Int>()
    input.lines().map { line ->
        val parts = line.split("   ").map { it.toInt() }
        first.add(parts[0])
        second.add(parts[1])
    }
    first.sort()

    return first.sumOf { firstIt ->
        firstIt * second.count { it == firstIt }
    }
}
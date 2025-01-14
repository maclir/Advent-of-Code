package y2024.d11

import utilities.*
import java.io.File
import kotlin.system.measureTimeMillis

private fun main() {
    val input = File(
//        "src/main/kotlin/y2024/d11/Input-test.txt"
        "src/main/kotlin/y2024/d11/Input.txt"
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
    var arrangement = input.split(" ").map { it.toLong() }
    repeat(25) {
        arrangement = blink(arrangement)
    }

    return arrangement.size
}

private val cachedBlink = { stone: Long, count: Int -> blink(stone, count) }.cache()
private fun part2(input: String): Long {
    return input.split(" ").map { it.toLong() }.sumOf {
        cachedBlink(it, 0)
    }
}

private fun blink(arrangement: List<Long>): List<Long> {
    val newList = mutableListOf<Long>()
    arrangement.forEach { stone ->
        if (stone == 0L) {
            newList.add(1)
            return@forEach
        }

        val stoneStr = stone.toString()
        if (stoneStr.length % 2 == 0) {
            newList.add(stoneStr.substring(0, stoneStr.length / 2).toLong())
            newList.add(stoneStr.substring(stoneStr.length / 2, stoneStr.length).toLong())
            return@forEach
        }

        newList.add((stone * 2024))
    }
    return newList
}

private fun blink(stone: Long, count: Int): Long {
    if (count >= 75) {
        return 1
    }

    if (stone == 0L) {
        return cachedBlink(1, count + 1)
    }

    val stoneStr = stone.toString()
    if (stoneStr.length % 2 == 0) {
        return cachedBlink(stoneStr.substring(0, stoneStr.length / 2).toLong(), count + 1) +
                cachedBlink(stoneStr.substring(stoneStr.length / 2, stoneStr.length).toLong(), count + 1)
    }

    return cachedBlink(stone * 2024, count + 1)
}
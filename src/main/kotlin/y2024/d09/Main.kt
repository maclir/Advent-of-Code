package y2024.d09

import utilities.Node
import utilities.intLines
import utilities.longLines
import utilities.print
import java.io.File
import kotlin.system.measureTimeMillis

private fun main() {
    val input = File(
//        "src/main/kotlin/y2024/d09/Input-test.txt"
        "src/main/kotlin/y2024/d09/Input.txt"
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

private fun part1(input: String): Long {
    val diskMap = input.split("").drop(1).dropLast(1).map { it.toInt() }.mapIndexed { index, digit ->
        if (index % 2 == 0) MutableList(digit, { index / 2 })
        else MutableList(digit, { -1 })
    }

    var targetRowIndex = diskMap.lastIndex
    diskMap.forEachIndexed { index, freeInts ->
        if (index % 2 == 0) return@forEachIndexed
        if (targetRowIndex < index) return@forEachIndexed

        var k = 0
        while (true) {
            val targetRow = diskMap[targetRowIndex]
            for (i in targetRow.filter { it > -1 }.indices.reversed()) {
                if (k > freeInts.lastIndex) return@forEachIndexed
                freeInts[k] = targetRow[i]
                targetRow[i] = -1
                k++
            }
            targetRowIndex -= 2
            if (targetRowIndex < index) return@forEachIndexed
        }
    }
    var sum = 0L
    var index = 0
    diskMap.forEach outer@ { ints ->
        ints.forEach { int ->
            if (int == -1) return@outer
            sum += index * int
            index++
        }
    }
    return sum
}

private fun part2(input: String): Long {
    val diskMap = input.split("").drop(1).dropLast(1).map { it.toInt() }.mapIndexed { index, digit ->
        if (index % 2 == 0) MutableList(digit, { index / 2 })
        else MutableList(digit, { -1 })
    }


    for (index in diskMap.indices.reversed()) {
        if (index % 2 != 0) continue
        val ints = diskMap[index]
        val targetFreeRow = diskMap.subList(0, index)
            .filterIndexed({ i, _ -> i % 2 != 0 })
            .firstOrNull { it.filter { it == -1 }.size >= ints.size }
        if (targetFreeRow == null) continue
        val firstFreeIndex = targetFreeRow.indexOf(-1)
        for (i in ints.indices) {
            targetFreeRow[firstFreeIndex + i] = ints[i]
            ints[i] = -1
        }
    }
    var sum = 0L
    var index = -1
    diskMap.forEach outer@ { ints ->
        ints.forEach { int ->
            index++
            if (int == -1) return@forEach
            sum += index * int
        }
    }
    return sum
}
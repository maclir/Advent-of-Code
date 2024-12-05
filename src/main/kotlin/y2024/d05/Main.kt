package y2024.d05

import utilities.intLines
import utilities.longLines
import java.io.File
import kotlin.system.measureTimeMillis

private fun main() {
    val input = File(
        "src/main/kotlin/y2024/d05/Input-test.txt"
//        "src/main/kotlin/y2024/d05/Input.txt"
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
    val (rulesInput, updatesInput) = input.split("\n\n")
    val rules = mutableMapOf<Int, MutableList<Int>>()
    rulesInput.lines().forEach { line ->
        """([0-9]*)\|([0-9]*)""".toRegex().findAll(line).map {
            it.groupValues[1].toInt() to it.groupValues[2].toInt()
        }.forEach { (first, second) ->
            if (rules.contains(first)) rules[first]?.add(second)
            else rules[first] = mutableListOf(second)
        }
    }
    val updates = updatesInput.lines().map { line ->
        line.split(",").map {
            it.toInt()
        }
    }
    var sum = 0
    updates.forEach outer@{ update ->
        update.forEachIndexed { index, number ->
            val secondNumberList = rules[number]
            if (secondNumberList != null) {
                val belowList = update.subList(0, index)
                secondNumberList.forEach { secondNumber ->
                    if (belowList.contains(secondNumber)) return@outer
                }
            }
        }
        sum += update[(update.size - 1) / 2]
    }
    return sum
}

private fun part2(input: String): Int {
    val (rulesInput, updatesInput) = input.split("\n\n")
    val rules = mutableMapOf<Int, MutableList<Int>>()
    rulesInput.lines().forEach { line ->
        """([0-9]*)\|([0-9]*)""".toRegex().findAll(line).map {
            it.groupValues[1].toInt() to it.groupValues[2].toInt()
        }.forEach { (first, second) ->
            if (rules.contains(first)) rules[first]?.add(second)
            else rules[first] = mutableListOf(second)
        }
    }
    val updates = updatesInput.lines().map { line ->
        line.split(",").map {
            it.toInt()
        }
    }

    var sum = 0
    updates.forEach outer@{ update ->
        val sortedUpdate = update.sortedWith { a, b ->
            if (rules[a]?.contains(b) == true) -1
            else 1
        }
        if (update != sortedUpdate) {
            sum += sortedUpdate[(sortedUpdate.size - 1) / 2]
        }
    }

    return sum
}
package y2023.d04

import java.io.File
import kotlin.system.measureTimeMillis

private fun main() {
    val input = File(
//        "src/main/kotlin/y2023/d04/Input-test.txt"
        "src/main/kotlin/y2023/d04/Input.txt"
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
    return input.lines().sumOf { line ->
        val newLine = line.replace(Regex("Card\\s+[0-9]+:"), "")
        val (win, guess) = newLine.split("|")
        val winSet = Regex("([0-9]+)[^:]").findAll(win).map { it.groupValues[1] }.map { it.toInt() }.toSet()
        val guessList = Regex("[0-9]+").findAll(guess).map(MatchResult::value).map { it.toInt() }
        var score = 0
        guessList.forEach {
            if (!winSet.contains(it)) {
                return@forEach
            }
            if (score == 0) score = 1
            else score *= 2
        }
        score
    }
}

private fun part2(input: String): Int {
    var totalCards = 0
    val cardQuantity = mutableMapOf<Int, Int>()
    input.lines().forEachIndexed { index, line ->
        val newLine = line.replace(Regex("Card\\s+[0-9]+:"), "")
        val (win, guess) = newLine.split("|")
        val winSet = Regex("([0-9]+)[^:]").findAll(win).map { it.groupValues[1] }.map { it.toInt() }.toSet()
        val guessList = Regex("[0-9]+").findAll(guess).map(MatchResult::value).map { it.toInt() }

        var wins = 0
        guessList.forEach {
            if (winSet.contains(it)) {
                wins++
            }
        }
        totalCards += cardQuantity.getOrDefault(index, 1)
        if (wins != 0) {
            for (k in index + 1 until index + 1 + wins) {
                val pre = cardQuantity.getOrDefault(k, 1)
                cardQuantity[k] = pre + cardQuantity.getOrDefault(index, 1)
            }
        }
    }
    return totalCards
}
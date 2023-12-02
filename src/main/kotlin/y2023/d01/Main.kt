package y2023.d01

import java.io.File
import kotlin.system.measureTimeMillis

fun main() {
    val input = File(
//        "src/main/kotlin/y2023/d01/Input-test.txt"
        "src/main/kotlin/y2023/d01/Input.txt"
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

fun part1(input: String): Int {
    return input.lines().sumOf { line ->
        Regex("([0-9])").findAll(line).map(MatchResult::value).map { it.toInt() }.first() * 10 +
                Regex("([0-9])").findAll(line).map(MatchResult::value).map { it.toInt() }.last()
    }
}

fun part2(input: String): Int {
    val newInput = input
        .replace("one", "o1e")
        .replace("two", "t2")
        .replace("three", "th3e")
        .replace("four", "4")
        .replace("five", "5e")
        .replace("six", "6")
        .replace("seven", "7n")
        .replace("eight", "e8t")
        .replace("nine", "n9e")

    return newInput.lines().sumOf { line ->
        println(line)
        println(Regex("([1-9])").findAll(line).map(MatchResult::value).map { it.toInt() }.first() * 10)
        println(Regex("([1-9])").findAll(line).map(MatchResult::value).map { it.toInt() }.last())
        println("---")
        Regex("([1-9])").findAll(line).map(MatchResult::value).map { it.toInt() }.first() * 10 +
                Regex("([1-9])").findAll(line).map(MatchResult::value).map { it.toInt() }.last()
    }
}
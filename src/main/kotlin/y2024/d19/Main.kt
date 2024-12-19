package y2024.d19

import utilities.*
import java.io.File
import kotlin.system.measureTimeMillis

private fun main() {
    val input = File(
//        "src/main/kotlin/y2024/d19/Input-test.txt"
        "src/main/kotlin/y2024/d19/Input.txt"
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

private fun part1(input: String): Int {
    val (towelsInput, patternsInput) = input.split("\n\n")
    val towels = towelsInput.split(", ")
    val pattens = patternsInput.lines()

    return pattens.count { pattern ->
        cachedIsPatternPossible(pattern, towels)
    }
}

private val cachedIsPatternPossible = {pattern: String, towels: List<String> -> isPatternPossible(pattern, towels) }.cache()
private fun isPatternPossible(pattern: String, towels: List<String>): Boolean {
    if (pattern.isEmpty()) return true
    return towels.any { towel ->
        if (pattern.startsWith(towel)) cachedIsPatternPossible(pattern.removePrefix(towel), towels)
        else false
    }
}

private fun part2(input: String): Long {
    val (towelsInput, patternsInput) = input.split("\n\n")
    val towels = towelsInput.split(", ")
    val pattens = patternsInput.lines()

    return pattens.sumOf { pattern ->
        cachedCountPatternPossible(pattern, towels)
    }
}

private val cachedCountPatternPossible = {pattern: String, towels: List<String> -> countPatternPossible(pattern, towels) }.cache()
private fun countPatternPossible(pattern: String, towels: List<String>): Long {
    if (pattern.isEmpty()) return 1
    return towels.sumOf { towel ->
        if (pattern.startsWith(towel)) cachedCountPatternPossible(pattern.removePrefix(towel), towels)
        else 0
    }
}
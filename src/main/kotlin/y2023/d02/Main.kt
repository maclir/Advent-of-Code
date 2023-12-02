package y2023.d02

import java.io.File
import kotlin.system.measureTimeMillis

fun main() {
    val input = File(
//        "src/main/kotlin/y2023/d02/Input-test.txt"
        "src/main/kotlin/y2023/d02/Input.txt"
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

// only 12 red cubes, 13 green cubes, and 14 blue cubes
fun part1(input: String): Int {
    return input.lines().sumOf { line ->
        val id = Regex("([0-9]+):").find(line)?.groupValues?.get(1)?.toInt() ?: 0
        val rawBuckets = Regex("([0-9]+ [a-z]+)").findAll(line).map(MatchResult::value)

        val reds = rawBuckets.filter { it.endsWith("red") }.maxOf { (Regex("([0-9]+)").find(it)?.value ?: "0").toInt() }
        val blues = rawBuckets.filter { it.endsWith("blue") }.maxOf { (Regex("([0-9]+)").find(it)?.value ?: "0").toInt() }
        val greens = rawBuckets.filter { it.endsWith("green") }.maxOf { (Regex("([0-9]+)").find(it)?.value ?: "0").toInt() }

//        println("id: $id, red: $reds, green: $greens, blue: $blues")
        if (reds <= 12 && greens <= 13 && blues <= 14) id
        else 0
    }
}

fun part2(input: String): Int {
    return input.lines().sumOf { line ->
        val id = Regex("([0-9]+):").find(line)?.groupValues?.get(1)?.toInt() ?: 0
        val rawBuckets = Regex("([0-9]+ [a-z]+)").findAll(line).map(MatchResult::value)

        val reds = rawBuckets.filter { it.endsWith("red") }.maxOf { (Regex("([0-9]+)").find(it)?.value ?: "0").toInt() }
        val blues = rawBuckets.filter { it.endsWith("blue") }.maxOf { (Regex("([0-9]+)").find(it)?.value ?: "0").toInt() }
        val greens = rawBuckets.filter { it.endsWith("green") }.maxOf { (Regex("([0-9]+)").find(it)?.value ?: "0").toInt() }

        reds * blues * greens
    }
}
package y2015.d09

import utilities.*
import java.io.File
import kotlin.system.measureTimeMillis

private fun main() {
    val input = File(
//        "src/main/kotlin/y2015/d09/Input-test.txt"
        "src/main/kotlin/y2015/d09/Input.txt"
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

// London to Dublin = 464
private data class Distance(
    val locations: Set<String>,
    val distance: Int,
)

private fun part1(input: String): Int {
    val distancePairs = input.lines().map { line ->
        val matches = """(.*) to (.*) = ([0-9]+)""".toRegex().find(line)!!
        Distance(
            setOf(matches.groupValues[1], matches.groupValues[2]),
            matches.groupValues[3].toInt(),
        )
    }
    val locations = distancePairs.map { it.locations }.flatten().toList().distinct()
    val distances = locations.associateWith { location ->
        distancePairs
            .filter { it.locations.contains(location) }
            .associate { it.locations.first { it != location } to it.distance }
    }

    return locations.permutations().minOf {
        it.windowed(2).sumOf { (a, b) -> distances.getValue(a).getValue(b) }
    }
}

private fun part2(input: String): Int {
    val distancePairs = input.lines().map { line ->
        val matches = """(.*) to (.*) = ([0-9]+)""".toRegex().find(line)!!
        Distance(
            setOf(matches.groupValues[1], matches.groupValues[2]),
            matches.groupValues[3].toInt(),
        )
    }
    val locations = distancePairs.map { it.locations }.flatten().toList().distinct()
    val distances = locations.associateWith { location ->
        distancePairs
            .filter { it.locations.contains(location) }
            .associate { it.locations.first { it != location } to it.distance }
    }

    return locations.permutations().maxOf {
        it.windowed(2).sumOf { (a, b) -> distances.getValue(a).getValue(b) }
    }
}
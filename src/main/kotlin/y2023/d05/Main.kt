package y2023.d05

import java.io.File
import kotlin.system.measureTimeMillis

private fun main() {
    val input = File(
//        "src/main/kotlin/y2023/d05/Input-test.txt"
        "src/main/kotlin/y2023/d05/Input.txt"
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

private data class Almanac(val destination: Long, val source: Long, val range: Long) {
  fun inRange(s: Long) = s >= source && s < source + range
  fun translate(s: Long) = (s - source) + destination
}

private fun part1(input: String): Long {
    val seeds = mutableListOf<Long>()
    val dict = mutableListOf<List<Almanac>>()
    input.split("\n\n").forEach { section ->
        with(section) {
            when {
                startsWith("seeds:") -> {
                    seeds.addAll(Regex("[0-9]+").findAll(this).map(MatchResult::value).map { it.toLong() })
                }

                else -> {
                    dict.add(section.lines().drop(1).map { line ->
                        val parts =
                            Regex("[0-9]+").findAll(line).map(MatchResult::value).map { it.toLong() }.toList()
                        Almanac(parts[0], parts[1], parts[2])
                    })
                }
            }
        }
    }

    return seeds.minOf { seed ->
        var currentNumber = seed
        dict.forEach dictLoop@{ list ->
            list.forEach { almanac ->
                if (almanac.inRange(currentNumber)) {
                    currentNumber = almanac.translate(currentNumber)
                    return@dictLoop
                }
            }
        }

        currentNumber
    }
}

private data class Seed(val start: Long, val range: Long)

private fun part2(input: String): Long {
    val seeds = mutableListOf<Seed>()
    val dict = mutableListOf<List<Almanac>>()
    input.split("\n\n").forEach { section ->
        with(section) {
            when {
                startsWith("seeds:") -> {
                    val seedParts =
                        Regex("[0-9]+").findAll(this).map(MatchResult::value).map { it.toLong() }.toList()
                    for (i in seedParts.indices step 2) {
                        seeds.add(Seed(seedParts[i], seedParts[i + 1]))
                    }
                }

                else -> {
                    dict.add(section.lines().drop(1).map { line ->
                        val parts =
                            Regex("[0-9]+").findAll(line).map(MatchResult::value).map { it.toLong() }.toList()
                        Almanac(parts[0], parts[1], parts[2])
                    })
                }
            }
        }
    }

    return seeds.minOf { seed ->
        (seed.start until seed.start + seed.range).minOf {
            var currentNumber = it
            dict.forEach dictLoop@{ list ->
                list.forEach { almanac ->
                    if (almanac.inRange(currentNumber)) {
                        currentNumber = almanac.translate(currentNumber)
                        return@dictLoop
                    }
                }
            }
            currentNumber
        }
    }
}

package y2015.d14

import utilities.*
import java.io.File

private fun main() {
    val input = File(
//        "src/main/kotlin/y2015/d14/Input-test.txt"
        "src/main/kotlin/y2015/d14/Input.txt"
    ).readText(Charsets.UTF_8)

    printRun(::part1, input)
    printRun(::part2, input)
}

private data class Reindeer(
    val speed: Int,
    val runtime: Int,
    val rest: Int,
) {
    fun kmAtSecond(seconds: Int): Int {
        val cycles = seconds / (runtime + rest)
        val remaining = seconds % (runtime + rest)
        return (cycles * runtime * speed) +
                (remaining.coerceAtMost(runtime) * speed)
    }
}

private fun part1(input: String): Int {
    val reindeer = input.lines().associate { line ->
        val matches =
            """(.*) can fly ([0-9]+) km/s for ([0-9]+) seconds, but then must rest for ([0-9]+) seconds.""".toRegex()
                .find(line)!!
        matches.groupValues[1] to Reindeer(
            matches.groupValues[2].toInt(),
            matches.groupValues[3].toInt(),
            matches.groupValues[4].toInt(),
        )
    }

    return reindeer.values.maxOf {
        it.kmAtSecond(2503)
    }
}

private fun part2(input: String): Int {
    val reindeer = input.lines().associate { line ->
        val matches =
            """(.*) can fly ([0-9]+) km/s for ([0-9]+) seconds, but then must rest for ([0-9]+) seconds.""".toRegex()
                .find(line)!!
        matches.groupValues[1] to Reindeer(
            matches.groupValues[2].toInt(),
            matches.groupValues[3].toInt(),
            matches.groupValues[4].toInt(),
        )
    }

    val points = reindeer.map {
        it.key to 0
    }.toMap().toMutableMap()

    for (seconds in 1..2503) {
        val results = reindeer.map { it.key to it.value.kmAtSecond(seconds) }.sortedBy { it.second }
        results.filter { it.second == results.last().second }
            .forEach { (winner, _) -> points[winner] = points.getValue(winner) + 1 }
    }
    return points.values.max()
}
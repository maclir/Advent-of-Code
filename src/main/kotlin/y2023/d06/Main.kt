package y2023.d06

import java.io.File
import kotlin.system.measureTimeMillis
import kotlin.time.times

fun main() {
    val input = File(
//        "src/main/kotlin/y2023/d06/Input-test.txt"
        "src/main/kotlin/y2023/d06/Input.txt"
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

data class Race(val time: Int, val distance: Int)

fun part1(input: String): Int {
    val races = Regex("[0-9]+").findAll(input.lines()[0]).map(MatchResult::value).map { it.toInt() }.zip(
        Regex("[0-9]+").findAll(input.lines()[1]).map(MatchResult::value).map { it.toInt() }
    ).map { Race(it.first, it.second) }

    var result = 1
    races.forEach { race ->
        for (i in 1 until race.time / 2) {
            if (i * (race.time - i) > race.distance) {
                result *= if (race.time % 2 == 0) (race.time / 2 - i) * 2 + 1
                else (race.time / 2 - i + 1) * 2
                break
            }
        }
    }
    return result
}

data class LongRace(val time: Long, val distance: Long)

fun part2(input: String): Long {
    val race = LongRace(
        Regex("[0-9]+").findAll(input.lines()[0].replace(" ", "")).map(MatchResult::value).map { it.toLong() }.first(),
        Regex("[0-9]+").findAll(input.lines()[1].replace(" ", "")).map(MatchResult::value).map { it.toLong() }.first()
    )

    var result = 1L

    for (i in 1 until race.time / 2) {
        if (i * (race.time - i) > race.distance) {
            result *= if (race.time % 2 == 0L) (race.time / 2 - i) * 2 + 1
            else (race.time / 2 - i + 1) * 2
            break
        }
    }

    return result
}

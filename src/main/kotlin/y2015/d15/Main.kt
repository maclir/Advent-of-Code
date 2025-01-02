package y2015.d15

import utilities.*
import java.io.File
import kotlin.math.max

private fun main() {
    val input = File(
//        "src/main/kotlin/y2015/d15/Input-test.txt"
        "src/main/kotlin/y2015/d15/Input.txt"
    ).readText(Charsets.UTF_8)

    printRun(::part1, input)
    printRun(::part2, input)
}

private data class Ingredient(
    val capacity: Int,
    val durability: Int,
    val flavor: Int,
    val texture: Int,
    val calories: Int,
)

private fun part1(input: String): Int {
    val ingredients = input.lines().map { line ->
        val matches =
            """(.*): capacity ([\-0-9]+), durability ([\-0-9]+), flavor ([\-0-9]+), texture ([\-0-9]+), calories ([\-0-9]+)""".toRegex()
                .find(line)!!
        Ingredient(
            matches.groupValues[2].toInt(),
            matches.groupValues[3].toInt(),
            matches.groupValues[4].toInt(),
            matches.groupValues[5].toInt(),
            matches.groupValues[6].toInt(),
        )
    }

    var maxScore = 0
    for (q0 in 0..100) {
        for (q1 in 0..100 - q0) {
            for (q2 in 0..100 - q0 - q1) {
                val q3 = 100 - q0 - q1 - q2
                val capacity = ingredients[0].capacity * q0 + ingredients[1].capacity * q1 + ingredients[2].capacity * q2 + ingredients[3].capacity * q3
                val durability = ingredients[0].durability * q0 + ingredients[1].durability * q1 + ingredients[2].durability * q2 + ingredients[3].durability * q3
                val flavor = ingredients[0].flavor * q0 + ingredients[1].flavor * q1 + ingredients[2].flavor * q2 + ingredients[3].flavor * q3
                val texture = ingredients[0].texture * q0 + ingredients[1].texture * q1 + ingredients[2].texture * q2 + ingredients[3].texture * q3
                if (capacity <= 0 || durability <= 0 || flavor <= 0 || texture <= 0) {
                    continue
                }
                maxScore = max(maxScore, capacity * durability * flavor * texture)
            }
        }
    }
    return maxScore
}

private fun part2(input: String): Int {
    val ingredients = input.lines().map { line ->
        val matches =
            """(.*): capacity ([\-0-9]+), durability ([\-0-9]+), flavor ([\-0-9]+), texture ([\-0-9]+), calories ([\-0-9]+)""".toRegex()
                .find(line)!!
        Ingredient(
            matches.groupValues[2].toInt(),
            matches.groupValues[3].toInt(),
            matches.groupValues[4].toInt(),
            matches.groupValues[5].toInt(),
            matches.groupValues[6].toInt(),
        )
    }

    var maxScore = 0
    for (q0 in 0..100) {
        for (q1 in 0..100 - q0) {
            for (q2 in 0..100 - q0 - q1) {
                val q3 = 100 - q0 - q1 - q2
                val calories = ingredients[0].calories * q0 + ingredients[1].calories * q1 + ingredients[2].calories * q2 + ingredients[3].calories * q3
                if (calories != 500) continue
                val capacity = ingredients[0].capacity * q0 + ingredients[1].capacity * q1 + ingredients[2].capacity * q2 + ingredients[3].capacity * q3
                val durability = ingredients[0].durability * q0 + ingredients[1].durability * q1 + ingredients[2].durability * q2 + ingredients[3].durability * q3
                val flavor = ingredients[0].flavor * q0 + ingredients[1].flavor * q1 + ingredients[2].flavor * q2 + ingredients[3].flavor * q3
                val texture = ingredients[0].texture * q0 + ingredients[1].texture * q1 + ingredients[2].texture * q2 + ingredients[3].texture * q3
                if (capacity <= 0 || durability <= 0 || flavor <= 0 || texture <= 0) {
                    continue
                }
                maxScore = max(maxScore, capacity * durability * flavor * texture)
            }
        }
    }
    return maxScore
}
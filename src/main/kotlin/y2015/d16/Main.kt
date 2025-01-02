package y2015.d16

import utilities.*
import java.io.File

private fun main() {
    val input = File(
//        "src/main/kotlin/y2015/d16/Input-test.txt"
        "src/main/kotlin/y2015/d16/Input.txt"
    ).readText(Charsets.UTF_8)

    printRun(::part1, input)
    printRun(::part2, input)
}

//Sue 1: children: 1, cars: 8, vizslas: 7

private fun part1(input: String): Int {
    val aunts = input.lines().associate { line ->
        val matches = """Sue ([0-9]+): .*""".toRegex().find(line)!!
        matches.groupValues[1].toInt() to
        """([a-z]+): ([0-9]+)""".toRegex().findAll(line).associate {
            it.groupValues[1] to it.groupValues[2].toInt()
        }
    }

    val properties = mapOf(
        "children" to 3,
        "cats" to 7,
        "samoyeds" to 2,
        "pomeranians" to 3,
        "akitas" to 0,
        "vizslas" to 0,
        "goldfish" to 5,
        "trees" to 3,
        "cars" to 2,
        "perfumes" to 1,
    )
    var filteredAunts = aunts.toMap()
    properties.forEach { (property, quantity) ->
        filteredAunts = filteredAunts.filter { aunt ->
            val pq = aunt.value[property]
            if (pq == null) {
                true
            } else {
                pq == quantity
            }
        }
    }
    return filteredAunts.entries.first().key
}

private fun part2(input: String): Int {
    val aunts = input.lines().associate { line ->
        val matches = """Sue ([0-9]+): .*""".toRegex().find(line)!!
        matches.groupValues[1].toInt() to
                """([a-z]+): ([0-9]+)""".toRegex().findAll(line).associate {
                    it.groupValues[1] to it.groupValues[2].toInt()
                }
    }

    val properties = mapOf(
        "children" to 3,
        "cats" to 7,
        "samoyeds" to 2,
        "pomeranians" to 3,
        "akitas" to 0,
        "vizslas" to 0,
        "goldfish" to 5,
        "trees" to 3,
        "cars" to 2,
        "perfumes" to 1,
    )
    var filteredAunts = aunts.toMap()
    properties.forEach { (property, quantity) ->
        filteredAunts = filteredAunts.filter { aunt ->
            val pq = aunt.value[property]
            if (pq == null) {
                true
            } else {
                when (property) {
                    "cats", "trees" -> pq > quantity
                    "pomeranians", "goldfish" -> pq < quantity
                    else -> pq == quantity
                }
            }
        }
    }
    return filteredAunts.entries.first().key
}
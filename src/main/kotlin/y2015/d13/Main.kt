package y2015.d13

import utilities.*
import java.io.File

private fun main() {
    val input = File(
//        "src/main/kotlin/y2015/d13/Input-test.txt"
        "src/main/kotlin/y2015/d13/Input.txt"
    ).readText(Charsets.UTF_8)

    printRun(::part1, input)
    printRun(::part2, input)
}

private fun part1(input: String): Int {
    val happiness = mutableMapOf<String, MutableMap<String, Int>>()
    input.lines().forEach { line ->
        val matches = """(.*) would (.*) ([0-9]+) happiness units by sitting next to (.*)\.""".toRegex().find(line)!!
        val from = matches.groupValues[1]
        val loseGain = matches.groupValues[2]
        val quantity = matches.groupValues[3].toInt()
        val to = matches.groupValues[4]
        happiness.getOrPut(from) {
            mutableMapOf()
        }[to] = if (loseGain == "lose") -quantity else quantity
    }

    val guests = happiness.keys.toList()

    return guests.permutations().maxOf { arrangement ->
        arrangement.windowed(2)
            .sumOf { (a, b) -> happiness.getValue(a).getValue(b) + happiness.getValue(b).getValue(a) }
            .plus(happiness.getValue(arrangement.first()).getValue(arrangement.last()))
            .plus(happiness.getValue(arrangement.last()).getValue(arrangement.first()))
    }
}

private fun part2(input: String): Int {
    val happiness = mutableMapOf<String, MutableMap<String, Int>>()
    input.lines().forEach { line ->
        val matches = """(.*) would (.*) ([0-9]+) happiness units by sitting next to (.*)\.""".toRegex().find(line)!!
        val from = matches.groupValues[1]
        val loseGain = matches.groupValues[2]
        val quantity = matches.groupValues[3].toInt()
        val to = matches.groupValues[4]
        happiness.getOrPut(from) {
            mutableMapOf()
        }[to] = if (loseGain == "lose") -quantity else quantity
    }
    val myName = "Reza"
    val guests = happiness.keys.toList().plus(myName)
    happiness[myName] = guests.associateWith { 0 }.toMutableMap()
    guests.forEach { guest -> happiness.getValue(guest)[myName] = 0 }

    return guests.permutations().maxOf { arrangement ->
        arrangement.windowed(2)
            .sumOf { (a, b) -> happiness.getValue(a).getValue(b) + happiness.getValue(b).getValue(a) }
            .plus(happiness.getValue(arrangement.first()).getValue(arrangement.last()))
            .plus(happiness.getValue(arrangement.last()).getValue(arrangement.first()))
    }
}
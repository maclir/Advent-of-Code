package y2023.d08

import java.io.File
import kotlin.system.measureTimeMillis
import kotlin.time.times

private fun main() {
    val input = File(
//        "src/main/kotlin/y2023/d08/Input-test.txt"
        "src/main/kotlin/y2023/d08/Input.txt"
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


private fun part1(input: String): Int {
    val instructions = input.lines().first().toCharArray()
    val map = input.lines().drop(2).map {line ->
        val parts = Regex("([A-Z]{3})").findAll(line).map(MatchResult::value).toList()
        parts[0] to Pair(parts[1], parts[2])
    }.toMap()

    var step = 0
    var currentNode = "AAA"
    while (currentNode != "ZZZ") {
        run breaking@{
            instructions.forEach { direction ->
                step++
                when (direction) {
                    'R' -> currentNode = map[currentNode]!!.second
                    'L' -> currentNode = map[currentNode]!!.first
                }
                if (currentNode == "ZZZ") return@breaking
            }
        }
    }

    return step
}

private fun part2(input: String): Long {
    val instructions = input.lines().first().toCharArray()
    val map = input.lines().drop(2).map {line ->
        val parts = Regex("([A-Z0-9]{3})").findAll(line).map(MatchResult::value).toList()
        parts[0] to Pair(parts[1], parts[2])
    }.toMap()

    var step = 0
    val currentNodes = map.filter { entry -> entry.key.endsWith('A') }.map { it.key }.toMutableList()
    val nodesToZSteps = mutableMapOf<Int, Int>()
    while (nodesToZSteps.size < currentNodes.size) {
        run breaking@{
            instructions.forEach {direction ->
                step++
                when (direction) {
                    'R' -> currentNodes.replaceAll { map[it]!!.second }
                    'L' -> currentNodes.replaceAll { map[it]!!.first }
                }
                currentNodes.forEachIndexed { index, node ->
                    if (!nodesToZSteps.contains(index) && node.endsWith('Z')) nodesToZSteps[index] = step
                }
                if (nodesToZSteps.size == currentNodes.size) return@breaking
            }
        }
    }

    val max = nodesToZSteps.values.max()
    var multiplier = 1L
    while (true) {
        multiplier++
        if (nodesToZSteps.values.all {
                (max * multiplier) % it == 0L
            }) break
    }
    return max * multiplier
}

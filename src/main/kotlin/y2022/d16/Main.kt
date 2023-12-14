package y2022.d16

import java.io.File
import kotlin.math.abs
import kotlin.math.min

private fun main() {
    val input = File("src/main/kotlin/y2022/d16/Input.txt").readText(Charsets.UTF_8)
//    val input = File("src/main/kotlin/y2022/d16/Input-test.txt").readText(Charsets.UTF_8)
//    println(part1(input))
    println(part2(input))
}

private fun part2(input: String): Int {
    val all = mutableMapOf<String, Valve>()
    input.lines().forEach { line ->
        val rate = Regex("[0-9]+").findAll(line).map(MatchResult::value).map { it.toInt() }.first()
        val names = Regex("[A-Z]{2}").findAll(line).map(MatchResult::value).toList()
        all[names[0]] = Valve(name = names[0], rate = rate, children = names.drop(1))
    }
    all.forEach { (name, valve) ->
        all.forEach { (oName, _) ->
            if (name == oName) return@forEach
            valve.othersDistance[oName] = getDistance(all, name, oName, mutableSetOf())
        }
    }

    val valves = all.filterValues { valve -> valve.rate > 0 }.keys.toMutableSet()

    var max = 0
    for (i in 0 until (1 shl valves.size)) {
        val valves1 = valves.filterIndexed { index, _ -> i and (1 shl index) != 0 }.toMutableSet()
        val valves2 = valves.filterIndexed { index, _ -> i and (1 shl index) == 0 }.toMutableSet()
        max = (calculateEnergy(26, "AA", all, valves1) + calculateEnergy(26, "AA", all, valves2)).coerceAtLeast(max)
    }

    return max
}

private fun part1(input: String): Int {
    val all = mutableMapOf<String, Valve>()
    input.lines().forEach { line ->
        val rate = Regex("[0-9]+").findAll(line).map(MatchResult::value).map { it.toInt() }.first()
        val names = Regex("[A-Z]{2}").findAll(line).map(MatchResult::value).toList()
        all[names[0]] = Valve(name = names[0], rate = rate, children = names.drop(1))
    }
    all.forEach { (name, valve) ->
        all.forEach { (oName, _) ->
            if (name == oName) return@forEach
            valve.othersDistance[oName] = getDistance(all, name, oName, mutableSetOf())
        }
    }

    val valvesLeft = all.filterValues { valve -> valve.rate > 0 }.keys.toMutableSet()
    return calculateEnergy(30, "AA", all, valvesLeft)
}

val cache = mutableMapOf<String, Int>()
private fun calculateEnergy(minutesLeft: Int, current: String, all: Map<String, Valve>, valvesLeft: MutableSet<String>): Int {
    if (minutesLeft <= 0 || valvesLeft.isEmpty()) return 0
    val cacheValue = cache["$minutesLeft|$current|$valvesLeft"]
    if (cacheValue != null) return cacheValue

    val newValvesLeft = valvesLeft.toMutableSet()
    newValvesLeft.remove(current)

    val currentRate = all[current]!!.rate
    val newPressureRelease = (minutesLeft - 1) * currentRate
    val newMinutesLeft = if (currentRate == 0) minutesLeft else minutesLeft - 1

    val result = all[current]!!.othersDistance.filterKeys { valve -> newValvesLeft.contains(valve) }
        .filterValues { distance -> newMinutesLeft - distance > 0 }.maxOfOrNull { (newCurrent, distance) ->
            newPressureRelease + calculateEnergy(newMinutesLeft - distance, newCurrent, all, newValvesLeft)
        } ?: newPressureRelease

    cache["$minutesLeft|$current|$valvesLeft"] = result
    return result
}

private fun getDistance(all: MutableMap<String, Valve>, name: String, oName: String, skipParents: MutableSet<String>): Int {
    val newSkipFriends = skipParents.toMutableSet()
    return if (all[name]!!.children.contains(oName)) 1
    else {
        newSkipFriends.add(name)
        1 + all[name]!!.children.minOf { child ->
            if (newSkipFriends.contains(child)) 999
            else getDistance(all, child, oName, newSkipFriends)
        }
    }
}

private data class Valve(
    val name: String,
    val rate: Int,
    val children: List<String>,
    val othersDistance: MutableMap<String, Int> = mutableMapOf(),
)

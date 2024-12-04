package y2023.d20

import utilities.leastCommonMultiple
import java.io.File
import kotlin.system.measureTimeMillis

private fun main() {
    val input = File(
//        "src/main/kotlin/y2023/d20/Input-test.txt"
        "src/main/kotlin/y2023/d20/Input.txt"
    ).readText(Charsets.UTF_8)

    println("${
        measureTimeMillis {
            println(
                "part1: ${part1(input)}"
            )
        }
    } ms\n")

    println("${
        measureTimeMillis {
            println(
                "part2: ${part2(input)}"
            )

        }
    } ms")
}

private sealed class Module {
    abstract val destinations: List<String>
    abstract fun relay(from: String, highPulse: Boolean): List<Pair<String, Boolean>>
}
private data class FlipFlop(var on: Boolean = false, override val destinations: List<String>): Module() {
    override fun relay(from: String,highPulse: Boolean): List<Pair<String, Boolean>> {
        if (highPulse) {
            return listOf()
        }

        on = !on
        return if (on) destinations.map { destination -> destination to true }
        else destinations.map { destination -> destination to false }
    }
}

private data class Conjunction(val inputs: MutableMap<String, Boolean> = mutableMapOf(), override val destinations: List<String>): Module() {
    override fun relay(from: String,highPulse: Boolean): List<Pair<String, Boolean>> {
        inputs[from] = highPulse
        return if (inputs.all { it.value }) destinations.map { destination -> destination to false }
        else destinations.map { destination -> destination to true }
    }
}

private data class Broadcast(override val destinations: List<String>): Module() {
    override fun relay(from: String, highPulse: Boolean): List<Pair<String, Boolean>> {
        return destinations.map { destination -> destination to highPulse }
    }
}


private fun part1(input: String): Int {
    val modules = input.lines().associate { line ->
        val (nameRaw, destinationsRaw) = line.split(" -> ")
        val destinations = destinationsRaw.split(", ")

        if (nameRaw.startsWith("broadcaster")) {
            nameRaw to Broadcast(destinations)
        } else if (nameRaw.startsWith('%')) {
            nameRaw.drop(1) to FlipFlop(destinations = destinations)
        } else {
            nameRaw.drop(1) to Conjunction(destinations = destinations)
        }
    }

    modules.filter { it.value is Conjunction }.forEach { (name, module) ->
        if (module is Conjunction) {
            module.inputs.putAll(modules.filter { it.value.destinations.contains(name) }.map { it.key to false })
        }
    }

    val queue = mutableListOf<Triple<String, String, Boolean>>()
    var lowCounter = 0
    var highCounter = 0
    repeat(1000) {
        queue.add(Triple("", "broadcaster", false))
        lowCounter++
        while (queue.isNotEmpty()) {
            val (from, moduleName, pulse) = queue.removeFirst()
            val module = modules[moduleName] ?: continue
            val newList = module.relay(from, pulse)
            queue.addAll(newList.map {
                if (it.second) highCounter++
                else lowCounter++
                Triple(moduleName, it.first, it.second)
            })
        }
    }

    return lowCounter * highCounter
}

private fun part2(input: String): Long {
    val modules = input.lines().associate { line ->
        val (nameRaw, destinationsRaw) = line.split(" -> ")
        val destinations = destinationsRaw.split(", ")

        if (nameRaw.startsWith("broadcaster")) {
            nameRaw to Broadcast(destinations)
        } else if (nameRaw.startsWith('%')) {
            nameRaw.drop(1) to FlipFlop(destinations = destinations)
        } else {
            nameRaw.drop(1) to Conjunction(destinations = destinations)
        }
    }

    modules.filter { it.value is Conjunction }.forEach { (name, module) ->
        if (module is Conjunction) {
            module.inputs.putAll(modules.filter { it.value.destinations.contains(name) }.map { it.key to false })
        }
    }

    val queue = mutableListOf<Triple<String, String, Boolean>>()
    var counter = 0
    val cMap = mutableMapOf(
        "qz" to 0,
        "tt" to 0,
        "jx" to 0,
        "cq" to 0,
    )
    while(true)  {
        counter++
        queue.add(Triple("", "broadcaster", false))
        while (queue.isNotEmpty()) {
            val (from, moduleName, pulse) = queue.removeFirst()
            val module = modules[moduleName] ?: continue
            val newList = module.relay(from, pulse)
            queue.addAll(newList.map { (destination, pulse) ->
                if (!pulse && destination in cMap.keys) {
                    cMap[destination] = counter
                }
                if (cMap.values.all { it != 0 }) {
                    return cMap.values.toList().leastCommonMultiple()
                }
                Triple(moduleName, destination, pulse)
            })
        }
    }

    return 0L
}
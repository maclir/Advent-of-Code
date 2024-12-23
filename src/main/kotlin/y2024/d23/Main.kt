package y2024.d23

import java.io.File
import kotlin.system.measureTimeMillis

private fun main() {
    val input = File(
//        "src/main/kotlin/y2024/d23/Input-test.txt"
        "src/main/kotlin/y2024/d23/Input.txt"
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

private fun part1(input: String): Int {
    val connectionsInput = input.lines().map { it.split("-") }
    val connectionMap = mutableMapOf<String, Set<String>>()
    connectionsInput.forEach { pair ->
        pair.forEach inner@{ x ->
            if (connectionMap.contains(x)) return@inner
            val foundConnections = mutableSetOf<String>()
            foundConnections.addAll(
                connectionsInput.filter { (a, b) ->
                    a == x || b == x
                }.map { (a, b) ->
                    if (a == x) b
                    else a
                }
            )
            connectionMap[x] = foundConnections
        }
    }
    val threeWayConnections = mutableSetOf<Set<String>>()
    connectionMap.forEach { (computer, connections) ->
        connections.forEach { computer2 ->
            connectionMap.getValue(computer2).forEach { computer3 ->
                if (connections.contains(computer3)) threeWayConnections.add(setOf(computer, computer2, computer3))
            }
        }
    }
    return threeWayConnections.filter { set ->
        set.any { it.startsWith('t') }
    }.size
}

private fun part2(input: String): Int {
    val connectionsInput = input.lines().map { it.split("-") }
    val connectionMap = mutableMapOf<String, Set<String>>()
    connectionsInput.forEach { pair ->
        pair.forEach { x ->
            val foundConnections = mutableSetOf<String>()
            foundConnections.addAll(
                connectionsInput.filter { (a, b) ->
                    a == x || b == x
                }.map { (a, b) ->
                    if (a == x) b
                    else a
                }
            )
            connectionMap[x] = foundConnections
        }
    }

    val allWayConnections = mutableSetOf<Set<String>>()
    connectionMap.forEach { (computer, connections) ->
        connections.forEach { computer2 ->
            val computerNetwork = sortedSetOf<String>()
            computerNetwork.add(computer)
            computerNetwork.add(computer2)
            connections.forEach { computer3 ->
                if (computerNetwork.all { connectionMap.getValue(it).contains(computer3) }) computerNetwork.add(computer3)
            }
            allWayConnections.add(computerNetwork)
        }
    }
    println(allWayConnections.maxBy { it.size }.joinToString(","))
    return 0
}
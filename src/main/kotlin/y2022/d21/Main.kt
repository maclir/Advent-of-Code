package y2022.d21

import java.io.File

fun main() {
    val input = File("src/main/kotlin/y2022/d21/Input.txt").readText(Charsets.UTF_8)
//    val input = File("src/main/kotlin/y2022/d21/Input-test.txt").readText(Charsets.UTF_8)
//    println(part1(input))
    println(part2(input))
}

fun part2(input: String): Long {
    val operations =
        input.lines().map { line -> line.split(" ") }.filter { parts -> parts[0] != "humn:" }.associate { parts ->
            parts[0].dropLast(1) to (if (parts[0] != "root:") parts.drop(1) else parts.drop(1)
                .mapIndexed { i, v -> if (i == 1) "=" else v }).toMutableList()
        }.toMutableMap()

    val variables = mutableMapOf<String, Long>()

    var previousSize: Int
    do {
        previousSize = operations.size
        val operationsIterator = operations.iterator()
        operationsIterator.forEachRemaining { (name, operation) ->
            if (operation.size == 1) {
                operationsIterator.remove()
                variables[name] = operation[0].toLong()
            } else {
                val (first, operator, second) = operation
                if (variables.containsKey(first) && variables.containsKey(second)) {
                    operationsIterator.remove()
                    val firstLong = variables[first]!!
                    val secondLong = variables[second]!!
                    variables[name] = when (operator) {
                        "+" -> firstLong + secondLong
                        "-" -> firstLong - secondLong
                        "/" -> firstLong / secondLong
                        "*" -> firstLong * secondLong
                        else -> -1L
                    }
                } else if (operator == "=" && variables.containsKey(first)) {
                    operationsIterator.remove()
                    variables[second] = variables[first]!!
                } else if (operator == "=" && variables.containsKey(second)) {
                    operationsIterator.remove()
                    variables[first] = variables[second]!!
                } else if (variables.containsKey(name)) {
                    if (variables.containsKey(first)) {
                        operationsIterator.remove()
                        val nameLong = variables[name]!!
                        val firstLong = variables[first]!!
                        variables[second] = when (operator) {
                            //name = first operator second
                            "+" -> nameLong - firstLong
                            "-" -> firstLong - nameLong
                            "/" -> firstLong / nameLong
                            "*" -> nameLong / firstLong
                            else -> -1L
                        }
                    } else if (variables.containsKey(second)) {
                        operationsIterator.remove()
                        val nameLong = variables[name]!!
                        val secondLong = variables[second]!!
                        variables[first] = when (operator) {
                            //name = first operator second
                            "+" -> nameLong - secondLong
                            "-" -> nameLong + secondLong
                            "/" -> nameLong * secondLong
                            "*" -> nameLong / secondLong
                            else -> -1L
                        }
                    }
                }
            }
        }
    } while (previousSize != operations.size)

    return variables["humn"]!!
}

fun part1(input: String): Long {
    val operations = input.lines().map { line -> line.split(" ") }.associate { parts ->
        parts[0].dropLast(1) to parts.drop(1)
    }.toMutableMap()

    val variables = mutableMapOf<String, Long>()

    while (operations.isNotEmpty()) {
        val operationsIterator = operations.iterator()
        operationsIterator.forEachRemaining { (name, operation) ->
            if (operation.size == 1) {
                operationsIterator.remove()
                variables[name] = operation[0].toLong()
            } else {
                val (first, operator, second) = operation
                if (variables.containsKey(first) && variables.containsKey(second)) {
                    operationsIterator.remove()
                    val firstLong = variables[first]!!
                    val secondLong = variables[second]!!
                    variables[name] = when (operator) {
                        "+" -> firstLong + secondLong
                        "-" -> firstLong - secondLong
                        "/" -> firstLong / secondLong
                        "*" -> firstLong * secondLong
                        else -> -1L
                    }
                }
            }
        }
    }

    return variables["root"]!!
}
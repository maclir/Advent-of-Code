package y2022.d21

import java.io.File

private fun main() {
    val input = File("src/main/kotlin/y2022/d21/Input.txt").readText(Charsets.UTF_8)
//    val input = File("src/main/kotlin/y2022/d21/Input-test.txt").readText(Charsets.UTF_8)
//    println(part1(input))
    println(part2(input))
}

private fun part2(input: String): Long {
    val operationsRaw =
        input.lines().map { line -> line.split(" ") }.filter { parts -> parts[0] != "humn:" }.associate { parts ->
            parts[0].dropLast(1) to (if (parts[0] != "root:") parts.drop(1) else parts.drop(1)
                .mapIndexed { i, v -> if (i == 1) "=" else v }).toMutableList()
        }
    val operations = mutableListOf<Pair<String, List<String>>>()
    operationsRaw.forEach { (name, operation) ->
        operations.add(name to operation)
        if (operation.size == 3) {
            val (first, operator, second) = operation
            when (operator) {
                "+" -> {
                    operations.add(first to listOf(name, "-", second))
                    operations.add(second to listOf(name, "-", first))
                }
                "-" -> {
                    operations.add(first to listOf(name, "+", second))
                    operations.add(second to listOf(first, "-", name))
                }
                "/" -> {
                    operations.add(first to listOf(name, "*", second))
                    operations.add(second to listOf(first, "/", name))
                }
                "*" -> {
                    operations.add(first to listOf(name, "/", second))
                    operations.add(second to listOf(name, "/", first))
                }
            }
        }
    }

    val variables = mutableMapOf<String, Long>()

    while (operations.isNotEmpty()) {
        operations.removeAll {(name, operation) ->
            if (operation.size == 1) {
                variables[name] = operation[0].toLong()
                return@removeAll true
            } else {
                val (first, operator, second) = operation
                if (variables.containsKey(first) && variables.containsKey(second)) {
                    val firstLong = variables[first]!!
                    val secondLong = variables[second]!!
                    variables[name] = when (operator) {
                        "+" -> firstLong + secondLong
                        "-" -> firstLong - secondLong
                        "/" -> firstLong / secondLong
                        "*" -> firstLong * secondLong
                        else -> -1L
                    }
                    return@removeAll true
                } else if (operator == "=" && variables.containsKey(first)) {
                    variables[second] = variables[first]!!
                    return@removeAll true
                } else if (operator == "=" && variables.containsKey(second)) {
                    variables[first] = variables[second]!!
                    return@removeAll true
                }
            }
            false
        }
    }

    return variables["humn"]!!
}

private fun part1(input: String): Long {
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
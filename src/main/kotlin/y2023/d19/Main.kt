package y2023.d19

import java.io.File
import kotlin.system.measureTimeMillis

private fun main() {
    val input = File(
//        "src/main/kotlin/y2023/d19/Input-test.txt"
        "src/main/kotlin/y2023/d19/Input.txt"
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

private enum class Category { X, M, A, S }
private data class Rule(val category: Category, val value: Int, val bigger: Boolean, val target: String)
private data class Workflow(val rules: List<Rule>, val fallbackTarget: String)
private data class Part(val ratings: Map<Category, Int>)

private fun part1(input: String): Int {
    val (rawWorkflows, rawParts) = input.split("\n\n")
    val workflows = rawWorkflows.lines().associate { line ->
        val (name, rawInstructions) = line.split('{')
        val instructions = rawInstructions.dropLast(1).split(',')
        val rules = instructions.dropLast(1).map { instruction ->
            val (rawRule, target) = instruction.split(':')
            val category = Category.valueOf(rawRule[0].uppercase())
            val bigger = rawRule[1] == '>'
            val value = rawRule.split(rawRule[1])[1].toInt()
            Rule(category, value, bigger, target)
        }
        name to Workflow(rules, instructions.last())
    }

    val parts = rawParts.lines().map { line ->
        Part(line.drop(1).dropLast(1).split(',').associate {
            val (category, rating) = it.split('=')
            Category.valueOf(category.uppercase()) to rating.toInt()
        })
    }

    return parts.filter { part ->
        var target = "in"
        while (target !in listOf("A", "R")) {
            run breaking@{
                val workflow = workflows[target]!!
                workflow.rules.forEach { rule ->
                    if (
                        (rule.bigger && part.ratings[rule.category]!! > rule.value) ||
                        (!rule.bigger && part.ratings[rule.category]!! < rule.value)
                    ) {
                        target = rule.target
                        return@breaking
                    }
                }
                target = workflow.fallbackTarget
            }
        }
        target == "A"
    }.sumOf { part ->
        part.ratings.values.sum()
    }
}

private fun part2(input: String): Long {
    val (rawWorkflows, rawParts) = input.split("\n\n")
    val workflows = rawWorkflows.lines().associate { line ->
        val (name, rawInstructions) = line.split('{')
        val instructions = rawInstructions.dropLast(1).split(',')
        val rules = instructions.dropLast(1).map { instruction ->
            val (rawRule, target) = instruction.split(':')
            val category = Category.valueOf(rawRule[0].uppercase())
            val bigger = rawRule[1] == '>'
            val value = rawRule.split(rawRule[1])[1].toInt()
            Rule(category, value, bigger, target)
        }
        name to Workflow(rules, instructions.last())
    }


    val acceptedRanges = mutableListOf<Map<Category, IntRange>>()
    val queue = mutableListOf<Pair<String, Map<Category, IntRange>>>()
    queue.add(
        "in" to mapOf(
            Category.X to 1..4000,
            Category.M to 1..4000,
            Category.A to 1..4000,
            Category.S to 1..4000,
        )
    )

    while (queue.isNotEmpty()) {
        val (workflowKey, validRangesOriginal) = queue.removeLast()
        val workflow = workflows[workflowKey]!!
        val remainingValidRanges = validRangesOriginal.toMutableMap()
        workflow.rules.forEach { rule ->
            val validRanges = remainingValidRanges.toMutableMap()
            val validRange = validRanges[rule.category]!!
            if (rule.bigger) {
                validRanges[rule.category] = rule.value + 1..validRange.last
                remainingValidRanges[rule.category] = validRange.first..rule.value
            } else {
                validRanges[rule.category] = validRange.first until rule.value
                remainingValidRanges[rule.category] = rule.value..validRange.last
            }
            when (rule.target) {
                "A" -> {
                    acceptedRanges.add(validRanges)
                }

                "R" -> {
                }

                else -> {
                    queue.add(rule.target to validRanges)
                }
            }
        }
        when (workflow.fallbackTarget) {
            "A" -> {
                acceptedRanges.add(remainingValidRanges)
            }

            "R" -> {
            }

            else -> {
                queue.add(workflow.fallbackTarget to remainingValidRanges)
            }
        }
    }
    return acceptedRanges.sumOf {
        it.values.fold(1L) { acc, range -> acc * range.count() }
    }
}
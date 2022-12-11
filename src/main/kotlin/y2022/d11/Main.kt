package y2022.d11

import java.io.File
import java.math.BigInteger
import kotlin.math.abs

data class Monkey(
    val items: MutableList<BigInteger>,
    val operation: (old: BigInteger) -> BigInteger,
    val test: (stress: BigInteger) -> Int,
    var inspectCounter: Int = 0,
)

fun main() {
    val input = File("src/main/kotlin/y2022/d11/Input.txt").readText(Charsets.UTF_8)
//    val input = File("src/main/kotlin/y2022/d11/Input-test.txt").readText(Charsets.UTF_8)
//    println(part1(input))
    println(part2(input))
}

fun part2(input: String): Long {
    val zeroBigInt = 0.toBigInteger()
    var masterDivide = 1.toBigInteger()
    val monkeys = mutableListOf<Monkey>()
    input.split("\n\n").map { lines ->
        val items =
            Regex("[0-9]+").findAll(lines.lines()[1]).map(MatchResult::value).map { it.toBigInteger() }.toMutableList()

        val (firstS, opS, secondS) = lines.lines()[2].removePrefix("  Operation: new = ").split(" ")
        val first = if (firstS == "old") zeroBigInt else firstS.toBigInteger()
        val second = if (secondS == "old") zeroBigInt else secondS.toBigInteger()
        val operation: (old: BigInteger) -> BigInteger
        when {
            firstS == "old" && secondS == "old" -> {
                operation = { old: BigInteger ->
                    when (opS) {
                        "+" -> old.add(old)
                        "*" -> old * old
                        else -> {
                            println("Unknown operator: $opS")
                            zeroBigInt
                        }
                    }
                }
            }
            firstS == "old" -> {
                operation = { old: BigInteger ->
                    when (opS) {
                        "+" -> second.add(old)
                        "*" -> second.multiply(old)
                        else -> {
                            println("Unknown operator: $opS")
                            zeroBigInt
                        }
                    }
                }
            }
            secondS == "old" -> {
                operation = { old: BigInteger ->
                    when (opS) {
                        "+" -> first.add(old)
                        "*" -> first.multiply(old)
                        else -> {
                            println("Unknown operator: $opS")
                            zeroBigInt
                        }
                    }
                }
            }
            else -> {
                operation = {
                    when (opS) {
                        "+" -> first.add(second)
                        "*" -> first.multiply(second)
                        else -> {
                            println("Unknown operator: $opS")
                            zeroBigInt
                        }
                    }
                }
            }
        }


        val divide = Regex("[0-9]+").findAll(lines.lines()[3]).map(MatchResult::value).map { it.toBigInteger() }.first()
        masterDivide *= divide
        val monT = lines.lines()[4].removePrefix("    If true: throw to monkey ").toInt()
        val monF = lines.lines()[5].removePrefix("    If false: throw to monkey ").toInt()

        val test = { stress: BigInteger -> if (stress.remainder(divide) == 0.toBigInteger()) monT else monF }

        monkeys.add(Monkey(items, operation, test))
    }


    for (round in 1..10000) {
        monkeys.forEach { monkey ->
            monkey.items.forEach {item ->
                monkey.inspectCounter++

                val worry = monkey.operation(item)
                monkeys[monkey.test(worry)].items.add(worry % masterDivide)
            }
            monkey.items.clear()
        }
        println("End of round $round")
//        monkeys.forEachIndexed { index, monkey ->
//            println("monkey $index: ${monkey.items}")
//        }
    }

    var total = 1L
    monkeys.sortedBy { it.inspectCounter }.subList(monkeys.size - 2, monkeys.size)
        .forEach { total *= it.inspectCounter }

    return total
}

fun part1(input: String): Int {
    val monkeys = mutableListOf<Monkey>()
    input.split("\n\n").map { lines ->
        val items =
            Regex("[0-9]+").findAll(lines.lines()[1]).map(MatchResult::value).map { it.toBigInteger() }.toMutableList()

        val (firstS, opS, secondS) = lines.lines()[2].removePrefix("  Operation: new = ").split(" ")
        val operation = { old: BigInteger ->
            val first = if (firstS == "old") old else firstS.toBigInteger()
            val second = if (secondS == "old") old else secondS.toBigInteger()
            when (opS) {
                "+" -> first + second
                "*" -> first * second
                else -> {
                    println("Unknown operator: $opS")
                    0.toBigInteger()
                }
            }
        }

        val divide = Regex("[0-9]+").findAll(lines.lines()[3]).map(MatchResult::value).map { it.toBigInteger() }.first()
        val monT = lines.lines()[4].removePrefix("    If true: throw to monkey ").toInt()
        val monF = lines.lines()[5].removePrefix("    If false: throw to monkey ").toInt()
        val test = { stress: BigInteger -> if (stress.remainder(divide) == 0.toBigInteger()) monT else monF }

        monkeys.add(Monkey(items, operation, test))
    }


    for (round in 1..20) {
        monkeys.forEach { monkey ->
            while (monkey.items.isNotEmpty()) {
                monkey.inspectCounter++
                val item = monkey.items.removeFirst()

                val worry = monkey.operation(item).divide(3.toBigInteger())
                monkeys[monkey.test(worry)].items.add(worry)
            }
        }
    }

    var total = 1
    monkeys.sortedBy { it.inspectCounter }.subList(monkeys.size - 2, monkeys.size)
        .forEach { total *= it.inspectCounter }

    return total
}

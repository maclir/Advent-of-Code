package y2024.d22

import utilities.Node
import utilities.charGrid
import utilities.intLines
import utilities.longLines
import java.io.File
import kotlin.system.measureTimeMillis

private fun main() {
    val input = File(
//        "src/main/kotlin/y2024/d22/Input-test.txt"
        "src/main/kotlin/y2024/d22/Input.txt"
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

private fun part1(input: String): Long {
    var secrets = input.lines().map { digits -> digits.toLong() }
    fun Long.mix(other: Long) = this.xor(other)
    fun Long.prune() = this % 16777216

    repeat(2000) {
        secrets = secrets.map { secret ->
            var newSecret = (secret * 64).mix(secret).prune()
            newSecret = (newSecret / 32).mix(newSecret).prune()
            (newSecret * 2048).mix(newSecret).prune()
        }
    }

    return secrets.sum()
}

private fun part2(input: String): Int {
    val secrets = input.lines().map { digits -> digits.toLong() }
    fun Long.mix(other: Long) = this.xor(other)
    fun Long.prune() = this % 16777216

    val sellersPrices = secrets.map { secret ->
        val prices = mutableListOf<Int>()
        var oldSecret = secret
        repeat(2000) {
            var newSecret = (oldSecret * 64).mix(oldSecret).prune()
            newSecret = (newSecret / 32).mix(newSecret).prune()
            newSecret = (newSecret * 2048).mix(newSecret).prune()
            val newPrice = (newSecret % 10).toInt()
            prices.add(newPrice)
            oldSecret = newSecret
        }
        prices.toList()
    }

    val sellerPricesDiffs = sellersPrices.map { prices ->
        val priceDiffs = mutableMapOf<List<Int>, Int>()
        for (i in 4..prices.lastIndex) {
            val key = listOf(
                prices[i - 4] - prices[i - 3],
                prices[i - 3] - prices[i - 2],
                prices[i - 2] - prices[i - 1],
                prices[i - 1] - prices[i],
            )
            if (!priceDiffs.contains(key)) priceDiffs[key] = prices[i]
        }
        priceDiffs
    }

    val changePatterns = mutableListOf<List<Int>>()
    for (a in -9..9) {
        for (b in -9..9) {
            for (c in -9..9) {
                for (d in -9..9) {
                    changePatterns.add(listOf(a, b, c, d))
                }
            }
        }
    }

    return changePatterns.maxOf { (a, b, c, d) ->
        sellerPricesDiffs.sumOf { prices ->
            prices.getOrDefault(listOf(a, b, c, d), 0)
        }
    }
}
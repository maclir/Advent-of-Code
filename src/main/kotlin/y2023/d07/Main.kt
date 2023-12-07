package y2023.d07

import java.io.File
import kotlin.system.measureTimeMillis
import kotlin.time.times

fun main() {
    val input = File(
//        "src/main/kotlin/y2023/d07/Input-test.txt"
        "src/main/kotlin/y2023/d07/Input.txt"
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

enum class Type {HIGH, PAIR, TWO_PAIR, THREE, FULL, FOUR, FIVE}
data class Round(val cards: List<Int>, val type: Type, val bid: Int)

fun part1(input: String): Int {
    val game = input.lines().map { line ->
        val (cardsRaw, bid) = line.split(" ")
        val cards = cardsRaw.map {
            when (it) {
                'T' -> 10
                'J' -> 11
                'Q' -> 12
                'K' -> 13
                'A' -> 14
                else -> it.digitToInt()
            }
        }

        val checkedQueue = mutableListOf<Int>()
        var type = Type.HIGH
        run breaking@{
            cards.forEach { card ->
                if (checkedQueue.contains(card)) {
                    return@forEach
                }
                checkedQueue.add(card)
                val count = cards.count { it == card }
                when (count) {
                    5 -> {
                        type = Type.FIVE
                        return@breaking
                    }
                    4 -> {
                        type = Type.FOUR
                        return@breaking
                    }
                    3 -> {
                        if (type == Type.PAIR) {
                            type = Type.FULL
                            return@breaking
                        } else type = Type.THREE
                    }
                    2 -> {
                        when (type) {
                            Type.THREE -> {
                                type = Type.FULL
                                return@breaking
                            }
                            Type.PAIR -> {
                                type = Type.TWO_PAIR
                                return@breaking
                            }
                            else -> type = Type.PAIR
                        }
                    }
                }
            }
        }

        Round(
            cards,
            type,
            bid.toInt()
        )
    }
    var sum = 0
    game.sortedWith (compareBy({it.type.ordinal}, {it.cards[0]}, {it.cards[1]}, {it.cards[2]}, {it.cards[3]}, {it.cards[4]} ))
        .onEachIndexed { index, round ->
            sum += (index + 1) * round.bid
        }

    return sum
}


fun part2(input: String): Int {
    val game = input.lines().map { line ->
        val (cardsRaw, bid) = line.split(" ")
        val cards = cardsRaw.map {
            when (it) {
                'T' -> 10
                'J' -> 0
                'Q' -> 12
                'K' -> 13
                'A' -> 14
                else -> it.digitToInt()
            }
        }

        val checkedQueue = mutableListOf<Int>()
        var type = Type.HIGH
        run breaking@{
            cards.forEach { card ->
                if (checkedQueue.contains(card) || card == 0) {
                    return@forEach
                }
                checkedQueue.add(card)
                val count = cards.count { it == card}
                when (count) {
                    5 -> {
                        type = Type.FIVE
                        return@breaking
                    }
                    4 -> {
                        type = Type.FOUR
                        return@breaking
                    }
                    3 -> {
                        if (type == Type.PAIR) {
                            type = Type.FULL
                            return@breaking
                        } else type = Type.THREE
                    }
                    2 -> {
                        when (type) {
                            Type.THREE -> {
                                type = Type.FULL
                                return@breaking
                            }
                            Type.PAIR -> {
                                type = Type.TWO_PAIR
                                return@breaking
                            }
                            else -> type = Type.PAIR
                        }
                    }
                }
            }
        }

//        enum class Type {HIGH, PAIR, TWO_PAIR, THREE, FULL, FOUR, FIVE}
        type = when (cards.count { it == 0}) {
            4, 5 -> Type.FIVE
            3 -> when(type) {
                Type.HIGH -> Type.FOUR
                Type.PAIR -> Type.FIVE
                else -> type
            }
            2 -> when(type) {
                Type.HIGH -> Type.THREE
                Type.PAIR -> Type.FOUR
                Type.THREE -> Type.FIVE
                else -> type
            }
            1 -> when(type) {
                Type.HIGH -> Type.PAIR
                Type.PAIR -> Type.THREE
                Type.TWO_PAIR -> Type.FULL
                Type.THREE -> Type.FOUR
                Type.FOUR -> Type.FIVE
                else -> type
            }
            else -> type
        }

        Round(
            cards,
            type,
            bid.toInt()
        )
    }
    var sum = 0
    game.sortedWith (compareBy({it.type.ordinal}, {it.cards[0]}, {it.cards[1]}, {it.cards[2]}, {it.cards[3]}, {it.cards[4]} ))
        .onEachIndexed { index, round ->
            sum += (index + 1) * round.bid
        }

    return sum
}
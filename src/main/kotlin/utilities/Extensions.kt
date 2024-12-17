package utilities

import java.math.BigInteger
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun Pair<Int, Int>.manhattanDistance(to: Pair<Int, Int>) = abs(first - to.first) + abs(second - to.second)

fun Int.leastCommonMultiple(other: Int): Int {
    val smaller = min(this, other)
    val bigger = max(this, other)
    var multiple = 1
    while (bigger * multiple % smaller != 0) multiple++
    return bigger * multiple
}

fun Long.leastCommonMultiple(other: Long): Long {
    val smaller = min(this, other)
    val bigger = max(this, other)
    var multiple = 1
    while (bigger * multiple % smaller != 0L) multiple++
    return bigger * multiple
}

fun List<Int>.leastCommonMultiple(): Long {
    var result = this[0].toLong()
    for (i in 1 until this.size) {
        result = result.leastCommonMultiple(this[i].toLong())
    }
    return result
}

fun <E> Iterable<E>.indicesOf(e: E) = mapIndexedNotNull { index, elem -> index.takeIf { elem == e } }

fun <E> List<E>.safeAccess(index: Int) = if (index in indices) this[index] else null

fun <T> List<T>.combinations(size: Int): List<List<T>> = when (size) {
    0 -> listOf(listOf())
    else -> flatMapIndexed { idx, element -> drop(idx + 1).combinations(size - 1).map { listOf(element) + it } }
}

fun String.intLines(delimiters: String = " ") =
    if (delimiters != "") this.lines().map { it.split(delimiters).map { digits -> digits.toInt() } }
    else this.lines().map { it.split(delimiters).drop(1).dropLast(1).map { digits -> digits.toInt() } }

fun String.longLines(delimiters: String = " ") =
    this.lines().map { it.split(delimiters).map { digits -> digits.toLong() } }

fun String.charGrid() = lines().map { it.toCharArray().toList() }

fun List<List<Any>>.print() = forEach { row ->
    row.forEach { print(it) }
    println()
}

fun Int.isPowerOf(number: Int): Boolean {
    if (this == 1) return (number == 1)

    var pow = 1
    while (pow < this) pow *= number

    return pow == this
}


fun BigInteger.isPowerOf(number: Int): Boolean {
    if (this == 1.toBigInteger()) return (number == 1)

    val bigIntegerNumber = number.toBigInteger()
    var pow = 1.toBigInteger()
    while (pow < this) pow *= bigIntegerNumber

    return pow == this
}

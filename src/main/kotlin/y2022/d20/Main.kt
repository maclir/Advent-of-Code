package y2022.d20

import java.io.File

fun main() {
    val input = File("src/main/kotlin/y2022/d20/Input.txt").readText(Charsets.UTF_8)
//    val input = File("src/main/kotlin/y2022/d20/Input-test.txt").readText(Charsets.UTF_8)
//    println(part1(input))
    println(part2(input))
}

fun part2(input: String): Long {
    val numbers = input.lines().map { line -> line.toLong() * 811589153}.mapIndexed{index, number ->
        index to number
    }.toMutableList()

    val size = numbers.size
    for (z in 1..10) {
        var k = 0
        while (k < size) {
            val mixedIndex = numbers.indexOfFirst { it.first == k }
            val (index, number) = numbers[mixedIndex]
            numbers.removeAt(mixedIndex)
            val newIndex = ((mixedIndex + number + (8115891530 * (size - 1))) % (size - 1)).toInt()
            numbers.add(newIndex, index to number)
            k++
        }
    }

    val index0 = numbers.indexOfFirst { (_, number) -> number == 0L }
    val indexes = listOf(1000, 2000, 3000)

    return indexes.sumOf { i ->
        val index = index0 + i - if (index0 + i > size) ((index0 + i) / numbers.size) * numbers.size else 0
        numbers[index].second
    }
}

fun part1(input: String): Int {
    val numbers = input.lines().map { line -> line.toInt() }.mapIndexed{index, number ->
        index to number
    }.toMutableList()

    var k = 0
    val size = numbers.size
    while (k < size) {
        val mixedIndex = numbers.indexOfFirst { it.first == k }
        val (index, number) = numbers[mixedIndex]
        numbers.removeAt(mixedIndex)
        val newIndex = (mixedIndex + number + (4 * (size - 1))) % (size - 1)
        numbers.add(newIndex, index to number)
        k++
    }

    val index0 = numbers.indexOfFirst { (_, number) -> number == 0 }
    val indexes = listOf(1000, 2000, 3000)

    return indexes.sumOf { i ->
        val index = index0 + i - if (index0 + i > size) ((index0 + i) / numbers.size) * numbers.size else 0
        numbers[index].second
    }
}
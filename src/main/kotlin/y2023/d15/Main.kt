package y2023.d15

import java.io.File
import kotlin.system.measureTimeMillis

private fun main() {
    val input = File(
//        "src/main/kotlin/y2023/d15/Input-test.txt"
        "src/main/kotlin/y2023/d15/Input.txt"
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

private fun part1(input: String): Int {
    return input.split(",").sumOf { step ->
        var acc = 0
        step.toCharArray().map { c ->
            acc += c.code
            acc *= 17
            acc %= 256
        }
        acc
    }
}

private fun part2(input: String): Long {
    val boxes = MutableList(256) { mutableListOf<Pair<String, Int>>() }
    input.split(",").forEach { step ->
        val (label, focal) = step.split(if (step.contains('=')) "=" else "-")

        var box = 0
        label.toCharArray().map { c ->
            box += c.code
            box *= 17
            box %= 256
        }

        if (step.contains('=')) {
            val index = boxes[box].indexOfFirst { it.first == label } ?: -1
            if (index != -1) {
                boxes[box][index] = label to focal.toInt()
            } else {
                boxes[box].add(label to focal.toInt())
            }
        } else {
            boxes[box].removeIf { it.first == label }
        }

    }
    var sum = 0L
    boxes.forEachIndexed { boxNumber, box ->
        var boxPower = 0L
        box.forEachIndexed { index, lens ->
            boxPower += (boxNumber + 1) * (index + 1) * lens.second
        }
        sum += boxPower
    }
    return sum
}
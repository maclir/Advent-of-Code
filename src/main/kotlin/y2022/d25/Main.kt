package y2022.d25

import java.io.File
import kotlin.system.measureTimeMillis

fun main() {
    val input = File(
//        "src/main/kotlin/y2022/d25/Input-test.txt"
        "src/main/kotlin/y2022/d25/Input.txt"
    ).readText(Charsets.UTF_8)

    println("${
        measureTimeMillis {
            println(
                part1(input)
//                part2(input)
            )
        }
    } ms")
}


fun part2(input: String): Int {
    return -1
}

fun part1(input: String): String {
    return input.lines().sumOf {
        it.customBaseToInt()
    }.customBase()
}

fun Long.customBase(): String {
    var carry = false
    return this.toString(5).reversed().map {
        val char = if (carry) {
            carry = false
            it.digitToInt().plus(1).digitToChar()
        } else it

        when (char) {
            '3' -> {
                carry = true
                '='
            }

            '4' -> {
                carry = true
                '-'
            }

            '5' -> {
                carry = true
                '0'
            }

            else -> char
        }
    }.let {
        if (carry) {
            carry = false
            it.plus('1')
        } else it
    }.reversed().joinToString("")
}


fun String.customBaseToInt(): Long {
    var carry = false
    return reversed().map {
        if (carry) {
            carry = false
            when (it) {
                '=' -> {
                    carry = true
                    '2'
                }

                '-' -> {
                    carry = true
                    '3'
                }

                '2' -> {
                    '1'
                }

                '1' -> {
                    '0'
                }
                '0' -> {
                    carry = true
                    4
                }
                else -> it
            }
        } else {
            when (it) {
                '=' -> {
                    carry = true
                    '3'
                }
                '-' -> {
                    carry = true
                    '4'
                }
                else -> it
            }
        }
    }.reversed().joinToString("").toLong(5)
}
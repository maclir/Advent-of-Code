package y2015.d04

import utilities.Node
import utilities.charGrid
import utilities.intLines
import utilities.longLines
import java.io.File
import java.math.BigInteger
import java.security.MessageDigest
import kotlin.system.measureTimeMillis

private fun main() {
    val input = File(
//        "src/main/kotlin/y2015/d04/Input-test.txt"
        "src/main/kotlin/y2015/d04/Input.txt"
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

private fun part1(secretKey: String): BigInteger {
    var number = 1.toBigInteger()
    while (true) {
        val hashCode = "$secretKey$number".md5()
        if (hashCode.startsWith("00000")) {
            return number
        }
        number++
    }
}

private fun part2(secretKey: String): BigInteger {
    var number = 1.toBigInteger()
    while (true) {
        val hashCode = "$secretKey$number".md5()
        if (hashCode.startsWith("000000")) {
            return number
        }
        number++
    }
}

private fun String.md5(): String {
    val md = MessageDigest.getInstance("MD5")
    return BigInteger(1, md.digest(this.toByteArray())).toString(16).padStart(32, '0')
}
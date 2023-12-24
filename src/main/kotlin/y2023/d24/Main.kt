package y2023.d24

import combinations
import java.io.File
import kotlin.system.measureTimeMillis

private fun main() {
    val input = File(
//        "src/main/kotlin/y2023/d24/Input-test.txt"
        "src/main/kotlin/y2023/d24/Input.txt"
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

private data class Node3D(val x: Double, val y: Double, val z: Double)
private data class Hail(val point: Node3D, val velocity: Node3D) {
    fun slope() = velocity.y / velocity.x
}

private fun part1(input: String): Int {
    val hails = input.lines().map { line ->
        val parts = Regex("[-0-9]+").findAll(line).map(MatchResult::value).map { it.toDouble() }.toList()
        Hail(Node3D(parts[0], parts[1], parts[2]), Node3D(parts[3], parts[4], parts[5]))
    }

    val lowerBound = 200000000000000.toDouble()
    val upperBound = 400000000000000.toDouble()
//    val lowerBound = 7.toDouble()
//    val upperBound = 27.toDouble()

    return hails.combinations(2).count {
        val intersection = calc2DOverlap(it[0], it[1]) ?: return@count false
        intersection.first in lowerBound..upperBound && intersection.second in lowerBound..upperBound
    }
}

private fun calc2DOverlap(h1: Hail, h2: Hail): Pair<Double, Double>? {
    fun validFuture(h: Hail, cx: Double, cy: Double): Boolean {
        return !((h.velocity.x < 0 && h.point.x < cx) || (h.velocity.x > 0 && h.point.x > cx) || (h.velocity.y < 0 && h.point.y < cy) || (h.velocity.y > 0 && h.point.y > cy))
    }

    if (h1.slope() == h2.slope()) return null
    val cx =
        ((h2.slope() * h2.point.x) - (h1.slope() * h1.point.x) + h1.point.y - h2.point.y) / (h2.slope() - h1.slope())
    val cy = (h1.slope() * (cx - h1.point.x)) + h1.point.y
    val valid = validFuture(h1, cx, cy) && validFuture(h2, cx, cy)

    if (!valid) return null

    return cx to cy
}

private data class Node3DLong(val x: Long, val y: Long, val z: Long)
private data class HailLong(val point: Node3DLong, val velocity: Node3DLong)

private fun part2(input: String): Long {
    val hails = input.lines().map { line ->
        val parts = Regex("[-0-9]+").findAll(line).map(MatchResult::value).map { it.toLong() }.toList()
        HailLong(Node3DLong(parts[0], parts[1], parts[2]), Node3DLong(parts[3], parts[4], parts[5]))
    }

    val h1 = hails[0]
    val h2 = hails[1]

    val range = 500
    for (vx in -range..range) {
        for (vy in -range..range) {
            for (vz in -range..range) {
                if (vx == 0 || vy == 0 || vz == 0) continue

                val A = h1.point.x
                val a = h1.velocity.x - vx
                val B = h1.point.y
                val b = h1.velocity.y - vy
                val C = h2.point.x
                val c = h2.velocity.x - vx
                val D = h2.point.y
                val d = h2.velocity.y - vy

                if (c == 0L || (a * d) - (b * c) == 0L) continue

                val t = (d * (C - A) - c * (D - B)) / ((a * d) - (b * c))

                val x = h1.point.x + h1.velocity.x * t - vx * t
                val y = h1.point.y + h1.velocity.y * t - vy * t
                val z = h1.point.z + h1.velocity.z * t - vz * t


                var hitAll = true
                for (hail in hails) {
                    val u = if (hail.velocity.x.toInt() != vx) {
                        (x - hail.point.x) / (hail.velocity.x - vx)
                    } else if (hail.velocity.y.toInt() != vy) {
                        (y - hail.point.y) / (hail.velocity.y - vy)
                    } else if (hail.velocity.z.toInt() != vz) {
                        (z - hail.point.z) / (hail.velocity.z - vz)
                    } else {
                        throw Exception("WTF!!")
                    }

                    if ((x + u * vx != hail.point.x + u * hail.velocity.x) || (y + u * vy != hail.point.y + u * hail.velocity.y) || (z + u * vz != hail.point.z + u * hail.velocity.z)) {
                        hitAll = false
                        break
                    }
                }

                if (hitAll) {
                    println(x)
                    println(y)
                    println(z)
                    return x + y + z
                }
            }
        }
    }

    return 0
}

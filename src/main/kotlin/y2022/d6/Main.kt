package y2022.d6

import java.io.File

private fun main() {
    val inputL = File("src/main/kotlin/y2022/d6/Input.txt").readText(Charsets.UTF_8).split("").toMutableList()
    inputL.removeFirst()
    for (i in 13..inputL.size-1) {
        val nIn = inputL.subList(i - 13, i + 1)
        if (nIn.distinct().size == 14) {
            println(i + 1)
            break
        }
    }
}
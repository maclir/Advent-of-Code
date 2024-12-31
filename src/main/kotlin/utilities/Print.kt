package utilities

import kotlin.reflect.KFunction1
import kotlin.time.measureTimedValue

fun <T> printRun(function: KFunction1<String, T>, input: String) {
    val (result, duration) = measureTimedValue {
        function(input)
    }

    println("${function.name}: ${result}\n${duration.inWholeMilliseconds} ms")
    if (duration.inWholeSeconds > 0) {
        println("${duration.inWholeSeconds} s")
    }
    if (duration.inWholeMinutes > 0) {
        println("${duration.inWholeMinutes} m")
    }
    println()
}
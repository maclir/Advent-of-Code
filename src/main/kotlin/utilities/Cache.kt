package utilities

fun <X, R> ((X) -> R).cache(): (X) -> R {
    val cache = hashMapOf<X, R>()
    mutableMapOf<X, R>()
    return {
        cache.getOrPut(it, { this(it) })
    }
}

fun <X, Y, R> ((X, Y) -> R).cache(): (X, Y) -> R {
    val cache = hashMapOf<Pair<X, Y>, R>()
    mutableMapOf<Pair<X, Y>, R>()
    return { x, y ->
        cache.getOrPut(x to y, { this(x, y) })
    }
}
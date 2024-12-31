package y2024

fun main() {
    println("advent 25")

    dataForAdvent25.data1.toFittingPairs()
        .also { println("Task 1 for data 1 should be 3 and is ... ${it.size}") }

    dataForAdvent25.data2.toFittingPairs()
        .also { println("Task 1 for data 2 should be 3356 and is ... ${it.size}") }
}

private fun String.toFittingPairs(): List<Pair<Key, Lock>> =
    split("\n\n")
    .map { it.toLockOrKey() }
//    .onEach { println(it) }
    .toFittingPairs()


private fun List<LockOrKey>.toFittingPairs(): List<Pair<Key, Lock>> {
    val keys = filterIsInstance<Key>()
    val locks = filterIsInstance<Lock>()
    val pairs = mutableListOf<Pair<Key, Lock>>()
    for (key in keys) {
        for (lock in locks) {
            if (key.pins.zip(lock.pins).none { it.first + it.second > 5 }) {
                pairs.add(key to lock)
            }
        }
    }
    return pairs
}

private fun String.toLockOrKey(): LockOrKey {
    val rows = lines()
    val pins = mutableListOf<Int>()
    for (j in 0 until rows[0].length) {
        var count = 0
        for (i in 1 until rows.size-1) {
            if (rows[i][j] == '#') count++
        }
        pins.add(count)
    }
    return if (rows[0][0] == '.') Key(pins)
    else Lock(pins)
}

private sealed class LockOrKey {}
private data class Lock(val pins: List<Int>) : LockOrKey()
private data class Key(val pins: List<Int>) : LockOrKey()
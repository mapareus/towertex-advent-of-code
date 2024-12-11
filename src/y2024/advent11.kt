package y2024

import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

fun main() {
    println("advent 11")

    runBlocking {
        dataForAdvent11.data1
            .countAfterBlinks(1)
            .also { println("Task 1 for data 1 1x should be 7 and is ... $it") }
        dataForAdvent11.data2
            .countAfterBlinks(6)
            .also { println("Task 1 for data 2 6x should be 22 and is ... $it") }
        dataForAdvent11.data2
            .countAfterBlinks(25)
            .also { println("Task 1 for data 2 25x should be 55312 and is ... $it") }
        measureTimeMillis {
            dataForAdvent11.data3
                .countAfterBlinks1(25)
                .also { println("Task 1 for data 3 25 should be 194782 and is ... $it") }
        }.also { println("took $it ms") }
        measureTimeMillis {
            dataForAdvent11.data3
                .countAfterBlinks1(25)
                .also { println("Task 1 for data 3 25 should be 194782 and is ... $it") }
        }.also { println("took $it ms") }
        measureTimeMillis {
            dataForAdvent11.data3
                .countAfterBlinks3(25)
                .also { println("Task 1 for data 3 25x should be 194782 and is ... $it") }
        }.also { println("took $it ms") }
        measureTimeMillis {
            dataForAdvent11.data3
                .countAfterBlinks4(25)
                .also { println("Task 1 for data 3 25x should be 194782 and is ... $it") }
        }.also { println("took $it ms") }

        measureTimeMillis {
            dataForAdvent11.data3
                .countAfterBlinks(75)
                .also { println("Task 1 for data 3 75x should be 233007586663131 and is ... $it") }
        }.also { println("took $it ms") }
    }
}

private fun String.countAfterBlinks(times: Int): Long = countAfterBlinks4(times)

//FOURTH VERSION

private fun String.countAfterBlinks4(max: Int): Long {
    return this
        .split(" ")
        .map { it.toLong().blink(max-1, mutableMapOf()) }
        .sum()
}

private fun Long.blink(times: Int, cache: MutableMap<Pair<Long, Int>, Long>): Long {
    if (this@blink == 0L) {
        if (times == 0) return 1L
        cache[Pair(0L, times)]?.let { return it }
        return 1L.blink(times-1, cache)
            .also { cache[Pair(0L, times)] = it }
    }
    val s = this@blink.toString()
    if (s.length % 2 == 0) {
        if (times == 0) return 2L
        cache[Pair(this@blink, times)]?.let { return it }
        val half = s.length / 2
        return (s.substring(0, half).toLong().blink(times-1, cache) +
                s.substring(half).toLong().blink(times-1, cache))
            .also { cache[Pair(this@blink, times)] = it }
    }
    if (times == 0) return 1L
//    cache[Pair(this@blink, times)]?.let { return it }
    return (this@blink*2024).blink(times-1, cache)
//        .also { cache[Pair(this@blink, times)] = it }
}

//THIRD VERSION

private fun String.countAfterBlinks3(max: Int): Long {
    return this
        .split(" ")
        .map { it.toLong().blink(max-1) }
        .sum()
}

private fun Long.blink(times: Int): Long {
    if (this@blink == 0L) {
        if (times == 0) return 1L
        return 1L.blink(times-1)
    }
    val s = this@blink.toString()
    if (s.length % 2 == 0) {
        if (times == 0) return 2L
        val half = s.length / 2
        return (s.substring(0, half).toLong().blink(times-1) +
                s.substring(half).toLong().blink(times-1))
    }
    if (times == 0) return 1L
    return (this@blink*2024).blink(times-1)
}

//SECOND VERSION

private suspend fun String.countAfterBlinks2(times: Int): Long {
    var count = 0L
    this
        .split(" ")
        .map { it.toLong() }
        .blink(times)
        .collect {
            count++
//            println("$count...$it")
        }
    return count
}

private fun List<Long>.blink(times: Int): Flow<Long> {
    var resultFlow = this.asFlow()
    repeat(times) {
        resultFlow = resultFlow.blink()
    }
    return resultFlow
}

private fun Flow<Long>.blink(): Flow<Long> = this
    .flatMapMerge { it.blink() }

private fun Long.blink(): Flow<Long> = flow {
    if (this@blink == 0L) {
        emit(1L)
        return@flow
    }
    val s = this@blink.toString()
    if (s.length % 2 == 0) {
        val half = s.length / 2
        emit(s.substring(0, half).toLong())
        emit(s.substring(half).toLong())
        return@flow
    }
    emit(this@blink*2024)
}

//FIRST VERSION

private fun String.countAfterBlinks1(times: Int): Int = this
    .split(" ")
    .map { it.toLong() }
    .let {
        (1..times).fold(it) { acc, _ ->
//            println(acc.size)
            acc.blink() }
    }
//    .also { println(it) }
    .size

private fun List<Long>.blink(): List<Long> = this
    .flatMap {
        if (it == 0L) return@flatMap listOf(1L)
        val s = it.toString()
        if (s.length % 2 == 0) {
            val half = s.length / 2
            val firstHalf = s.substring(0, half)
            val secondHalf = s.substring(half)
            return@flatMap listOf(firstHalf.toLong(), secondHalf.toLong())
        }
        listOf(it*2024)
    }
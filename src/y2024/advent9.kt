package y2024

import kotlin.time.Duration.Companion.seconds

fun main() {
    println("advent 9")

    dataForAdvent9.data1.flatExpandCompressAndFold()
        .also { println("Task 1 for data 1 should be 60 and is ... $it") }
    dataForAdvent9.data2.flatExpandCompressAndFold()
        .also { println("Task 1 for data 2 should be 1928 and is ... $it") }
    dataForAdvent9.data3.flatExpandCompressAndFold()
        .also { println("Task 1 for data 3 should be 6382875730645 and is ... $it") }

    dataForAdvent9.data2.expandCompressAndFold()
        .also { println("Task 2 for data 2 should be 2858 and is ... $it") }
    dataForAdvent9.data3.expandCompressAndFold()
        .also { println("Task 2 for data 3 should be 6420913943576 and is ... $it") }
}

private fun String.flatExpandCompressAndFold(): Long =
    this.flatExpand()
//        .also { expanded ->
//            expanded.joinToString("") { "${it ?: '.'}" }
//                .also { println(it) }
//        }
        .compress()
//        .also { compressed ->
//            compressed.joinToString("") { "$it" }
//                .also { println(it) }
//        }
        .foldIndexed(0L) { i, sum, value -> sum + i * value }

private fun String.expandCompressAndFold(): Long =
    this.expand()
//        .also { it1 -> println(it1.printString()) }
        .compress()

private fun List<Pair<Int,Int?>>.printString() = joinToString("") { it2 -> List(it2.first) {it2.second ?: '.'}.joinToString("") { it.toString() } }

private fun List<Pair<Int,Int?>>.compress(): Long {
    val originalPairs = this.toMutableList()
    val result = this.toMutableList()
    originalPairs.reversed().forEachIndexed { indexOfPairToMove, pairToMove ->
        if (pairToMove.second == null) return@forEachIndexed
        val indexOfEmptyGroupWithEnoughCapacity = result.indexOfFirst { it.second == null && it.first >= pairToMove.first }
        val indexOfPairToMoveInResult = result.indexOf(pairToMove)
        if (indexOfEmptyGroupWithEnoughCapacity == -1 || indexOfEmptyGroupWithEnoughCapacity >= indexOfPairToMoveInResult) return@forEachIndexed
        result.set(indexOfPairToMoveInResult, Pair(pairToMove.first, null))
        val emptyGroupToFill = result[indexOfEmptyGroupWithEnoughCapacity]
        result.remove(emptyGroupToFill)
        result.add(indexOfEmptyGroupWithEnoughCapacity, Pair(emptyGroupToFill.first - pairToMove.first, null))
        result.add(indexOfEmptyGroupWithEnoughCapacity, pairToMove)
//        println(result.printString())
    }
    return result.flatMap { (c, value) -> List(c) { value } }
        .foldIndexed(0L) { i, sum, value -> sum + i * (value ?: 0) }
}

private fun List<Int?>.compress(): List<Int> {
    val result = mutableListOf<Int>()
    var lastIndex = indexOfLast { it != null }
    var index = 0
    while (index <= lastIndex ) {
        val value = get(index)
        if (value != null) {
            result.add(value)
            index++
            continue
        }
        val lastValue = get(lastIndex)
        if (lastValue == null) {
            lastIndex--
            continue
        }
        result.add(lastValue)
        index++
        lastIndex--
    }
    return result
}

private fun String.expand(): List<Pair<Int,Int?>> = mapIndexed { i, c ->
    if (i % 2 == 0) Pair(c.digitToInt(), i / 2)
    else Pair(c.digitToInt(), null)
}

private fun String.flatExpand(): List<Int?> = flatMapIndexed { i, c ->
        if (i % 2 == 0) List(c.digitToInt()) { i / 2 }
        else List(c.digitToInt()) { null }
    }
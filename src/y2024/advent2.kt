package y2024

import kotlin.math.abs

fun main() {
    println("advent 2")
    println("Task 1 for data 1 should be 2 and is ...")
    dataForAdvent2.data1.printSafeReportCount()
    println("Task 1 for data 2 is ...")
    dataForAdvent2.data2.printSafeReportCount()
    println("Task 2 for data 2 is ...")
    println(part2)
}

private fun String.printSafeReportCount() {
    println(this.lines().count { it.split(" ").isSafe() })
}

private fun List<String>.toDiffs() = zipWithNext().map { it.second.toInt() - it.first.toInt() }

private fun List<String>.isSafe(): Boolean {
    return toDiffs().let { diff ->
        diff.all { abs(it) in 1..3 }
                && (diff.all { it > 0 } || diff.all { it < 0 })
    }
}
private val Iterable<Int>.safe get() = (all { it > 0 } || all { it < 0 }) && all { abs(it) in 1..3 }
private val Iterable<Int>.diffs get() = windowed(2) { it.first() - it.last() }

private val input2 = dataForAdvent2.data2
    .lineSequence()
    .filterNot { it.isBlank() }
    .map { it.split(' ').map { it.toInt() } }

private val part2 = input2
    .map { line ->
        buildList {
            add(line)
            line.forEachIndexed { i, _ ->
                add(line.toMutableList().apply { removeAt(i) })
            }
        }
    }
    .map { it.map { it2 -> it2.diffs } }
    .map { it.any { it2 -> it2.safe } }
    .count { it }
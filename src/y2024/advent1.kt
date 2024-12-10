package y2024

import java.util.LinkedList
import kotlin.math.abs

fun main() {
    println("advent 1")

    dataForAdvent1.data1.toTwoColumns().sumSortedDifferences()
        .also { println("Task 1 for data 1 should be 11 and is $it") }

    dataForAdvent1.data3.toTwoColumns().sumSortedDifferences()
        .also { println("Task 1 for data 3 is $it") }

    dataForAdvent1.data2.toTwoColumns().sumGroupSizes()
        .also { println("Task 2 for data 2 should be 31 and is $it") }

    dataForAdvent1.data3.toTwoColumns().sumGroupSizes()
        .also { println("Task 2 for data 3 is $it") }
}

private fun Pair<LinkedList<Int>, LinkedList<Int>>.sumGroupSizes(): Int {
    val firstMap = first.groupBy { it }
    val secondMap = second.groupBy { it }
    return firstMap.keys.fold(0) { acc, key ->
        acc + (key * firstMap.getOrDefault(key, emptyList()).size * secondMap.getOrDefault(key, emptyList()).size)
    }
}

private fun Pair<LinkedList<Int>, LinkedList<Int>>.sumSortedDifferences(): Int {
    first.sort()
    second.sort()
    return first.foldIndexed(0) { index, acc, i ->
        acc + abs(second[index] - i)
    }
}

private fun String.toTwoColumns(): Pair<LinkedList<Int>, LinkedList<Int>> {
    val firstRow: LinkedList<Int> = LinkedList()
    val secondRow: LinkedList<Int> = LinkedList()
    this.lines()
        .forEach { line ->
            line.trim().split("   ").apply {
                firstRow.add(this[0].toInt())
                secondRow.add(this[1].toInt())
            }
        }
    return Pair(firstRow, secondRow)
}

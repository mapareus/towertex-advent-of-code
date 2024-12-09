package y2024

fun main() {
    println("advent 5")
    dataForAdvent5.data1.sumMiddleNumbersFromCorrectlyOrderedLines()
        .also { println("Task 1 for data 1 should be 143 and is ... $it") }
    dataForAdvent5.data2.sumMiddleNumbersFromCorrectlyOrderedLines()
        .also { println("Task 1 for data 2 is ... $it") }

    dataForAdvent5.data1.sumMiddleNumbersFromIncorrectlyOrderedLines()
        .also { println("Task 2 for data 1 should be 123 and is ... $it") }
    dataForAdvent5.data2.sumMiddleNumbersFromIncorrectlyOrderedLines()
        .also { println("Task 2 for data 2 is ... $it") }
}

private fun String.sumMiddleNumbersFromIncorrectlyOrderedLines(): Int {
    val mapOfOutOfOrder = mapOfNumbersWhichWouldBeOutOfOrder()
    return parseLinesToBeOrdered()
        .filterNot { it.isOrderedByOutOfOrderMap(mapOfOutOfOrder) }
        .map { line ->
            line.sortedWith(Comparator { o1, o2 ->
                if (mapOfOutOfOrder[o2]?.contains(o1) == true) return@Comparator -1
                if (mapOfOutOfOrder[o1]?.contains(o2) == true) return@Comparator 1
                0
            })
        }
        .sumOf { it[it.size / 2] }
}

private fun String.sumMiddleNumbersFromCorrectlyOrderedLines(): Int {
    val mapOfOutOfOrder = mapOfNumbersWhichWouldBeOutOfOrder()
    return parseLinesToBeOrdered()
        .filter { it.isOrderedByOutOfOrderMap(mapOfOutOfOrder) }
        .sumOf { it[it.size / 2] }
}

private fun String.parseLinesToBeOrdered() =
    substringAfter("\n\n")
    .lines()
    .map { it.split(",").map { i -> i.toInt() } }

private fun List<Int>.isOrderedByOutOfOrderMap(map: Map<Int, List<Int>>): Boolean {
    val listOfOutOfOrder = mutableListOf<Int>()
    forEach { number ->
        if (listOfOutOfOrder.contains(number)) {
            return false
        }
        map[number]?.also { listOfOutOfOrder.addAll(it) }
    }
    return true
}

private fun String.mapOfNumbersWhichWouldBeOutOfOrder() =
    substringBefore("\n\n")
    .split("\n")
    .map { it.split("|").map { i -> i.toInt() } }
    .groupBy { it[1] }
    .mapValues { pairs -> pairs.value.map { it[0] } }

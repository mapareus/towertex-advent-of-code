package y2024

fun main() {
    println("advent 12")

    dataForAdvent12.data1.getGroups().sumOf { it.price1 }
        .also { println("Task 1 for data 1 should be 140 and is ... $it") }
    dataForAdvent12.data2.getGroups().sumOf { it.price1 }
        .also { println("Task 1 for data 2 should be 772 and is ... $it") }
    dataForAdvent12.data3.getGroups().sumOf { it.price1 }
        .also { println("Task 1 for data 3 should be 1930 and is ... $it") }
    dataForAdvent12.data6.getGroups().sumOf { it.price1 }
        .also { println("Task 1 for data 6 should be 1450816 and is ... $it") }

    dataForAdvent12.data1.getGroups().sumOf { it.price2 }
        .also { println("Task 2 for data 1 should be 80 and is ... $it") }
    dataForAdvent12.data2.getGroups().sumOf { it.price2 }
        .also { println("Task 2 for data 2 should be 436 and is ... $it") }
    dataForAdvent12.data3.getGroups().sumOf { it.price2 }
        .also { println("Task 2 for data 3 should be 1206 and is ... $it") }
    dataForAdvent12.data4.getGroups().sumOf { it.price2 }
        .also { println("Task 2 for data 4 should be 236 and is ... $it") }
    dataForAdvent12.data5.getGroups().sumOf { it.price2 }
        .also { println("Task 2 for data 5 should be 368 and is ... $it") }
    dataForAdvent12.data6.getGroups().sumOf { it.price2 }
        .also { println("Task 1 for data 6 should be 1450816 and is ... $it") }
}

private fun String.getGroups(): List<Group> =
    lines()
        .flatMapIndexed { i: Int, line: String ->
            line.mapIndexed { j: Int, c: Char ->
                Pair(c, Pair(i, j))
            }
        }
        .groupBy { it.first }
        .map { Group(it.key, it.value.map { p -> p.second }) }
        .flatMap { before ->
//            println("before split $it")
            before.splitUnconnected()
//                .onEach { println("after split $it") }
//                .onEach { println("edges ${it.countEdges()}") }
        }


private data class Group(
    val letter: Char,
    val coordinates: List<Pair<Int, Int>>,
    val edges: MutableList<Edge> = mutableListOf()
) {
    private val area get() = coordinates.size
    private val perimeter get() = coordinates.firstOrNull()?.let { coordinates.getPerimeterOf(it, mutableListOf()) } ?: 0

    val price1: Int get() {
//        println("computing price of $letter with area $area and perimeter $perimeter")
        return area * perimeter
    }

    val price2: Int get() {
        return area * countEdges()
    }

    fun countEdges(): Int {
        val leftEdges = edges.filter { it.leftOrTop && it.orientationVertical }.groupBy { it.coordinate.second }
        val rightEdges = edges.filter { !it.leftOrTop && it.orientationVertical }.groupBy { it.coordinate.second }
        val topEdges = edges.filter { it.leftOrTop && !it.orientationVertical }.groupBy { it.coordinate.first }
        val bottomEdges = edges.filter { !it.leftOrTop && !it.orientationVertical }.groupBy { it.coordinate.first }
        return leftEdges.countSubGroups() + rightEdges.countSubGroups() + topEdges.countSubGroups() + bottomEdges.countSubGroups()
    }

    fun splitUnconnected(): List<Group> {
        val result = mutableListOf<Group>()
        val remaining = coordinates.toMutableList()
        while (remaining.isNotEmpty()) {
            val subgroup = remaining.collectWithNeighbours(remaining.first(), mutableListOf(), edges)
            result.add(Group(letter, subgroup, edges.toList().toMutableList()))
            edges.clear()
            remaining.removeAll(subgroup)
        }
        return result
    }
}

private fun Map<Int,List<Edge>>.countSubGroups(): Int = values.sumOf { it.countSubGroups() }

private fun List<Edge>.countSubGroups(): Int {
    val remaining = map { it.coordinate }.toMutableList()
    var count = 0
    while (remaining.isNotEmpty()) {
        val subgroup = remaining.collectWithNeighbours(remaining.first(), mutableListOf(), mutableListOf())
        count++
        remaining.removeAll(subgroup.flatMap { edge -> listOfNotNull(remaining.find { it == edge }) })
    }
    return count
}

private data class Edge(
    val orientationVertical: Boolean,
    val leftOrTop: Boolean,
    val coordinate: Pair<Int, Int>
)

private fun List<Pair<Int,Int>>.collectWithNeighbours(
    point: Pair<Int, Int>,
    alreadyTraversed: MutableList<Pair<Int, Int>>,
    edges: MutableList<Edge>
): List<Pair<Int, Int>> {
    val newList = this.toMutableList().apply { remove(point) }
    alreadyTraversed.add(point)
    val resultList = listOf(
        Pair(point.first, point.second + 1),
        Pair(point.first, point.second - 1),
        Pair(point.first + 1, point.second),
        Pair(point.first - 1, point.second)
    ).flatMapIndexed { i: Int, coord: Pair<Int, Int> ->
        when (coord) {
            in alreadyTraversed -> emptyList()
            in newList -> newList.collectWithNeighbours(coord, alreadyTraversed, edges)
            else -> {
                when (i) {
                    0 -> edges.add(Edge(true, false, point))
                    1 -> edges.add(Edge(true, true, point))
                    2 -> edges.add(Edge(false, false, point))
                    3 -> edges.add(Edge(false, true, point))
                }
                emptyList()
            }
        }
    }
    return resultList + point
}

private fun List<Pair<Int,Int>>.getPerimeterOf(
    point: Pair<Int, Int>,
    alreadyTraversed: MutableList<Pair<Int,Int>>
): Int {
    val newList = this.toMutableList().apply { remove(point) }
    alreadyTraversed.add(point)
    return listOf(
        Pair(point.first, point.second+1),
        Pair(point.first, point.second-1),
        Pair(point.first+1, point.second),
        Pair(point.first-1, point.second)
    ).sumOf {
        when (it) {
            in alreadyTraversed -> 0
            in newList -> newList.getPerimeterOf(it, alreadyTraversed)
            else -> 1
        }
    }
}

package y2024

fun main() {
    println("advent 6")
    
    dataForAdvent6.data1.lines().countVisitedNodes()
        .also { println("Task 1 for data 1 should be 41 and is ... $it") }
    dataForAdvent6.data2.lines().countVisitedNodes()
        .also { println("Task 1 for data 2 should be 4964 and is ... $it") }

    dataForAdvent6.data1.lines().countLoopingNodes()
        .also { println("Task 2 for data 1 should be 6 and is ... $it") }
    dataForAdvent6.data2.lines().countLoopingNodes()
        .also { println("Task 2 for data 2 should be 1740 and is ... $it") }
}

private fun List<String>.countLoopingNodes(): Int {
    val loopingNodes = mutableListOf<Pair<Int, Int>>()
    forEachIndexed { i, line ->
        line.forEachIndexed { j, c ->
            if (c == '.') {
                if (checkWhetherTheNodeLoops(Pair(i, j))) loopingNodes.add(Pair(i, j))
            }
        }
    }
    return loopingNodes.size
}

private fun List<String>.checkWhetherTheNodeLoops(potentialLoopNode: Pair<Int,Int>): Boolean =
    getVisitedNodes(potentialLoopNode).contains(null)

private fun List<String>.countVisitedNodes() = getVisitedNodes().count { it != null }

private fun List<String>.getVisitedNodes(
    addedNode: Pair<Int,Int>? = null
): MutableSet<Pair<Int,Int>?> {
    val start = findStart()
    var step = Pair(-1,0)
    var current = start
    val visitedNodes = mutableSetOf<Pair<Int,Int>?>()
    val visitedNodePairs = mutableSetOf<Pair<Pair<Int,Int>,Pair<Int,Int>>>()
    while (true) {
        visitedNodes.add(current)
        val next = current + step
        when {
            next.isInGrid(this).not() -> break
            visitedNodePairs.contains(Pair(current, next)) -> {
                visitedNodes.add(null)
                break
            }
            this[next] == '#' -> step = step.turnRight()
            next == addedNode -> step = step.turnRight()
            else -> {
                visitedNodePairs.add(Pair(current, next))
                current = next
            }
        }
    }
    return visitedNodes
}

private fun List<String>.findStart(): Pair<Int,Int> {
    forEachIndexed { i, line ->
        line.forEachIndexed { j, c ->
            if (c == '^') return Pair(i, j)
        }
    }
    return Pair(-1, -1)
}

private fun Pair<Int,Int>.turnRight() = Pair(this.second, -this.first)

private operator fun List<String>.get(point: Pair<Int, Int>): Char = this[point.first][point.second]

private operator fun Pair<Int,Int>.plus(step: Pair<Int, Int>) = Pair(first + step.first, second + step.second)

private operator fun Pair<Int,Int>.minus(step: Pair<Int, Int>) = Pair(first - step.first, second - step.second)

private fun Pair<Int,Int>.isInGrid(grid: List<String>) = when {
    first < 0 -> false
    second < 0 -> false
    first >= grid.size -> false
    second >= grid[0].length -> false
    else -> true
}
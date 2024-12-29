package y2024

fun main() {
    println("advent 16")

    dataForAdvent16.data1.let { s -> Maze(s.lines().map { it.toList() }) }
        .also { println(it) }
        .also { println("Task 1 for data 1 should be 2028 and is ... ") }
}

private class Maze(val map: List<List<Char>>) {
    lateinit var start: Pair<Int, Int>
    lateinit var end: Pair<Int, Int>

    init {
        map.forEachIndexed { i, row ->
            row.forEachIndexed { j, c ->
                when(c) {
                    'S' -> start = Pair(i, j)
                    'E' -> end = Pair(i, j)
                }
            }
        }
    }

    override fun toString(): String {
        return map.joinToString("\n") { line -> line.joinToString(" ") { it.toString() } }
    }
}

private class Node(point: Pair<Int, Int>, direction: Direction) {
    fun getNeighbours(): List<Node> {
        
    }
}

private class Step(val nodeA: Node, val nodeB: Node, val distance: Int) {

}

private enum class Direction {
    UP, DOWN, LEFT, RIGHT
}


package y2024

import java.util.LinkedList
import java.util.PriorityQueue
import java.util.Queue

fun main() {
    println("advent 16")

//    dataForAdvent16.data0.let { s -> Maze(s.lines().map { it.toMutableList() }) }
//        .also { println("Task 1 for data 0 should be 2008 and is ... ${it.getMinMazeRunner()?.countPath()}") }

    dataForAdvent16.data0.let { s -> Maze2.from(s) }
        .also { println("dijsktra 0 should be 2008 and is: ${it.dijsktra()}") }
        .also { println("dijsktra 0 sum: ${it.sumGraph2()}") }

    dataForAdvent16.data0a.let { s -> Maze2.from(s) }
        .also { println("dijsktra 0a should be 21148 and is: ${it.dijsktra()}") }
        .also { println("dijsktra 0a sum: ${it.sumGraph()}") }
        .also { println("dijsktra 0a sum: ${it.sumGraph2()}") }

    dataForAdvent16.data0b.let { s -> Maze2.from(s) }
        .also { println("dijsktra 0b should be 4013 and is: ${it.dijsktra()}") }
        .also { println("dijsktra 0b sum: ${it.sumGraph2()}") }

    dataForAdvent16.data1.let { s -> Maze2.from(s)}
        .also { println("dijkstra 1 should be 7036 and is: ${it.dijsktra()}") }
        .also { println("dijsktra 1 sum: ${it.sumGraph()}") }
        .also { println("dijsktra 1 sum: ${it.sumGraph2()}") }

    dataForAdvent16.data2.let { s -> Maze2.from(s)}
        .also { println("dijkstra 2 should be 11048 and is: ${it.dijsktra()}") }
        .also { println("dijsktra 2 sum: ${it.sumGraph()}") }
        .also { println("dijsktra 2 sum: ${it.sumGraph2()}") }

    dataForAdvent16.data3.let { s -> Maze2.from(s)}
        .also { println("dijkstra 3 should be 95444 and is:${it.dijsktra()}") }
        .also { println("dijsktra 3 sum: ${it.sumGraph()}") }
        .also { println("dijsktra 3 sum: ${it.sumGraph2()}") } //454,455,456
        .also { it.printAll() }
}

private typealias Point = Pair<Int, Int>

private enum class ORIENTATION { HORIZONTAL, VERTICAL }

private data class Node(val point: Point, val orientation: ORIENTATION)

private class Maze2 private constructor(
    val start: Point,
    val end: Point,
    val map: List<MutableList<Char>>,
    val graph: MutableMap<Node, List<Path>>
) {
    companion object {
        fun from(s: String): Maze2 {
            val map: List<MutableList<Char>> = s.lines().map { it.toMutableList() }
            lateinit var start: Point
            lateinit var end: Point
            map.forEachIndexed { i, row ->
                row.forEachIndexed { j, c ->
                    when(c) {
                        'S' -> start = Point(i, j)
                        'E' -> end = Point(i, j)
                    }
                }
            }
            while(true) {
                val deadEnds = mutableListOf<Point>()
                map.forEachIndexed { i, row ->
                    row.forEachIndexed { j, c ->
                        when(c) {
                            '.' -> {
                                if (map.getNeighbourPoints(Point(i,j)).size == 1) {
                                    deadEnds.add(Point(i,j))
                                }
                            }
                        }
                    }
                }
                deadEnds.forEach { map[it.first][it.second] = '#' }
                if(deadEnds.isEmpty()) break
            }
            return Maze2(start, end, map, mutableMapOf())
        }
    }

    fun getNextNodes(
        aStart: Node,
        visitedNodes: MutableSet<Node>,
        sumOfEdges: Int,
        returnOnJunction: Boolean = false
    ): List<Path> {
        visitedNodes.add(aStart)
        return listOf (
            Pair(
                Node(Point(aStart.point.first+1,aStart.point.second), ORIENTATION.VERTICAL),
                if (aStart.orientation == ORIENTATION.HORIZONTAL) 1001 else 1
            ),
            Pair(
                Node(Point(aStart.point.first,aStart.point.second+1), ORIENTATION.HORIZONTAL),
                if (aStart.orientation == ORIENTATION.VERTICAL) 1001 else 1
            ),
            Pair(
                Node(Point(aStart.point.first-1,aStart.point.second), ORIENTATION.VERTICAL),
                if (aStart.orientation == ORIENTATION.HORIZONTAL) 1001 else 1
            ),
            Pair(
                Node(Point(aStart.point.first,aStart.point.second-1), ORIENTATION.HORIZONTAL),
                if (aStart.orientation == ORIENTATION.VERTICAL) 1001 else 1
            )
        )
            .filterNot { map[it.first.point.first][it.first.point.second] == '#' }
            .filterNot { visitedNodes.any { vp -> vp.point == it.first.point } }
            .filterNot { it.first.point == start }
            .let {
                when {
                    aStart.point == end -> listOf(Path(aStart, sumOfEdges, visitedNodes))
                    it.size == 1 -> getNextNodes(
                        it.first().first,
                        visitedNodes,
                        sumOfEdges + it.first().second,
                        true)
                    it.isEmpty() -> emptyList()
                    returnOnJunction -> listOf(Path(aStart, sumOfEdges, visitedNodes))
                    else -> it.flatMap { it2 -> getNextNodes(
                        it2.first,
                        visitedNodes.toMutableSet(),
                        sumOfEdges + it2.second,
                        true) }
                }
            }
    }

    var distances = mutableMapOf<Node, Int>().withDefault { Int.MAX_VALUE }
    var countOfPaths = mutableMapOf<Node, Int>().withDefault { 0 }

    fun dijsktra(): List<Int?> {
        distances = mutableMapOf<Node, Int>().withDefault { Int.MAX_VALUE }
        countOfPaths = mutableMapOf<Node, Int>().withDefault { 0 }
        val priorityQueue = PriorityQueue<Path>(compareBy { it.distance })

        val startNode = Node(start, ORIENTATION.HORIZONTAL)
        priorityQueue.add(Path(startNode, 0, mutableSetOf(startNode)))
        distances[startNode] = 0

        while (priorityQueue.isNotEmpty()) {
            val path = priorityQueue.poll()
            val currentNode = path.node
            val currentDistance = path.distance
            val visited = path.visitedNodes
            if (visited.contains(currentNode) && visited.last() != currentNode) continue
//            visited.add(currentNode)

            val nextNodes = getNextNodes(currentNode, visited, currentDistance)
            graph[currentNode] = nextNodes.toMutableList()
            nextNodes.forEach { nextPath ->
                val nextNode = nextPath.node
                val edgeWeight = nextPath.distance
                val edgeVisited = nextPath.visitedNodes
                val newDistance = edgeWeight
                if (newDistance == (distances[nextNode] ?: Int.MAX_VALUE)) {
                    countOfPaths[nextNode] = (countOfPaths[nextNode] ?: 0) + 1
                } else if (newDistance < (distances[nextNode] ?: Int.MAX_VALUE)) {
                    countOfPaths[nextNode] = 1
                    distances[nextNode] = newDistance
                    priorityQueue.add(Path(nextNode, newDistance, edgeVisited))
                }
            }
        }

        return listOf(distances[Node(end, ORIENTATION.HORIZONTAL)], distances[Node(end,ORIENTATION.VERTICAL)])
    }

    fun sumGraph(toNode: Node = Node(end, ORIENTATION.HORIZONTAL)): Int = sumGraphPoints(toNode).size

    fun sumGraphPoints(toNode: Node): Set<Point> {
        val remainingNodes: Queue<Node> = LinkedList<Node>().apply { add(toNode) }
        val allVisitedPoints = mutableSetOf<Point>()
        while (remainingNodes.isNotEmpty()) {
            val rNode = remainingNodes.poll()
            val edges = mutableListOf<Pair<Node, Path>>()
            graph.forEach {
                val ed = it.value.find { edge -> edge.node == rNode && edge.distance == distances[edge.node] }
                if (it.key != rNode && ed != null) {
                    edges.add(it.key to ed)
                    remainingNodes.add(it.key)
                }
            }
            edges.forEach { (_, path) ->
                allVisitedPoints.addAll(path.visitedNodes.map { it.point })
            }
        }
        return allVisitedPoints
    }

    fun sumGraph2(): Int = sumGraph(Node(end, ORIENTATION.VERTICAL))

    fun printAll() {
        val allVisited = sumGraphPoints(Node(end, ORIENTATION.VERTICAL))
        map.forEachIndexed { i, chars ->
            chars.mapIndexed { j, c -> if (allVisited.contains(Point(i,j))) 'o' else c }
                .joinToString("")
                .also { println(it) }
        }
    }

    data class Path(
        val node: Node,
        val distance: Int,
        val visitedNodes: MutableSet<Node>
    )
}

private fun List<MutableList<Char>>.getNeighbourPoints(aStart: Point): List<Point> =
    listOf (Point(1,0), Point(0,1), Point(-1,0), Point(0,-1))
        .map { Point(aStart.first + it.first, aStart.second + it.second) }
        .filterNot { this[it.first][it.second] == '#' }

private class Maze(val map: List<MutableList<Char>>) {
    lateinit var start: Point
    lateinit var end: Point
    var mazeRunners: MutableList<MazeRunner>

    init {
        map.forEachIndexed { i, row ->
            row.forEachIndexed { j, c ->
                when(c) {
                    'S' -> start = Point(i, j)
                    'E' -> end = Point(i, j)
                    '.' -> {
                        if (getNeighbourPoints(Point(i,j)).size == 1) {
                            map[i][j] = '#'
                        }
                    }
                }
            }
        }
        while(true) {
            val deadEnds = mutableListOf<Point>()
            map.forEachIndexed { i, row ->
                row.forEachIndexed { j, c ->
                    when(c) {
                        'S' -> start = Point(i, j)
                        'E' -> end = Point(i, j)
                        '.' -> {
                            if (getNeighbourPoints(Point(i,j)).size == 1) {
                                deadEnds.add(Point(i,j))
                            }
                        }
                    }
                }
            }
            deadEnds.forEach { map[it.first][it.second] = '#' }
            if(deadEnds.isEmpty()) break
        }
        mazeRunners = mutableListOf(MazeRunner(start, Direction.RIGHT))
    }

    override fun toString(): String {
        return map.joinToString("\n") { line -> line.joinToString(" ") { it.toString() } }
    }

    fun getNeighbourPoints(aStart: Point = start): List<Point> {
        return listOf (Point(1,0), Point(0,1), Point(-1,0), Point(0,-1))
            .map { Point(aStart.first + it.first, aStart.second + it.second) }
            .filterNot { map[it.first][it.second] == '#' }
    }

    fun getNextNodePoints(aStart: Point = start, visitedPoints: MutableList<Point> = mutableListOf()): List<Point> {
        visitedPoints.add(aStart)
        return getNeighbourPoints(aStart)
            .filterNot { visitedPoints.contains(it) }
            .let {
                when {
                    map[aStart.first][aStart.second] == 'E' -> listOf(aStart)
                    it.size == 1 -> getNextNodePoints(it.first(), visitedPoints)
                    it.isEmpty() -> emptyList()
                    visitedPoints.size == 1 -> it.flatMap { it2 -> getNextNodePoints(it2, visitedPoints) }
                    else -> listOf(aStart)
                }
            }
    }

    fun getValidNeighbourPoints(aStart: Point = start): List<Point> {
        return getNeighbourPoints(aStart)
            .filterNot { getNextNodePoints(it, mutableListOf(aStart)).isEmpty() }
    }

    fun getAllMazeRunners(): List<MazeRunner> {
        val finishedMazeRunners = mutableListOf<MazeRunner>()
        while(mazeRunners.isNotEmpty()) {
            val new = mazeRunners.flatMap { mazeRunner->
                if (mazeRunner.currentPoint == end) {
//                    println(mazeRunner)
                    finishedMazeRunners.add(mazeRunner)
                    emptyList()
                }
                else {
                    getNeighbourPoints(mazeRunner.currentPoint)
                        .filterNot { mazeRunner.steps.contains(it) }
                        .let { mazeRunner.splitToPoints(it) }
                }
            }
            mazeRunners = new.toMutableList()
        }
        return finishedMazeRunners
    }

    fun getMinMazeRunner(): MazeRunner? {
        var minMazeRunner: MazeRunner? = null
        var minMazeRunnerCount: Int? = null
        while(mazeRunners.isNotEmpty()) {
//            println(mazeRunners.size)
            val first = mazeRunners.first()
//            println(displayMazeRunner(first))
            if (first.currentPoint == end) {
                println(first)
                val newCount = first.countPath()
                if (minMazeRunnerCount == null || newCount < minMazeRunnerCount) {
                    minMazeRunner = first
                    minMazeRunnerCount = newCount
                }
                mazeRunners.removeFirst()
                continue
            }
            mazeRunners.removeFirst()
            getValidNeighbourPoints(first.currentPoint)
                .filterNot { first.steps.contains(it) }
                .let { first.splitToPoints(it) }
                .also { mazeRunners.addAll(0, it) }
        }
        return minMazeRunner
    }

    fun displayMazeRunner(mr: MazeRunner): String = StringBuilder().apply {
        map.forEachIndexed { i, line ->
            line.forEachIndexed { j, c ->
                if (mr.steps.contains(Point(i,j))) append('o')
                else append(c)
            }
            append("\n")
        }
    }.toString()
}

private data class MazeRunner(
    var currentPoint: Point,
    val initialDirection: Direction,
    val steps: MutableList<Point> = mutableListOf()
) {
    private fun cloneAndMakeStepTo(nextPoint: Point) = MazeRunner (
        nextPoint,
        initialDirection,
        steps.toMutableList().apply { add(currentPoint) }
    )

    fun splitToPoints(nextPoints: List<Point>): List<MazeRunner> {
        val ret = mutableListOf<MazeRunner>()
        nextPoints.forEachIndexed { i, point ->
            ret.add(cloneAndMakeStepTo(point))
        }
        return ret
    }

    override fun toString(): String = "MR: ${countPath()} = $steps"

    fun countPath(): Int {
        var sum = 0
        var dir = initialDirection
        var last = steps.first()
        steps.forEachIndexed { i, pair ->
            when {
                i == 0 -> {}
                pair.first == last.first && dir.isHor() -> {
                    sum += 1
                }
                pair.first == last.first -> {
                    sum += 1001
                    dir = Direction.RIGHT
                }
                dir.isHor() -> {
                    sum += 1001
                    dir = Direction.UP
                }
                else -> {
                    sum += 1
                }
            }
            last = pair
        }
        when {
            currentPoint.first == last.first && dir.isHor() -> sum += 1
            currentPoint.first == last.first -> sum += 1001
            dir.isHor() -> sum += 1001
            else -> sum += 1
        }
        return sum
    }
}

private fun Direction.isHor() = this == Direction.RIGHT || this == Direction.LEFT

private enum class Direction {
    UP, DOWN, LEFT, RIGHT
}


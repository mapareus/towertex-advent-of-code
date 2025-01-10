package y2024

fun main() {
    println("advent 16")

//    dataForAdvent16.data0.let { s -> Maze(s.lines().map { it.toMutableList() }) }
//        .also { println(it) }
//        .let { it.getAllMazeRunners().minBy { mr -> mr.countPath() } }
//        .also { println(it) }
//        .also { println("Task 1 for data 0 should be 2008 and is ... ${it.countPath()}") }
//
//    dataForAdvent16.data0.let { s -> Maze(s.lines().map { it.toMutableList() }) }
//        .also { println("Task 1 for data 0 should be 2008 and is ... ${it.getMinMazeRunner()?.countPath()}") }
//
//    dataForAdvent16.data1.let { s -> Maze(s.lines().map { it.toMutableList() }) }
//        .also { println("Task 1 for data 1 should be 7036 and is ... ${it.getMinMazeRunner()?.countPath()}") }
//
//    dataForAdvent16.data2.let { s -> Maze(s.lines().map { it.toMutableList() }) }
//        .also { println("Task 1 for data 2 should be 11048 and is ... ${it.getMinMazeRunner()?.countPath()}") }

    dataForAdvent16.data3.let { s -> Maze(s.lines().map { it.toMutableList() }) }
        .also { println("Task 1 for data 3 should be 11048 and is ... ${it.getMinMazeRunner()?.countPath()}") }
}

typealias Point = Pair<Int, Int>

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


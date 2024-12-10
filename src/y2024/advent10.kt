package y2024

fun main() {
    println("advent 10")

    dataForAdvent10.data1.sumTrailHeads()
        .also { println("Task 1 for data 1 should be 1 and is ... $it") }
    dataForAdvent10.data2.sumTrailHeads()
        .also { println("Task 1 for data 2 should be 36 and is ... $it") }
    dataForAdvent10.data3.sumTrailHeads()
        .also { println("Task 1 for data 3 should be 667 and is ... $it") }

    dataForAdvent10.data1.sumTrails()
        .also { println("Task 2 for data 1 should be 16 and is ... $it") }
    dataForAdvent10.data2.sumTrails()
        .also { println("Task 2 for data 2 should be 81 and is ... $it") }
    dataForAdvent10.data3.sumTrails()
        .also { println("Task 2 for data 3 should be 1344 and is ... $it") }
}

private fun String.sumTrailHeads() = getPaths()
    .groupBy { Pair(it.first(), it.last()) }
    .size

private fun String.sumTrails() = getPaths()
    .size

private fun String.getPaths(): List<List<Pair<Int, Int>>> {
    val topomap: List<List<Int>> = lines().map { line -> line.map { it.digitToInt() } }
    return topomap.flatMapIndexed { i: Int, line: List<Int> ->
        line.flatMapIndexed inner@{ j: Int, s: Int ->
            if (s != 0) return@inner emptyList()
            var paths = listOf(listOf(Pair(i, j)))
            var step = 1
            while (step <= 9) {
                paths = paths.flatMap { path ->
                    val (x0, y0) = path.last()
                    listOf(Pair(x0-1,y0), Pair(x0, y0-1), Pair(x0+1, y0), Pair(x0, y0+1))
                        .filter { (x, y) ->
                            x >= 0 && y >= 0 && x < topomap.size && y < line.size && topomap[x][y] == step }
                        .map { path + it }
                }
                step++
            }
            paths
        }
    }
}

package y2024

fun main() {
    println("advent 10")


}

private fun String.sumTrailHeads(): Long {
    val topomap: List<List<Int>> = lines().map { line -> line.map { it.digitToInt() } }
    return topomap.flatMapIndexed { i: Int, line: List<Int> ->
        line.mapIndexed { j: Int, s: Int ->
            if (s != 0) return@mapIndexed 0
            0
        }
    }.fold(0L) { sum, value -> sum + value }
}
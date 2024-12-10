package y2024

import kotlin.math.abs

fun main() {
    println("advent 8")

    dataForAdvent8.data1.countTargetNodes()
        .also { println("Task 1 for data 1 should be 14 and is ... $it") }
    dataForAdvent8.data2.countTargetNodes()
        .also { println("Task 1 for data 2 should be 244 and is ... $it") }

    dataForAdvent8.data1.getAllTargetNodes().count()
        .also { println("Task 2 for data 1 should be 34 and is ... $it") }
    dataForAdvent8.data2.getAllTargetNodes().count()
        .also { println("Task 2 for data 2 should be 912 and is ... $it") }
}

private fun String.getAllTargetNodes(): List<Pair<Int,Int>> {
    val targetNodes = mutableSetOf<Pair<Int, Int>>()
    val sourceNodes = mapSourceNodes()
    val maxX = this.lines().size
    val maxY = this.lines().first().length
    sourceNodes.forEach { (c, pairs) ->
        pairs.forEachIndexed { i, firstNode ->
            for (j in i+1 until pairs.size) {
                val secondNode = pairs[j]
                val dx = abs(firstNode.first - secondNode.first)
                val dy = abs(firstNode.second - secondNode.second)
                var ax = firstNode.first
                var ay = firstNode.second
                var bx = secondNode.first
                var by = secondNode.second
                do {
                    if (ax>=0 && ay>=0 && ax<maxX && ay<maxY) targetNodes.add(Pair(ax, ay))
                    if (bx>=0 && by>=0 && bx<maxX && by<maxY) targetNodes.add(Pair(bx, by))
                    when {
                        firstNode.first <= secondNode.first && firstNode.second <= secondNode.second -> {
                            ax -= dx
                            ay -= dy
                            bx += dx
                            by += dy
                        }
                        firstNode.first <= secondNode.first && firstNode.second > secondNode.second -> {
                            ax -= dx
                            ay += dy
                            bx += dx
                            by -= dy
                        }
                        firstNode.first > secondNode.first && firstNode.second <= secondNode.second -> {
                            ax += dx
                            ay -= dy
                            bx -= dx
                            by += dy
                        }
                        firstNode.first > secondNode.first && firstNode.second > secondNode.second -> {
                            ax += dx
                            ay += dy
                            bx -= dx
                            by -= dy
                        }
                    }
                } while ((ax>=0 && ay>=0 && ax<maxX && ay<maxY) || (bx>=0 && by>=0 && bx<maxX && by<maxY))
            }
        }
    }
    return targetNodes.toList()
}

private fun String.countTargetNodes(): Int = getTargetNodes().count()

private fun String.getTargetNodes(): List<Pair<Int,Int>> {
    val targetNodes = mutableSetOf<Pair<Int, Int>>()
    val sourceNodes = mapSourceNodes()
    sourceNodes.forEach { (c, pairs) ->
        pairs.forEachIndexed { i, firstNode ->
            for (j in i+1 until pairs.size) {
                val secondNode = pairs[j]
                val dx = abs(firstNode.first - secondNode.first)
                val dy = abs(firstNode.second - secondNode.second)
                var ax = -1
                var ay = -1
                var bx = -1
                var by = -1
                when {
                    firstNode.first <= secondNode.first && firstNode.second <= secondNode.second -> {
                        ax = firstNode.first - dx
                        ay = firstNode.second - dy
                        bx = secondNode.first + dx
                        by = secondNode.second + dy
                    }
                    firstNode.first <= secondNode.first && firstNode.second > secondNode.second -> {
                        ax = firstNode.first - dx
                        ay = firstNode.second + dy
                        bx = secondNode.first + dx
                        by = secondNode.second - dy
                    }
                    firstNode.first > secondNode.first && firstNode.second <= secondNode.second -> {
                        ax = firstNode.first + dx
                        ay = firstNode.second - dy
                        bx = secondNode.first - dx
                        by = secondNode.second + dy
                    }
                    firstNode.first > secondNode.first && firstNode.second > secondNode.second -> {
                        ax = firstNode.first + dx
                        ay = firstNode.second + dy
                        bx = secondNode.first - dx
                        by = secondNode.second - dy
                    }
                }
                if (ax >= 0 && ay >= 0 && ax < this.lines().size && ay < this.lines().first().length) {
                    targetNodes.add(Pair(ax, ay))
                }
                if (bx >= 0 && by >= 0 && bx < this.lines().size && by < this.lines().first().length) {
                    targetNodes.add(Pair(bx, by))
                }
            }
        }
    }
    return targetNodes.toList()
}

private fun String.mapSourceNodes(): Map<Char,List<Pair<Int,Int>>> {
    val sourceNodes = mutableMapOf<Char, MutableList<Pair<Int, Int>>>()
    lines().forEachIndexed { i, line ->
        line.forEachIndexed { j, c ->
            if (c != '.') {
                sourceNodes[c]?.add(Pair(i, j)) ?: sourceNodes.put(c, mutableListOf(Pair(i, j)))
            }
        }
    }
    return sourceNodes.filterNot { it.value.size < 2 }
}
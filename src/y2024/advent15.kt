package y2024

fun main() {
    println("advent 15")

    dataForAdvent15.data1.getMapAfterSteps(-1)
//        .also { println(it) }
        .also { println("Task 1 for data 1 should be 2028 and is ... ${it.toGPSSum()}") }

    dataForAdvent15.data2.getMapAfterSteps(-1)
        .also { println("Task 1 for data 2 should be 10092 and is ... ${it.toGPSSum()}") }

    dataForAdvent15.data3.getMapAfterSteps(-1)
        .also { println("Task 1 for data 3 should be 1568399 and is ... ${it.toGPSSum()}") }

    dataForAdvent15.data1.getExpandedMapAfterSteps(-1)
        .also { println("Task 2 for data 1 should be 1751 and is ... ${it.toGPSSum()}") }

    dataForAdvent15.data2.getExpandedMapAfterSteps(-1)
        .also { println("Task 2 for data 2 should be 9021 and is ... ${it.toGPSSum()}") }

    dataForAdvent15.data3.getExpandedMapAfterSteps(-1)
        .also { println("Task 2 for data 3 should be 1575877 and is ... ${it.toGPSSum()}") }
}

private fun String.getExpandedMapAfterSteps(max: Int): Warehouse {
    val (warehouse, steps) = toWarehouseAndSteps().let {
//        println(it.first)
        Pair(it.first.toExpandedWarehouse(), it.second)
    }
//    println(warehouse)
    return warehouse.getExpandedWarehouseAfterSteps(steps, max)
}

private fun String.getMapAfterSteps(max: Int): Warehouse {
    val (warehouse, steps) = toWarehouseAndSteps()
//    println(warehouse)
    return warehouse.getWarehouseAfterSteps(steps, max)
}

private fun String.toWarehouseAndSteps(): Pair<Warehouse, List<Char>> {
    val warehouse = substringBefore("\n\n").toWarehouse()
    val steps = substringAfter("\n\n").lines().flatMap { it.toList() }
    return Pair(warehouse, steps)
}

private fun String.toWarehouse(): Warehouse {
    val map = lines().map { it.toMutableList() }
    val position = map.findPos() ?: throw IllegalArgumentException("No position found")
    map[position.first][position.second] = '.'
    return Warehouse(map, position)
}

private class Warehouse(
    val map: List<List<Char>>,
    val position: Pair<Int, Int>
) {
    override fun toString(): String {
        val strb = StringBuilder()
        map.forEachIndexed { i, line ->
            if (i!=0) strb.append("\n")
            line.forEachIndexed { j, c ->
                if (i == position.first && j == position.second) strb.append('@')
                else strb.append(c)
            }
        }
        return strb.toString()
    }

    fun toGPSSum(): Long {
        var sum: Long = 0
        map.forEachIndexed { i, line ->
            line.forEachIndexed { j, c ->
                if (c == 'O' || c == '[') {
                    val toAdd = (100*i) + j
//                    println(toAdd)
                    sum += toAdd
                }
            }
        }
        return sum
    }

    fun toExpandedWarehouse(): Warehouse {
        val newMap = mutableListOf<List<Char>>()
        map.forEach { line ->
            newMap.add(line.flatMap { when (it) {
                '#' -> listOf('#','#')
                'O' -> listOf('[',']')
                else -> listOf('.','.')
            }})
        }
        return Warehouse(newMap, Pair(position.first, (position.second * 2) + (position.second % 2)))
    }

    fun getExpandedWarehouseAfterSteps(steps: List<Char>, maxSteps: Int): Warehouse {
        val newMap = map.toMutableList().map { it.toMutableList() }
        var currentPos = position
        var stepsCounter = 1
        steps.forEach { step ->
            if (stepsCounter > maxSteps && maxSteps > 0) return Warehouse(newMap, currentPos)
            when (step) {
                '^' -> {
                    if (newMap.tryToMoveUp(currentPos)) currentPos = Pair(currentPos.first - 1, currentPos.second)
                }
                '>' -> {
                    if (newMap.tryToMoveRight(currentPos)) currentPos = Pair(currentPos.first, currentPos.second + 1)
                }
                'v' -> {
                    if (newMap.tryToMoveDown(currentPos)) currentPos = Pair(currentPos.first + 1, currentPos.second)
                }
                '<' -> {
                    if (newMap.tryToMoveLeft(currentPos)) currentPos = Pair(currentPos.first, currentPos.second - 1)
                }
            }
//            println(step)
//            println(Warehouse(newMap, currentPos))
            stepsCounter++
        }
        return Warehouse(newMap, currentPos)
    }

    fun getWarehouseAfterSteps(steps: List<Char>, maxSteps: Int): Warehouse {
        val newMap = map.toMutableList().map { it.toMutableList() }
        var currentPos = position
        var stepsCounter = 1
        steps.forEach { step ->
            if (stepsCounter > maxSteps && maxSteps > 0) return Warehouse(newMap, currentPos)
            when (step) {
                '^' -> {
                    val nextPos = newMap.nextFreePos(currentPos, -1, 0)
                    if (nextPos != null) {
                        if (nextPos.first == currentPos.first - 1) {
                            currentPos = nextPos
                        } else {
                            newMap[nextPos.first][nextPos.second] = 'O'
                            currentPos = Pair(currentPos.first - 1, currentPos.second)
                            newMap[currentPos.first][currentPos.second] = '.'
                        }
                    }
                }
                '>' -> {
                    val nextPos = newMap.nextFreePos(currentPos, 0, 1)
                    if (nextPos != null) {
                        if (nextPos.second == currentPos.second + 1) {
                            currentPos = nextPos
                        } else {
                            newMap[nextPos.first][nextPos.second] = 'O'
                            currentPos = Pair(currentPos.first, currentPos.second + 1)
                            newMap[currentPos.first][currentPos.second] = '.'
                        }
                    }
                }
                'v' -> {
                    val nextPos = newMap.nextFreePos(currentPos, +1, 0)
                    if (nextPos != null) {
                        if (nextPos.first == currentPos.first + 1) {
                            currentPos = nextPos
                        } else {
                            newMap[nextPos.first][nextPos.second] = 'O'
                            currentPos = Pair(currentPos.first + 1, currentPos.second)
                            newMap[currentPos.first][currentPos.second] = '.'
                        }
                    }
                }
                '<' -> {
                    val nextPos = newMap.nextFreePos(currentPos, 0, -1)
                    if (nextPos != null) {
                        if (nextPos.second == currentPos.second - 1) {
                            currentPos = nextPos
                        } else {
                            newMap[nextPos.first][nextPos.second] = 'O'
                            currentPos = Pair(currentPos.first, currentPos.second - 1)
                            newMap[currentPos.first][currentPos.second] = '.'
                        }
                    }
                }
            }
//            println(step)
//            println(Warehouse(newMap, currentPos))
            stepsCounter++
        }
        return Warehouse(newMap, currentPos)
    }
}

private fun List<MutableList<Char>>.tryToMoveRight(currentPos: Pair<Int, Int>): Boolean {
    val currentValue = getOrNull(currentPos.first)?.getOrNull(currentPos.second) ?: return false
    val nextPos = Pair(currentPos.first, currentPos.second + 1)
    val nextValue = getOrNull(nextPos.first)?.getOrNull(nextPos.second) ?: return false
    val canMove = when (nextValue) {
        '.' -> true
        '#' -> false
        else -> tryToMoveRight(nextPos)
    }
    if (canMove) {
        this[nextPos.first][nextPos.second] = currentValue
        this[currentPos.first][currentPos.second] = '.'
        return true
    }
    return false
}

private fun List<MutableList<Char>>.tryToMoveLeft(currentPos: Pair<Int, Int>): Boolean {
    val currentValue = getOrNull(currentPos.first)?.getOrNull(currentPos.second) ?: return false
    val nextPos = Pair(currentPos.first, currentPos.second - 1)
    val nextValue = getOrNull(nextPos.first)?.getOrNull(nextPos.second) ?: return false
    val canMove = when (nextValue) {
        '.' -> true
        '#' -> false
        else -> tryToMoveLeft(nextPos)
    }
    if (canMove) {
        this[nextPos.first][nextPos.second] = currentValue
        this[currentPos.first][currentPos.second] = '.'
        return true
    }
    return false
}

private fun List<List<Char>>.getPointsToMoveDown(allPoints: MutableList<List<Pair<Int, Int>>>) {
    val lastPoints = allPoints.last()
    val nextSetOfPoints = lastPoints.flatMap { point ->
        val valueAtNextPoint = getOrNull(point.first + 1)?.getOrNull(point.second) ?: return@flatMap emptyList()
        when {
            valueAtNextPoint == '[' -> {
                listOf(Pair(point.first + 1, point.second), Pair(point.first + 1, point.second + 1))
            }
            valueAtNextPoint == ']' -> {
                listOf(Pair(point.first + 1, point.second), Pair(point.first + 1, point.second - 1))
            }
            valueAtNextPoint == '.' -> {
                emptyList()
            }
            else -> {
                allPoints.clear()
                return
            }
        }
    }
    if (nextSetOfPoints.isEmpty()) return
    allPoints.add(nextSetOfPoints.toSet().toList())
    getPointsToMoveDown(allPoints)
}

private fun List<MutableList<Char>>.tryToMoveDown(currentPos: Pair<Int, Int>): Boolean {
    val pointsToMoveDown = mutableListOf(listOf(currentPos))
    getPointsToMoveDown(pointsToMoveDown)
    val canMove = pointsToMoveDown.isNotEmpty()
    if (canMove.not()) return false
    pointsToMoveDown.reversed().forEach { points ->
        points.forEach { point ->
            this[point.first+1][point.second] = this[point.first][point.second]
            this[point.first][point.second] = '.'
        }
    }
    return true
}

private fun List<List<Char>>.getPointsToMoveUp(allPoints: MutableList<List<Pair<Int, Int>>>) {
    val lastPoints = allPoints.last()
    val nextSetOfPoints = lastPoints.flatMap { point ->
        val valueAtNextPoint = getOrNull(point.first - 1)?.getOrNull(point.second) ?: return@flatMap emptyList()
        when {
            valueAtNextPoint == '[' -> {
                listOf(Pair(point.first - 1, point.second), Pair(point.first - 1, point.second + 1))
            }
            valueAtNextPoint == ']' -> {
                listOf(Pair(point.first - 1, point.second), Pair(point.first - 1, point.second - 1))
            }
            valueAtNextPoint == '.' -> {
                emptyList()
            }
            else -> {
                allPoints.clear()
                return
            }
        }
    }
    if (nextSetOfPoints.isEmpty()) return
    allPoints.add(nextSetOfPoints.toSet().toList())
    getPointsToMoveUp(allPoints)
}

private fun List<MutableList<Char>>.tryToMoveUp(currentPos: Pair<Int, Int>): Boolean {
    val pointsToMoveUp = mutableListOf(listOf(currentPos))
    getPointsToMoveUp(pointsToMoveUp)
    val canMove = pointsToMoveUp.isNotEmpty()
    if (canMove.not()) return false
    pointsToMoveUp.reversed().forEach { points ->
        points.forEach { point ->
            this[point.first-1][point.second] = this[point.first][point.second]
            this[point.first][point.second] = '.'
        }
    }
    return true
}

private fun List<List<Char>>.nextFreePos(pos: Pair<Int, Int>, dx: Int, dy: Int): Pair<Int, Int>? {
    var nextPos = Pair(pos.first + dx, pos.second + dy)
    var nextValue = getOrNull(nextPos.first)?.getOrNull(nextPos.second) ?: return null
    while (nextValue != '.') {
        if (nextValue == '#') return null
        nextPos = Pair(nextPos.first + dx, nextPos.second + dy)
        nextValue = getOrNull(nextPos.first)?.getOrNull(nextPos.second) ?: return null
    }
    return nextPos
}

private fun List<List<Char>>.findPos(): Pair<Int, Int>? {
    forEachIndexed { x, line ->
        line.forEachIndexed { y, c ->
            if (c == '@') return Pair(x, y)
        }
    }
    return null
}
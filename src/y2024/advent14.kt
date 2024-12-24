package y2024

fun main() {
    println("advent 14")

    dataForAdvent14.data1.getRobots()
        .map { it.getRobotAfter(100, 11, 7) }
        .countSafetyIndex(11, 7)
        .also { println("Task 1 for data 1 should be 12 and is ... $it") }

    dataForAdvent14.data2.getRobots()
        .map { it.getRobotAfter(100, 101, 103) }
        .countSafetyIndex(101, 103)
        .also { println("Task 1 for data 2 should be 219150360 and is ... $it") }

//    dataForAdvent14.data1.getRobots()
//        .findStepsWithEqualSafetyIndex(11, 7)
//        .also { println("Task 2 for data 1 should be 1 and is ... $it") }

    dataForAdvent14.data2.getRobots()
        .findStepsWithEqualSafetyIndex(101, 103)
        .also { println("Task 2 for data 2 should be 12 and is ... $it") }
}

private fun List<Robot>.findStepsWithEqualSafetyIndex(roomX: Int, roomY: Int): Int? {
    var newRobots = this
    var step = 0
    val maxSteps = 100000
    while (step <= maxSteps) {
        if (newRobots.hasDuplicates().not()) {
            val printString: MutableList<String> = mutableListOf()
            for (i in 0 until roomX) {
                for (j in 0 until roomY) {
                    val numberOfRobots = newRobots.count { it.x == i && it.y == j }
                    if (numberOfRobots == 0) printString.add(".")
                    else printString.add(numberOfRobots.toString())
                    if (j == roomY - 1) printString.add("\n")
                }
            }
            println(printString.joinToString("") { it })
            println("\nstep: $step\n")
//            return step
        }
        newRobots = newRobots.map { it.getRobotAfter(1, roomX, roomY) }

        step++
    }
    return null
}

private fun List<Robot>.hasDuplicates(): Boolean {
    this.asSequence()
        .map { Pair(it.x, it.y) }
        .groupingBy { it }
        .aggregate { _, _: Int?, _ , first ->
            if (first) 1
            else return true
        }
    return false
}

private fun List<Robot>.getSafetyIndexes(roomX: Int, roomY: Int): List<List<Robot>> =
    groupBy {
        when {
            it.x<roomX/2 && it.y<roomY/2 -> 0
            it.x>roomX/2 && it.y<roomY/2 -> 1
            it.x<roomX/2 && it.y>roomY/2 -> 2
            it.x>roomX/2 && it.y>roomY/2 -> 3
            else -> 4
        }
    }
//        .onEach { println(it) }
        .toList()
        .filter { it.first != 4 }
        .map { it.second }

private fun List<Robot>.countSafetyIndex(roomX: Int, roomY: Int): Int =
    getSafetyIndexes(roomX, roomY).fold(1) { acc, robots -> acc * robots.size }

private fun String.getRobots(): List<Robot> {
    return lines()
        .map { Robot(
            it.substringAfter("p=").substringBefore(",").toInt(),
            it.substringAfter(",").substringBefore(" v=").toInt(),
            it.substringAfter(" v=").substringBefore(",").toInt(),
            it.substringAfter(",").substringAfter(",").toInt()
        ) }
}

private data class Robot(
    val x: Int,
    val y: Int,
    val vx: Int,
    val vy: Int
) {
    override fun toString(): String = "R$x,$y"

    fun getRobotAfter(time: Int, roomX: Int, roomY: Int): Robot {
        return Robot(
            ((x + (time * vx)) % roomX).let { if (it < 0) it + roomX else it },
            ((y + (time * vy)) % roomY).let { if (it < 0) it + roomY else it },
            vx,
            vy
        )
    }
}
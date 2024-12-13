package y2024

fun main() {
    println("advent 13")

    dataForAdvent13.data1.getClawMachines()
        .onEach { println("$it ... ${it.totalPrize}") }
        .sumOf { it.totalPrize }
        .also { println("Task 1 for data 1 should be 480 and is ... $it") }
    dataForAdvent13.data2.getClawMachines()
        .sumOf { it.totalPrize }
        .also { println("Task 1 for data 2 should be 39290 and is ... $it") }

    dataForAdvent13.data1.getClawMachines(10000000000000)
        .onEach { println("$it ... ${it.totalPrize}") }
        .sumOf { it.totalPrize }
        .also { println("Task 2 for data 1 should be 875318608908 and is ... $it") }
    dataForAdvent13.data2.getClawMachines(10000000000000)
        .sumOf { it.totalPrize }
        .also { println("Task 2 for data 2 should be 73458657399094 and is ... $it") }
}

private fun String.getClawMachines(factorPrize: Long = 0) = split("\n\n").map { it.getClawMachine(factorPrize) }

private fun String.getClawMachine(factorPrize: Long = 0): ClawMachine {
    val ALine = substringBefore("\n")
    val BLine = substringAfter("\n").substringBefore("\n")
    val PrizeLine = substringAfter("\n").substringAfter("\n")
    return ClawMachine(
        ALine.substringAfter ("Button A: X+").substringBefore(',').toLong(),
        ALine.substringAfter(", Y+").toLong(),
        BLine.substringAfter ("Button B: X+").substringBefore(',').toLong(),
        BLine.substringAfter(", Y+").toLong(),
        PrizeLine.substringAfter ("Prize: X=").substringBefore(',').toLong(),
        PrizeLine.substringAfter(", Y=").toLong(),
        factorPrize
    )
}

private data class ClawMachine(
    val aX: Long,
    val aY: Long,
    val bX: Long,
    val bY: Long,
    val prizeX: Long,
    val prizeY: Long,
    val factorPrize: Long = 0,
    val factorX: Long = 3L,
    val factorY: Long = 1L
) {
    val totalPrize: Long get() {
        val determinant: Long = aX * bY - aY * bX
        val prizeXInverted: Long = bY*(prizeX+factorPrize) - bX*(prizeY+factorPrize)
        val prizeYInverted: Long = aX*(prizeY+factorPrize) - aY*(prizeX+factorPrize)
        if (prizeXInverted % determinant != 0L || prizeYInverted % determinant != 0L) return 0
        return factorX*prizeXInverted/determinant + factorY*prizeYInverted/determinant
    }
}
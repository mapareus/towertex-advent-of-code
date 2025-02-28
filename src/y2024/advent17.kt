package y2024

import kotlin.math.pow

fun main() {
    println("advent 17")

    Computation.fromString(dataForAdvent17.data0a)
        .also { println("before: $it") }
        .also { it.execute() }
        .also { println("after: $it\n") }

    Computation.fromString(dataForAdvent17.data0b)
        .also { println("before: $it") }
        .also { it.execute() }
        .also { println("after: $it\n") }

    Computation.fromString(dataForAdvent17.data0c)
        .also { println("before: $it") }
        .also { it.execute() }
        .also { println("after: $it\n") }

    Computation.fromString(dataForAdvent17.data0d)
        .also { println("before: $it") }
        .also { it.execute() }
        .also { println("after: $it\n") }

    Computation.fromString(dataForAdvent17.data0e)
        .also { println("before: $it") }
        .also { it.execute() }
        .also { println("after: $it\n") }

    Computation.fromString(dataForAdvent17.data1)
        .also { println("before: $it") }
        .also { it.execute() }
        .also { println("after: $it\n") }

    Computation.fromString(dataForAdvent17.data2a)
        .also { println("before: $it") }
        .also { it.execute() }
        .also { println("after: $it\n") }

    Computation.fromString(dataForAdvent17.data2b)
        .also { println("before: $it") }
        .also { it.execute() }
        .also { println("after: $it\n") }
        .also { println("self computation: ${getSelfComputation(it.program)}\n") }

    Computation.fromString(dataForAdvent17.data3)
        .also { println("before: $it") }
        .also { it.execute() }
        .also { println("after: $it\n") }

    println("0o1 ... ${convertFromOctalToDecimal(listOf(1))}")
    println("0o30 ... ${convertFromOctalToDecimal(listOf(3, 0))}")
    println("0o340 ... ${convertFromOctalToDecimal(listOf(3, 4, 0))}")
    println("0o3450 ... ${convertFromOctalToDecimal(listOf(3, 4, 5, 0))}")
    println("0o34530 ... ${convertFromOctalToDecimal(listOf(3, 4, 5, 3, 0))}")
    println("0o345300 ... ${convertFromOctalToDecimal(listOf(3, 4, 5, 3, 0, 0))}")
    println("0o3550430615751420 ... ${convertFromOctalToDecimal(listOf(3, 5, 5, 0, 4, 3, 0, 6, 1, 5, 7, 5, 1, 4, 2, 0))}")
}

private fun convertFromOctalToDecimal(octal: List<Long>): Long {
    var decimal = 0L
    var i = 0
    var n: Long
    for (digit in octal.reversed()) {
        n = digit * 8.0.pow(i.toDouble()).toLong()
        decimal += n
        i++
    }
    return decimal
}

private fun getSelfComputation(program: List<Long>): Long {
    var output: List<Long> = listOf()
    var i = 0L
    lateinit var computation: Computation
    while (output != program && i < 1000000000) {
        if (i % 1000000 == 0L) println(i)
        computation = Computation(i, 0, 0, program)
        computation.execute()
        output = computation.output
        i++
    }
    i--
    return i
}

data class Computation(
    var registerA: Long,
    var registerB: Long,
    var registerC: Long,
    var program: List<Long>,
    val output: MutableList<Long> = mutableListOf()
) {
    var programPointer: Int = 0

    companion object {
        fun fromString(input: String): Computation {
            val lines = input.lines()
            val registers = lines.subList(0, 3).map { it.split(": ")[1].toLong() }
            val program = lines[4].substringAfter(": ").split(",").map { it.toLong() }
            return Computation(registers[0], registers[1], registers[2], program)
        }
    }

    private fun Long.combo() = when(this) {
        0L, 1L, 2L, 3L -> this
        4L -> registerA
        5L -> registerB
        6L -> registerC
        else -> throw IllegalArgumentException("operand $this")
    }

    private fun op0(operand: Long) {
        registerA = registerA / 2.toDouble().pow(operand.combo().toDouble()).toLong()
        programPointer += 2
    }

    private fun op1(operand: Long) {
        registerB = registerB.xor(operand)
        programPointer += 2
    }

    private fun op2(operand: Long) {
        registerB = operand.combo() % 8
        programPointer += 2
    }

    private fun op3(operand: Long) {
        if (registerA == 0L) {
            programPointer += 2
            return
        }
        programPointer = operand.toInt()
    }

    private fun op4(operand: Long) {
        registerB = registerB.xor(registerC)
        programPointer += 2
    }

    private fun op5(operand: Long) {
        output.add(operand.combo() % 8)
        programPointer += 2
    }

    private fun op6(operand: Long) {
        registerB = registerA / 2.toDouble().pow(operand.combo().toDouble()).toLong()
        programPointer += 2
    }

    private fun op7(operand: Long) {
        registerC = registerA / 2.toDouble().pow(operand.combo().toDouble()).toLong()
        programPointer += 2
    }

    fun execute() {
        while (programPointer < program.size) {
            val opcode = program[programPointer]
            val operand = program[programPointer + 1]
            when (opcode) {
                0L -> op0(operand)
                1L -> op1(operand)
                2L -> op2(operand)
                3L -> op3(operand)
                4L -> op4(operand)
                5L -> op5(operand)
                6L -> op6(operand)
                7L -> op7(operand)
                else -> throw IllegalArgumentException("opcode $opcode")
            }
        }
    }
}
package y2024

fun main() {
    println("advent 7")

    dataForAdvent7.data1.countValidEquations()
        .also { println("Task 1 for data 1 should be 3749 and is ... $it") }
    dataForAdvent7.data2.countValidEquations()
        .also { println("Task 1 for data 2 should be 3749 and is ... $it") }

    dataForAdvent7.data1.countValidEquations(true)
        .also { println("Task 2 for data 1 should be 11387 and is ... $it") }
    dataForAdvent7.data2.countValidEquations(true)
        .also { println("Task 2 for data 2 should be 70597497486371 and is ... $it") }
}

private fun String.countValidEquations(useConcat: Boolean = false) = lines().sumOf { it.isValidEquation(useConcat) }

private fun String.isValidEquation(useConcat: Boolean): Long {
    val expected = this
        .substringBefore(": ")
        .toLong()
    var equation: List<Pair<Long,operator>>? = this
        .substringAfter(": ")
        .split(" ")
        .let { numbers ->
            numbers.mapIndexed { index, number ->
                when (index) {
                    numbers.size-1 -> Pair(number.toLong(), operator.end)
                    else -> Pair(number.toLong(), operator.multiply)
                }
            }
        }
    while (equation != null) {
//        println(equation.joinToString("") { "${it.first}${it.second}" } )
        if (equation.evaluate(expected) == expected) return expected
        equation = nextEquation(equation, useConcat)
    }
    return 0
}

sealed class operator {
    object add : operator() {
        override fun toString(): String = "+"
    }
    object multiply : operator() {
        override fun toString(): String = "*"
    }
    object concat : operator() {
        override fun toString(): String = "||"
    }
    object end : operator() {
        override fun toString(): String = "."
    }
}

private fun List<Pair<Long,operator?>>.evaluate(expected: Long): Long {
    var result = first().first
    var index = 0
    while (index < size - 1) {
        result = when (get(index).second) {
            operator.add -> result + get(index + 1).first
            operator.multiply -> result * get(index + 1).first
            operator.concat -> "$result${get(index + 1).first}".toLong()
            else -> return result
        }
        if (result > expected) return -1
        index++
    }
    return result
}

private fun nextEquation(previousEquation: List<Pair<Long,operator>>, useConcat: Boolean): List<Pair<Long,operator>>? {
    val nextEquation = mutableListOf<Pair<Long,operator>>()
    val lastMultiply = previousEquation.indexOfLast { it.second == operator.multiply }
    val lastAdd = previousEquation.indexOfLast { it.second == operator.add }

    if (useConcat.not()) {
        return when {
            lastMultiply == -1 -> null
            else -> {
                previousEquation.forEachIndexed { index, pair ->
                    when {
                        index == previousEquation.size-1 -> nextEquation.add(pair)
                        index < lastMultiply -> nextEquation.add(pair)
                        index == lastMultiply -> nextEquation.add(Pair(pair.first, operator.add))
                        index > lastMultiply -> nextEquation.add(Pair(pair.first, operator.multiply))
                    }
                }
                nextEquation
            }
        }
    }

    return when {
        lastMultiply == -1 && lastAdd == -1 -> null
        lastAdd > lastMultiply -> {
            previousEquation.forEachIndexed { index, pair ->
                when {
                    index == previousEquation.size-1 -> nextEquation.add(pair)
                    index < lastAdd -> nextEquation.add(pair)
                    index == lastAdd -> nextEquation.add(Pair(pair.first, operator.concat))
                    index > lastAdd -> nextEquation.add(Pair(pair.first, operator.multiply))
                }
            }
            nextEquation
        }
        else -> {
            previousEquation.forEachIndexed { index, pair ->
                when {
                    index == previousEquation.size-1 -> nextEquation.add(pair)
                    index < lastMultiply -> nextEquation.add(pair)
                    index == lastMultiply -> nextEquation.add(Pair(pair.first, operator.add))
                    index > lastMultiply -> nextEquation.add(Pair(pair.first, operator.multiply))
                }
            }
            nextEquation
        }
    }
}

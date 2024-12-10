package y2024

fun main() {
    println("advent 3")

    println("Task 1 for data 1 should be 161 and is ...")
    dataForAdvent3.data1.addValidMultiplications().also { println(it) }
    println("Task 1 for data 2 should be 40 and is ...")
    dataForAdvent3.data2.addValidMultiplications().also { println(it) }
    println("Task 1 for data 4 should be 161 and is ...")
    dataForAdvent3.data4.addValidMultiplications().also { println(it) }
    println("Task 1 for data 3 is ...")
    dataForAdvent3.data3.addValidMultiplications().also { println(it) }

    println("Task 2 for data 1 should be 161 and is ...")
    dataForAdvent3.data1.addValidMultiplicationsFiltersByDoOrDont()
    println("Task 2 for data 2 should be 40 and is ...")
    dataForAdvent3.data2.addValidMultiplicationsFiltersByDoOrDont()
    println("Task 2 for data 4 should be 48 and is ...")
    dataForAdvent3.data4.addValidMultiplicationsFiltersByDoOrDont()
    println("Task 2 for data 3 is ...")
    dataForAdvent3.data3.addValidMultiplicationsFiltersByDoOrDont()
}

private fun String.addValidMultiplicationsFiltersByDoOrDont() =
        split("do()")
        .map {
            val indexOfDont = it.indexOf("don't()")
            if (indexOfDont == -1) it
            else it.dropLast(it.length - indexOfDont)
        }
        .map { it.addValidMultiplications() }
        .fold(0) { acc, i -> acc + i }
        .also { println(it) }

private fun String.addValidMultiplications() =
    split("mul(")
    .map { it.dropLast(it.length - it.indexOf(')'))  }
    .filter { it.length == it.replace(" ","").length }
    .map { it.split(",") }
    .filter { it.size == 2 }
    .mapNotNull { out ->
        out
            .map { it.toIntOrNull() }
            .let { if (it.any { i -> i == null }) null else it }
    }
    .fold(0) { acc, list -> acc + list[0]!! * list[1]!! }
package y2024

fun main() {
    println("advent 4")

    dataForAdvent4.data1.countXMAXOccurrences()
        .also { println("Task 1 for data 1 should be 4 and is ... $it") }
    dataForAdvent4.data2.countXMAXOccurrences()
        .also { println("Task 1 for data 2 should be 18 and is ... $it") }
    dataForAdvent4.data3.countXMAXOccurrences()
        .also { println("Task 1 for data 3 is ... $it") }

    dataForAdvent4.data4.countMASOccurrencesOnDiagonals()
        .also { println("Task 2 for data 4 should be 1 and is ... $it") }
    dataForAdvent4.data5.countMASOccurrencesOnDiagonals()
        .also { println("Task 2 for data 5 should be 9 and is ... $it") }
    dataForAdvent4.data3.countMASOccurrencesOnDiagonals()
        .also { println("Task 2 for data 3 is ... $it") }
}

private fun String.countXMAXOccurrences(): Int {
    val linesToAnalyze = mutableListOf<String>()
    val dataLines = this.lines()
    linesToAnalyze.addAll(dataLines)
    val dataColumns = mutableListOf<String>()
    dataLines.forEach { line ->
        line.forEachIndexed { j, c ->
            if (dataColumns.size <= j) {
                dataColumns.add(c.toString())
            } else {
                dataColumns[j] = dataColumns[j]+c
            }
        }
    }
    linesToAnalyze.addAll(dataColumns)
    val dataDiagonals = mutableMapOf<Pair<Int,Int>,String>()
    dataLines.forEachIndexed { i, line ->
        line.forEachIndexed { j, c ->
            if (i == 0 || j == 0) {
                dataDiagonals[Pair(i,j)] = c.toString()
            } else if (i >= j) {
                dataDiagonals[Pair(i-j,0)] = dataDiagonals[Pair(i-j,0)]+c
            } else {
                dataDiagonals[Pair(0,j-i)] = dataDiagonals[Pair(0,j-i)]+c
            }
        }
    }
    linesToAnalyze.addAll(dataDiagonals.values)
    val dataDiagonals2 = mutableMapOf<Pair<Int,Int>,String>()
    dataLines.forEachIndexed { i, line ->
        line.reversed().forEachIndexed { j, c ->
            if (i == 0 || j == 0) {
                dataDiagonals2[Pair(i,j)] = c.toString()
            } else if (i >= j) {
                dataDiagonals2[Pair(i-j,0)] = dataDiagonals2[Pair(i-j,0)]+c
            } else {
                dataDiagonals2[Pair(0,j-i)] = dataDiagonals2[Pair(0,j-i)]+c
            }
        }
    }
    linesToAnalyze.addAll(dataDiagonals2.values)
    return linesToAnalyze.fold(0) { acc, line ->
        acc+countOccurrences(line, "XMAS")+countOccurrences(line, "SAMX")
    }
}

private fun String.countMASOccurrencesOnDiagonals() = lines().run {
    foldIndexed(0) { i, foundTotal, line ->
        foundTotal + line.foldIndexed(0) { j, foundInLine, c ->
            if (c == 'A' && getCorners(i, j, this).isMS()) foundInLine + 1
            else foundInLine
        }
    }
}

private fun getCorners(i: Int, j: Int, dataLines: List<String>) =
    listOf(
        dataLines.getOrNull(i-1)?.getOrNull(j-1) ?: '.',
        dataLines.getOrNull(i-1)?.getOrNull(j+1) ?: '.',
        dataLines.getOrNull(i+1)?.getOrNull(j-1) ?: '.',
        dataLines.getOrNull(i+1)?.getOrNull(j+1) ?: '.'
    ).joinToString("")

private fun String.isMS() =
    this == "MSMS" || this == "MMSS" || this == "SSMM" || this == "SMSM"

private fun countOccurrences(str: String, searchStr: String): Int {
    var count = 0
    var startIndex = 0

    while (startIndex < str.length) {
        val index = str.indexOf(searchStr, startIndex)
        if (index >= 0) {
            count++
            startIndex = index + 1
        } else {
            break
        }
    }

    return count
}
package algorithms
fun main() {
    listOf(
        "Hello, World!" to true,
        "Hello, (World)!" to true,
        "Hello, (World!" to false,
        "(Hello, (World)!)" to true,
        "(Hello, [World]!)" to true,
        "(Hello, World]!)" to false,
        "(Hello, [World]!" to false,
        "[(Hello, World]!)" to false
    ).forEach { println("${it.first} is ${if (it.first.validateString().not()) "in" else ""}valid: ${it.first.validateString() == it.second}") }
}

fun String.validateString(): Boolean = fold(mutableListOf<Char>()) { stack, c ->
    when (c) {
        '(' -> stack.add('(')
        '[' -> stack.add('(')
        ')' -> if (stack.lastOrNull { it == '(' } == null) return false else stack.removeLast()
        ']' -> if (stack.lastOrNull { it == '[' } == null) return false else stack.removeLast()
    }
    stack
}.isEmpty()

fun String.validateString2(): Boolean {
    val stack = mutableListOf<Char>()
    for (c in this) {
        when (c) {
            '(' -> stack.add(')')
            '[' -> stack.add(']')
            ')' -> if (stack.isEmpty() || stack.removeAt(stack.lastIndex) != ')') return false
            ']' -> if (stack.isEmpty() || stack.removeAt(stack.lastIndex) != ']') return false
        }
    }
    return stack.isEmpty()
}

fun String.validateString3(): Boolean {
    val first1 = indexOfFirst { it == '(' }
    val first2 = indexOfFirst { it == '[' }
    val firstIs1 = first1 < first2
    val first = if (firstIs1) first1 else first2
    val last = if (firstIs1) indexOfLast { it == ')' } else indexOfLast { it == ']' }
    return when {
        (first == -1 && last == -1) -> true
        (first == -1) != (last == -1) -> false
        else -> substring(first+1, last)
//            .also { println(it) }
            .validateString()
    }
}

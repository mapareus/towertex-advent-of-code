package patterns

interface Y_Strategy { fun execute(str: String) }

class Y_ConcreteStrategy1 : Y_Strategy { override fun execute(str: String) = println("Executing strategy 1 on $str") }

class Y_Context(val input: String, private val strategy: Y_Strategy) {
    fun executeStrategy() = strategy.execute(input)
}

fun main() {
    Y_Context("input from main", Y_ConcreteStrategy1()).executeStrategy()
}
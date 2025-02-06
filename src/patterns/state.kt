package patterns

interface S_State {
    fun doSomething()
    fun doSomethingElse()
}

class S_ConcreteState1 : S_State {
    override fun doSomething() = println("Doing something in state 1")
    override fun doSomethingElse() = println("Doing something else in state 1")
}

class S_ConcreteState2 : S_State {
    override fun doSomething() = println("Doing something in state 2")
    override fun doSomethingElse() = println("Doing something else in state 2")
}

class S_Context(private var state: S_State) {
    fun doSomething() = state.doSomething()
    fun doSomethingElse() = state.doSomethingElse()
}
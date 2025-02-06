package patterns

interface D_Component {
    fun operation()
}

class D_ConcreteComponent : D_Component {
    override fun operation() = println("Operation in ConcreteComponent")
}

abstract class D_Decorator(protected val component: D_Component) : D_Component

class D_ConcreteDecorator1(component: D_Component) : D_Decorator(component) {
    override fun operation() {
        println("Operation in ConcreteDecorator1")
        component.operation()
    }
}

class D_ConcreteDecorator2(component: D_Component) : D_Decorator(component) {
    override fun operation() {
        println("Operation in ConcreteDecorator2")
        component.operation()
    }
}

fun main() {
    D_ConcreteDecorator2(D_ConcreteDecorator1(D_ConcreteComponent())).operation()
}
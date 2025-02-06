package patterns

interface F_Product { fun outPrice(): Int }
abstract class F_Product_plus(protected val inPrice: Int): F_Product

class F_ConcreteProduct_plus1(inPrice: Int): F_Product_plus(inPrice) { override fun outPrice() = inPrice+1 }
class F_ConcreteProduct_plus2(inPrice: Int): F_Product_plus(inPrice) { override fun outPrice() = inPrice+2 }

abstract class F_Creator: F_Product {
    var products = mutableListOf<F_Product>()
    override fun outPrice(): Int = products.sumOf { it.outPrice() }

    abstract fun create(inPrice: Int): F_Product_plus // Factory Method
    fun product_plus(inPrice: Int) { products += create(inPrice) }
}

class F_ConcreteCreator1 : F_Creator() { override fun create(inPrice: Int) = F_ConcreteProduct_plus1(inPrice) }
class F_ConcreteCreator2 : F_Creator() { override fun create(inPrice: Int) = F_ConcreteProduct_plus2(inPrice) }

fun main() {
    F_ConcreteCreator1().apply { product_plus(10) }.run { println(outPrice()) }
    F_ConcreteCreator2().apply { product_plus(10) }.run { println(outPrice()) }
}
package patterns

interface AF_Product { fun outPrice(): Int }
abstract class AF_Product_plus(protected val inPrice: Int): AF_Product
abstract class AF_Product_times(protected val inPrice: Int): AF_Product

class AF_ConcreteProduct_plus1(inPrice: Int) : AF_Product_plus(inPrice) { override fun outPrice() = inPrice+1 }
class AF_ConcreteProduct_plus2(inPrice: Int) : AF_Product_plus(inPrice) { override fun outPrice() = inPrice+2 }
class AF_ConcreteProduct_times1(inPrice: Int) : AF_Product_times(inPrice) { override fun outPrice() = inPrice*1 }
class AF_ConcreteProduct_times2(inPrice: Int) : AF_Product_times(inPrice) { override fun outPrice() = inPrice*2 }

class AF_Client(private val factory: AF_AbstractFactory): AF_Product {
    var products = mutableListOf<AF_Product>()
    override fun outPrice(): Int = products.sumOf { it.outPrice() }

    fun product_plus(inPrice: Int) { products += factory.createProduct_plus(inPrice) }
    fun product_times(inPrice: Int) { products += factory.createProduct_times(inPrice) }
}

interface AF_AbstractFactory {
    fun createProduct_plus(inPrice: Int): AF_Product_plus
    fun createProduct_times(inPrice: Int): AF_Product_times
}

class AF_ConcreteFactory1 : AF_AbstractFactory {
    override fun createProduct_plus(inPrice: Int): AF_Product_plus = AF_ConcreteProduct_plus1(inPrice)
    override fun createProduct_times(inPrice: Int): AF_Product_times = AF_ConcreteProduct_times1(inPrice)
}
class AF_ConcreteFactory2 : AF_AbstractFactory {
    override fun createProduct_plus(inPrice: Int): AF_Product_plus = AF_ConcreteProduct_plus2(inPrice)
    override fun createProduct_times(inPrice: Int): AF_Product_times = AF_ConcreteProduct_times2(inPrice)
}

fun main() {
    AF_Client(AF_ConcreteFactory1())
        .apply { product_plus(10)}
        .apply { product_times(10) }
        .run { println(outPrice()) }
    AF_Client(AF_ConcreteFactory2())
        .apply { product_plus(10) }
        .apply { product_times(10) }
        .run { println(outPrice()) }
}
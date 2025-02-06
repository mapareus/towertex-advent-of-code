package patterns

data class B_Product(val name: String, val price: Double, val sales: List<Double>)

interface B_Builder {
    fun setName(name: String): B_Builder
    fun setPrice(price: Double): B_Builder
    fun addSale(sale: Double): B_Builder
    fun build(): B_Product
}

class B_ConcreteBuilder : B_Builder {
    private var name: String = ""
    private var price: Double = 0.0
    private val sales = mutableListOf<Double>()

    override fun setName(name: String): B_Builder { this.name = name; return this }
    override fun setPrice(price: Double): B_Builder { this.price = price; return this }
    override fun addSale(sale: Double): B_Builder { sales.add(sale); return this }
    override fun build(): B_Product = B_Product(name, price, sales)
}

fun main() {
    B_ConcreteBuilder()
        .setName("Product")
        .setPrice(100.0)
        .addSale(10.0)
        .addSale(20.0)
        .addSale(30.0)
        .build()
        .also { println(it) }
}
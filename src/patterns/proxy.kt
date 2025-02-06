package patterns

interface P_RealObject {
    fun performOperation()
}
class P_RealObjectImpl : P_RealObject {
    override fun performOperation() {
        println("RealObject performing operation")
    }
}
class P_VirtualProxy : P_RealObject {
    private val realObject by lazy { P_RealObjectImpl() }
    override fun performOperation() {
        realObject.performOperation()
    }
}
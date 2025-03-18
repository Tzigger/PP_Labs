interface PaymentMethod {
    fun pay(amount: Double): Boolean
}
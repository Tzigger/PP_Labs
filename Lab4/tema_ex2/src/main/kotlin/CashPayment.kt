class CashPayment(private var cashOnHand: Double) : PaymentMethod {
    override fun pay(fee: Double): Boolean {
        if (fee > cashOnHand) return false
        cashOnHand -= fee
        return true
    }

    fun remainingCash(): Double = cashOnHand
}
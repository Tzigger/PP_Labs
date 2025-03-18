class CreditCardPayment(private val cinemaAccount: CinemaAccount) : PaymentMethod {
    override fun pay(amount: Double): Boolean {
        return cinemaAccount.withdraw(amount)
    }
}
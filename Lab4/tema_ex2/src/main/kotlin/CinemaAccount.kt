import java.time.LocalDate

typealias ExpiryDate = LocalDate

class CinemaAccount(
    private var balance: Double,
    private val cardholder: String,
    private val cardId: String,
    private val validUntil: ExpiryDate,
    private val securityCode: Int
) {
    fun withdraw(amount: Double): Boolean {
        if (amount <= 0 || amount > balance) return false
        balance -= amount
        return true
    }

    fun deposit(amount: Double) {
        if (amount > 0) balance += amount
    }

    fun getBalance(): Double = balance
}
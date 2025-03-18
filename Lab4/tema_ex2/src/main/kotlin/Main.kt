import java.time.LocalDate
import java.util.Scanner

fun main() {
    val scanner = Scanner(System.`in`)

    val cinema = CinemaBoxOffice(mapOf(
        "Fast and Furious" to 29.99,
        "Dune" to 34.50,
        "Space Racers" to 49.99,
        "Cyber Knights" to 54.75
    ))

    print("Which cinematic experience would you like to attend? ")

    val movieTitle = scanner.nextLine()

    print("Choose payment method [credit/cash]: ")
    when (val paymentChoice = scanner.next().lowercase()) {
        "credit" -> {
            scanner.nextLine() // Consume leftover newline
            println("Enter your card details:")
            print("Cardholder name: ")
            val name = scanner.nextLine()
            print("16-digit card number: ")
            val cardNumber = scanner.nextLine()
            print("Expiration date (YYYY-MM-DD): ")
            val expiry = LocalDate.parse(scanner.next())
            print("Security PIN: ")
            val pin = scanner.nextInt()
            print("Current account balance: ")
            val balance = scanner.nextDouble()

            val ticket = cinema.purchaseTicket(
                movieTitle,
                CreditCardPayment(CinemaAccount(balance, name, cardNumber, expiry, pin)),
                )

            println(ticket ?: "❌ Payment failed or invalid movie selection")
        }
        "cash" -> {
            print("Insert cash amount available: $")
            val cashAmount = scanner.nextDouble()

            val ticket = cinema.purchaseTicket(
                movieTitle,
                CashPayment(cashAmount)
            )

            println(ticket ?: "❌ Insufficient cash or invalid movie choice")
        }
        else -> println("⚠️ Unsupported payment option")
    }
}
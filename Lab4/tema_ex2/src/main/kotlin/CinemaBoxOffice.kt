class CinemaBoxOffice(private val movieCatalog: Map<String, Double>) {
    fun purchaseTicket(movieTitle: String, payment: PaymentMethod): String? {
        val ticketPrice = movieCatalog[movieTitle] ?: return null

        return if (payment.pay(ticketPrice)) {
            """
            ╔═══════════════════════════════
            ║    CINEMA TICKET    
            ╠═══════════════════════════════
            ║  Movie: ${movieTitle.padEnd(30)}
            ║  Price: $${"%.2f".format(ticketPrice).padEnd(10)}
            ║  Showtime: ${java.time.LocalDateTime.now().plusDays(3)}
            ╰═══════════════════════════════
            """.trimIndent()
        } else null
    }
}
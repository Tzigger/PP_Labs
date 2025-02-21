
fun calculPoloneza(pol: List<String>): Double {
    val stiva = ArrayDeque<Double>()

    for (operator in pol)
    {
        when
        {
            operator.toDoubleOrNull() != null -> {
                stiva.addFirst(operator.toDouble())
            }
            operator in listOf("+", "-", "*", "/") -> {
                val operand2 = stiva.removeFirst()
                val operand1 = stiva.removeFirst()

                val result = when (operator) {
                    "+" -> operand1 + operand2
                    "-" -> operand1 - operand2
                    "*" -> operand1 * operand2
                    "/" -> operand1 / operand2
                    else -> 0.0
                }

                stiva.addFirst(result)
            }
        }
    }

    return stiva.first()
}


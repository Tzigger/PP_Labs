fun stringToPol(expression: String): List<String>
{
    val output = mutableListOf<String>()
    val operators = ArrayDeque<Char>()

    var i = 0
    while (i < expression.length) {
        val char = expression[i]

        when {
            char.isDigit() -> {
                var num = ""
                while (i < expression.length && (expression[i].isDigit() || expression[i] == '.'))
                {
                    num += expression[i]
                    i++
                }
                output.add(num)
                continue
            }
            char == '(' -> operators.addFirst(char)
            char == ')' -> {
                while (operators.isNotEmpty() && operators.first() != '(') {
                    output.add(operators.removeFirst().toString())
                }
                operators.removeFirst() // Elimina paranteza deschisă '('
            }
            char in listOf('+', '-', '*', '/') -> {
                while (operators.isNotEmpty() && ordin(operators.first()) >= ordin(char)) {
                    output.add(operators.removeFirst().toString())
                }
                operators.addFirst(char)
            }
        }

        i++
    }

    while (operators.isNotEmpty()) {
        output.add(operators.removeFirst().toString())
    }

    return output
}

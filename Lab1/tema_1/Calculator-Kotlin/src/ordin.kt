fun ordin(op: Char): Int{
    return when (op) {
        '+', '-' -> 1
        '*', '/' -> 2
        else -> -1
    }
}
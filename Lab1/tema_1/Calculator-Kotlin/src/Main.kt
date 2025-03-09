import java.util.*


fun main() {
    val reader = Scanner(System.`in`)
    val expr = reader.next()

    val formaPoloneza = stringToPol(expr)
    print(formaPoloneza)

    val final = calculPoloneza(formaPoloneza)
    print("\n")
    System.out.printf("\n\tRezultatul este: %f\n",final)


}
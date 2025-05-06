// Finite State Machine for output calculation
class LogicGateFSM {
    private var currentState: Boolean = true // Start with true for AND gate

    fun processInput(input: Boolean) {
        currentState = currentState && input
    }

    fun getCurrentState(): Boolean = currentState
}
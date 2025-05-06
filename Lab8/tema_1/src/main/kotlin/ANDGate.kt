class ANDGate(private val implementation: LogicGateImplementation, private val inputs: List<Boolean>) : LogicGate {
    private val fsm = LogicGateFSM()

    override fun calculateOutput(): Boolean {
        // Process each input through the FSM
        inputs.forEach { input ->
            fsm.processInput(input)
        }
        return fsm.getCurrentState()
    }
}
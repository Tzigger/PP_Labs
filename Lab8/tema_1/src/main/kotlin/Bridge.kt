// Bridge Pattern
interface LogicGate {
    fun calculateOutput(): Boolean
}

interface LogicGateImplementation {
    fun processInputs(inputs: List<Boolean>): Boolean
}

class ANDGateImplementation : LogicGateImplementation {
    override fun processInputs(inputs: List<Boolean>): Boolean {
        return inputs.all { it }
    }
}

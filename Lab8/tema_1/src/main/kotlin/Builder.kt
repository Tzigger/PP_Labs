interface LogicGateBuilder {
    fun addInput(input: Boolean): LogicGateBuilder
    fun build(): LogicGate
}


// Builder implementations for different input sizes treated separately
class ANDGate2InputBuilder(implementation: LogicGateImplementation) : LogicGateBuilder {
    private val inputs = mutableListOf<Boolean>()

    override fun addInput(input: Boolean): LogicGateBuilder {
        if (inputs.size < 2) {
            inputs.add(input)
        }
        return this
    }

    override fun build(): LogicGate {
        require(inputs.size == 2) { "2-input AND gate requires exactly 2 inputs" }
        return ANDGate(ANDGateImplementation(), inputs)
    }
}

class ANDGate3InputBuilder(implementation: LogicGateImplementation) : LogicGateBuilder {
    private val inputs = mutableListOf<Boolean>()

    override fun addInput(input: Boolean): LogicGateBuilder {
        if (inputs.size < 3) {
            inputs.add(input)
        }
        return this
    }

    override fun build(): LogicGate {
        require(inputs.size == 3) { "3-input AND gate requires exactly 3 inputs" }
        return ANDGate(ANDGateImplementation(), inputs)
    }
}

class ANDGate4InputBuilder(implementation: LogicGateImplementation) : LogicGateBuilder {
    private val inputs = mutableListOf<Boolean>()

    override fun addInput(input: Boolean): LogicGateBuilder {
        if (inputs.size < 4) {
            inputs.add(input)
        }
        return this
    }

    override fun build(): LogicGate {
        require(inputs.size == 4) { "4-input AND gate requires exactly 4 inputs" }
        return ANDGate(ANDGateImplementation(), inputs)
    }
}

class ANDGate8InputBuilder(implementation: LogicGateImplementation) : LogicGateBuilder {
    private val inputs = mutableListOf<Boolean>()

    override fun addInput(input: Boolean): LogicGateBuilder {
        if (inputs.size < 8) {
            inputs.add(input)
        }
        return this
    }

    override fun build(): LogicGate {
        require(inputs.size == 8) { "8-input AND gate requires exactly 8 inputs" }
        return ANDGate(ANDGateImplementation(), inputs)
    }
}



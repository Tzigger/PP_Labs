fun main() {
    println("AND Gate Tests")
    println("-------------------")
    
    // Test 2-input AND gate
    val and2Builder = ANDGate2InputBuilder(ANDGateImplementation())
    val and2Gate = and2Builder
        .addInput(true)
        .addInput(true)
        .build()
    println("2-input AND gate output: ${and2Gate.calculateOutput()}")
    
    // Test 3-input AND gate
    val and3Builder = ANDGate3InputBuilder(ANDGateImplementation())
    val and3Gate = and3Builder
        .addInput(true)
        .addInput(true)
        .addInput(false)
        .build()
    println("3-input AND gate output: ${and3Gate.calculateOutput()}")
    
    // Test 4-input AND gate
    val and4Builder = ANDGate4InputBuilder(ANDGateImplementation())
    val and4Gate = and4Builder
        .addInput(true)
        .addInput(true)
        .addInput(true)
        .addInput(true)
        .build()
    println("4-input AND gate output: ${and4Gate.calculateOutput()}")
    
    // Test 8-input AND gate
    val and8Builder = ANDGate8InputBuilder(ANDGateImplementation())
    val and8Gate = and8Builder
        .addInput(true)
        .addInput(true)
        .addInput(true)
        .addInput(true)
        .addInput(false)
        .addInput(true)
        .addInput(true)
        .addInput(true)
        .build()
    println("8-input AND gate output: ${and8Gate.calculateOutput()}")
}
package org.pchapin.dragon

object NFAExample {

    def main(args: Array[String]): Unit = {
        // Consider the RE: (a|b)*c

        // Let's build an NFA!
        // We will need primitive NFAs for the three strings "a", "b", and "c".
        val primitiveA = new NFA(0, 1, Map(TransitionFunctionArgument(0, 'a') -> Set(1)))
        val primitiveB = new NFA(0, 1, Map(TransitionFunctionArgument(0, 'b') -> Set(1)))
        val primitiveC = new NFA(0, 1, Map(TransitionFunctionArgument(0, 'c') -> Set(1)))

        // Create the combined NFA using Thompson's Construction.
        val rawNFA = (primitiveA union primitiveB).kleeneClosure concatenate primitiveC

        // Convert the NFA to a DFA using Subset Construction.
        val rawDFA = rawNFA.toDFA

        // Minimize the raw DFA using Hopcroft's Algorithm.
        val minimalDFA = rawDFA.minimize

        if (minimalDFA.`match`("abbaabc")) {
            System.out.println("match [this is correct]")
        }
        else {
            System.out.println("no match [this is wrong]")
        }
    }

}

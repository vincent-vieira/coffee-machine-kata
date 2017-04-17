package io.vieira.coffeemachine;

import java.util.List;

public interface CoffeeMachine {

    /**
     * Processes the supplied instructions, or dies trying.
     * Instructions are resolved against {@link io.vieira.coffeemachine.instruction.Instructable} values.
     *
     * @param instructions the target instructions
     * @param paymentAmount the amount of money supplied inside the machine.
     */
    void process(List<String> instructions, double paymentAmount);

    /**
     * Triggers the reporting of already brewed drinks.
     */
    void report();
}

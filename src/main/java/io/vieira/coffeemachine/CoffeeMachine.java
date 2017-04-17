package io.vieira.coffeemachine;

import io.vieira.coffeemachine.instruction.Drink;

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

    @FunctionalInterface
    interface DrinkQuantityChecker {

        DrinkQuantityChecker NOOP = drink -> true;

        /**
         * Can the supplied drink can be brewed ?
         * @param drink the drink to brew
         * @return a boolean showing if the drink can be brewed
         */
        boolean canBrew(Drink drink);
    }

    @FunctionalInterface
    interface MissingDrinkNotifier {

        MissingDrinkNotifier NOOP = drink -> {};

        /**
         * Notifies that the supplied {@link Drink} is missing.
         *
         * @param drink the missing drink.
         */
        void notifyMissing(Drink drink);
    }
}

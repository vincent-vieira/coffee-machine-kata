package io.vieira.coffeemachine.reporting;

import io.vieira.coffeemachine.instruction.Drink;

import java.util.Map;

@FunctionalInterface
public interface DrinksSalesReporter {

    DrinksSalesReporter NOOP = (brewedDrinksSummary, totalEarned) -> {};

    /**
     * Allow to handle a coffee machine report.
     *
     * @param brewedDrinksSummary a map containing for each {@link Drink} type, the number of drinks made yet so far.
     * @param totalEarned the amount of money earned.
     */
    void printReport(Map<Drink.Type, Integer> brewedDrinksSummary, double totalEarned);
}

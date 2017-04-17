package io.vieira.coffeemachine.reporting;

import io.vieira.coffeemachine.instruction.Drink;

import java.util.Map;
import java.util.stream.Collectors;

@FunctionalInterface
public interface DrinksSalesReporter {

    DrinksSalesReporter DEFAULT = (brewedDrinksSummary, totalEarned) -> System.out.printf(
            "%f earned brewing %s",
            totalEarned,
            brewedDrinksSummary.entrySet().stream().map(entry -> String.format("%d %ss", entry.getValue(), entry.getKey().name().toLowerCase())).collect(Collectors.joining(","))
    );

    /**
     * Allows to handle a coffee machine report.
     *
     * @param brewedDrinksSummary a map containing for each {@link Drink} type, the number of drinks made yet so far.
     * @param totalEarned the amount of money earned.
     */
    void printReport(Map<Drink.Type, Integer> brewedDrinksSummary, double totalEarned);
}

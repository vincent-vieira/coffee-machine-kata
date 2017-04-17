package io.vieira.coffeemachine.reporting;

import io.vieira.coffeemachine.CoffeeMachine;
import io.vieira.coffeemachine.CoffeeMachineImpl;
import io.vieira.coffeemachine.instruction.Drink;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collections;

import static info.solidsoft.mockito.java8.AssertionMatcher.assertArg;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class DrinksReportingSpec {

    private final DrinksSalesReporter reporter = Mockito.mock(DrinksSalesReporter.class);

    private final CoffeeMachine machine = new CoffeeMachineImpl(reporter);

    @Test
    public void brewedDrinksShouldBeRegisteredAndAvailableForReport() {
        machine.process(Arrays.asList(
                new Drink(Drink.Type.ORANGE_JUICE).getInstruction(),
                new Drink(Drink.Type.CHOCOLATE, 1).getInstruction(),
                new Drink(Drink.Type.COFFEE).getInstruction(),
                new Drink(Drink.Type.COFFEE).getInstruction()
        ), 5);

        machine.report();
        verify(reporter, times(1)).printReport(assertArg(sales -> assertThat(sales)
                .containsEntry(Drink.Type.ORANGE_JUICE, 1)
                .containsEntry(Drink.Type.CHOCOLATE, 1)
                .containsEntry(Drink.Type.COFFEE, 2)
        ), assertArg(totalAmount -> assertThat(totalAmount).isEqualTo(2.3)));
    }

    @Test
    public void noDrinksShouldBeReportedWhenNoDrinksHaveBeenAlreadyBrewed() {
        machine.report();

        verify(reporter, times(1)).printReport(eq(Collections.emptyMap()), eq(0d));
    }
}

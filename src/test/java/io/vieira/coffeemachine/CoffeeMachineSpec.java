package io.vieira.coffeemachine;

import io.vieira.coffeemachine.instruction.Drink;
import io.vieira.coffeemachine.instruction.MachineMessageHandler;
import io.vieira.coffeemachine.instruction.UserMessage;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collections;

import static info.solidsoft.mockito.java8.AssertionMatcher.assertArg;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CoffeeMachineSpec {

    private final MachineMessageHandler messageHandler = Mockito.mock(MachineMessageHandler.class);

    private final CoffeeMachine.DrinkQuantityChecker quantityChecker = Mockito.mock(CoffeeMachine.DrinkQuantityChecker.class);

    private final CoffeeMachine.MissingDrinkNotifier missingDrinkNotifier = Mockito.mock(CoffeeMachine.MissingDrinkNotifier.class);

    private final CoffeeMachine coffeeMachine = new CoffeeMachineImpl(messageHandler, null, quantityChecker, missingDrinkNotifier);

    @Test
    public void processingUserMessagesShouldInvokeTheHandler(){
        coffeeMachine.process(Collections.singletonList("M:Hey!"), 0);
        verify(messageHandler).handleMessage(assertArg(message -> assertThat(message).isInstanceOf(UserMessage.class)));
    }

    @Test
    public void drinksShouldNotBeBrewedAndWeShouldBeNotifiedOfMissingDrinks() {
        when(quantityChecker.canBrew(any())).thenReturn(false);

        coffeeMachine.process(
                Arrays.asList(
                        new Drink(Drink.Type.COFFEE).getInstruction(),
                        new Drink(Drink.Type.CHOCOLATE).getInstruction()
                ),
                3
        );

        verify(missingDrinkNotifier, times(1)).notifyMissing(assertArg(drink -> assertThat(drink.getType()).isEqualTo(Drink.Type.COFFEE)));
        verify(missingDrinkNotifier, times(1)).notifyMissing(assertArg(drink -> assertThat(drink.getType()).isEqualTo(Drink.Type.CHOCOLATE)));
    }

    @Test
    public void drinksShouldBeBrewedInCaseOfSuppliesPresent() {
        when(quantityChecker.canBrew(any())).thenReturn(true);

        coffeeMachine.process(
                Arrays.asList(
                        new Drink(Drink.Type.COFFEE).getInstruction(),
                        new Drink(Drink.Type.CHOCOLATE).getInstruction()
                ),
                3
        );
        verify(missingDrinkNotifier, never()).notifyMissing(any());
    }

    @Test
    public void partialBrewingShouldBeStillPossible() {
        //With Mockito, general rules at first, most specific after.
        when(quantityChecker.canBrew(any())).thenReturn(true);
        when(quantityChecker.canBrew(argThat(drink -> drink.getType() == Drink.Type.CHOCOLATE))).thenReturn(false);

        coffeeMachine.process(
                Arrays.asList(
                        new Drink(Drink.Type.COFFEE).getInstruction(),
                        new Drink(Drink.Type.CHOCOLATE).getInstruction()
                ),
                3
        );
        verify(missingDrinkNotifier, only()).notifyMissing(assertArg(drink -> assertThat(drink.getType()).isEqualTo(Drink.Type.CHOCOLATE)));
    }
}

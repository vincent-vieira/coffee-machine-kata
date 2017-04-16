package io.vieira.coffeemachine.payment;

import io.vieira.coffeemachine.CoffeeMachine;
import io.vieira.coffeemachine.CoffeeMachineImpl;
import io.vieira.coffeemachine.instruction.Drink;
import io.vieira.coffeemachine.instruction.Instructable;
import io.vieira.coffeemachine.instruction.MachineMessageHandler;
import io.vieira.coffeemachine.instruction.UserMessage;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static info.solidsoft.mockito.java8.AssertionMatcher.assertArg;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class MakingThemPaySpec {

    private final MachineMessageHandler messageHandler = Mockito.mock(MachineMessageHandler.class);

    private final CoffeeMachine coffeeMachine = new CoffeeMachineImpl(messageHandler);

    @Test
    public void brewingDrinksWithEnoughMoneyShouldShowThem() {
        List<String> drinksToBrew = Stream.of(
                new Drink(Drink.Type.COFFEE, 1),
                new Drink(Drink.Type.CHOCOLATE)
        ).map(Instructable::getInstruction).collect(Collectors.toList());
        coffeeMachine.process(drinksToBrew, 1.5);

        verify(messageHandler, times(1)).handleMessage(assertArg(drink -> assertThat(drink)
                .isInstanceOf(Drink.class)
                .hasFieldOrPropertyWithValue("price", 0.6)
                .hasFieldOrPropertyWithValue("sugarNumber", 1)));
        verify(messageHandler, times(1)).handleMessage(assertArg(drink -> assertThat(drink)
                .isInstanceOf(Drink.class)
                .hasFieldOrPropertyWithValue("price", 0.5)
                .hasFieldOrPropertyWithValue("sugarNumber", 0)));
    }

    @Test
    public void brewingDrinksWithoutEnoughMoneyShouldShowAMessage() {
        List<String> drinksToBrew = Stream.of(
                new Drink(Drink.Type.COFFEE, 1),
                new Drink(Drink.Type.CHOCOLATE)
        ).map(Instructable::getInstruction).collect(Collectors.toList());
        coffeeMachine.process(drinksToBrew, 0.7);

        verify(messageHandler, times(1)).handleMessage(assertArg(drink -> assertThat(drink).isInstanceOf(Drink.class).hasFieldOrPropertyWithValue("price", 0.6).hasFieldOrPropertyWithValue("sugarNumber", 1)));
        verify(messageHandler, never()).handleMessage(assertArg(drink -> assertThat(drink).isInstanceOf(Drink.class).hasFieldOrPropertyWithValue("price", 0.5).hasFieldOrPropertyWithValue("sugarNumber", 0)));
        verify(messageHandler, times(1)).handleMessage(assertArg(message -> {
            assertThat(message).isInstanceOf(UserMessage.class);
            assertThat(((UserMessage) message).getMessage()).isEqualTo("0.4 is missing to brew your drink !");
        }));
    }
}

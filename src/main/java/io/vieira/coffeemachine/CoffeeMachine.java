package io.vieira.coffeemachine;

import io.vieira.coffeemachine.instruction.Drink;
import io.vieira.coffeemachine.instruction.UserMessage;

import java.util.List;

public interface CoffeeMachine {
    void brew(List<Drink> drinksToBrew, double paymentAmount);
    void processMessage(UserMessage message);
}

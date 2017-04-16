package io.vieira.coffeemachine;

import io.vieira.coffeemachine.instruction.Drink;
import io.vieira.coffeemachine.instruction.Instructable;
import io.vieira.coffeemachine.instruction.UserMessage;
import io.vieira.coffeemachine.instruction.MachineMessageHandler;

import java.util.List;
import java.util.stream.Collectors;

public class CoffeeMachineImpl implements CoffeeMachine {

    private MachineMessageHandler messageHandler;

    public CoffeeMachineImpl(MachineMessageHandler handler) {
        this.messageHandler = handler == null ? MachineMessageHandler.DEFAULT: handler;
    }

    @Override
    public void process(List<String> instructions, double paymentAmount) {
        List<Instructable> instructables = instructions.stream().map(instruction -> {
            String[] parts = instruction.split(":", -1);
            if(parts.length < 2 || parts.length > 3){
                throw new IllegalArgumentException("Invalid instruction : "+instruction);
            }
            switch(parts[0]) {
                case "M":
                    return new UserMessage(parts[1]);
                default:
                    return new Drink(Drink.Type.fromCode(parts[0]), "".equals(parts[1]) ? 0 : Integer.parseInt(parts[1]));
            }
        }).collect(Collectors.toList());

        for(Instructable instructable : instructables) {
            if(instructable instanceof Drink) {
                Drink toBrew = (Drink) instructable;
                if(paymentAmount < toBrew.getPrice()) {
                    messageHandler.handleMessage(new UserMessage(
                            String.format("%.1f is missing to brew your drink !", toBrew.getPrice() - paymentAmount)
                    ));
                }
                else {
                    messageHandler.handleMessage(toBrew);
                    paymentAmount -= toBrew.getPrice();
                }
            }
            else {
                messageHandler.handleMessage(instructable);
            }
        }
    }
}
package io.vieira.coffeemachine;

import io.vieira.coffeemachine.instruction.Drink;
import io.vieira.coffeemachine.instruction.Instructable;
import io.vieira.coffeemachine.instruction.MachineMessageHandler;
import io.vieira.coffeemachine.instruction.UserMessage;
import io.vieira.coffeemachine.reporting.DrinksSalesReporter;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class CoffeeMachineImpl implements CoffeeMachine {

    private final DrinksSalesReporter salesReporter;
    private final MachineMessageHandler messageHandler;
    private final Map<Drink.Type, Integer> summary = new ConcurrentHashMap<>();

    public CoffeeMachineImpl(){
        this(null, null);
    }

    public CoffeeMachineImpl(MachineMessageHandler handler) {
        this(handler, null);
    }

    public CoffeeMachineImpl(DrinksSalesReporter reporter) {
        this(null, reporter);
    }

    public CoffeeMachineImpl(MachineMessageHandler handler, DrinksSalesReporter salesReporter) {
        this.messageHandler = handler == null ? MachineMessageHandler.DEFAULT: handler;
        this.salesReporter = salesReporter == null ? DrinksSalesReporter.NOOP : salesReporter;
    }

    @Override
    public void process(List<String> instructions, double paymentAmount) {
        List<Instructable> instructables = instructions.stream().map(instruction -> {
            String[] parts = instruction.split(":", -1);
            if(parts.length < 2 || parts.length > 3){
                throw new IllegalArgumentException("Invalid instruction : "+instruction);
            }
            String drinkCode = parts[0];
            switch(drinkCode) {
                case "M":
                    return new UserMessage(parts[1]);
                default:
                    boolean isDrinkExtraHot = drinkCode.contains("h");
                    if(isDrinkExtraHot) drinkCode = drinkCode.replace("h", "");
                    return new Drink(Drink.Type.fromCode(drinkCode), "".equals(parts[1]) ? 0 : Integer.parseInt(parts[1]), isDrinkExtraHot);
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

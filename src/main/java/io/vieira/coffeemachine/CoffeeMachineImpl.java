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
    private final DrinkQuantityChecker quantityChecker;
    private final MissingDrinkNotifier missingDrinkNotifier;
    private final Map<Drink.Type, Integer> summary = new ConcurrentHashMap<>();

    public CoffeeMachineImpl(){
        this(null, null, null, null);
    }

    public CoffeeMachineImpl(MachineMessageHandler handler) {
        this(handler, null, null, null);
    }

    public CoffeeMachineImpl(DrinksSalesReporter reporter) {
        this(null, reporter, null, null);
    }

    public CoffeeMachineImpl(MachineMessageHandler handler, DrinksSalesReporter salesReporter, DrinkQuantityChecker quantityChecker, MissingDrinkNotifier missingDrinkNotifier) {
        this.messageHandler = handler == null ? MachineMessageHandler.DEFAULT: handler;
        this.salesReporter = salesReporter == null ? DrinksSalesReporter.DEFAULT : salesReporter;
        this.quantityChecker = quantityChecker == null ? DrinkQuantityChecker.NOOP : quantityChecker;
        this.missingDrinkNotifier = missingDrinkNotifier == null ? MissingDrinkNotifier.NOOP : missingDrinkNotifier;
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
                else if(this.quantityChecker.canBrew(toBrew)) {
                    messageHandler.handleMessage(toBrew);
                    paymentAmount -= toBrew.getPrice();
                    summary.computeIfPresent(toBrew.getType(), (type, integer) -> ++integer);
                    summary.putIfAbsent(toBrew.getType(), 1);
                }
                else {
                    this.missingDrinkNotifier.notifyMissing(toBrew);
                }
            }
            else {
                messageHandler.handleMessage(instructable);
            }
        }
    }

    @Override
    public void report() {
        this.salesReporter.printReport(
                this.summary,
                this.summary.entrySet().stream().mapToDouble(drinkEntry -> drinkEntry.getKey().getPrice() * drinkEntry.getValue()).sum()
        );
    }
}

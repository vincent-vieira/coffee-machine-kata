package io.vieira.coffeemachine.instruction;

import io.vieira.coffeemachine.payment.Payable;

import java.text.MessageFormat;

public class Drink implements Instructable, Payable {

    public enum Type implements Instructable {
        TEA("T", 0.4),
        CHOCOLATE("H", 0.5),
        COFFEE("C", 0.6);

        private final String instruction;
        private final double price;

        Type(String drinkInstruction, double price) {
            this.instruction = drinkInstruction;
            this.price = price;
        }

        @Override
        public String getInstruction() {
            return instruction;
        }

        public double getPrice() {
            return price;
        }

        public static Type fromCode(String code) {
            // values() will be inlined by javac, all great !
            for(Type value : values()) {
                if(value.getInstruction().equals(code)){
                    return value;
                }
            }
            throw new IllegalArgumentException("No Type found for code "+code);
        }
    }

    private final Type type;

    private final int sugarNumber;

    public Drink(Type type, int sugarNumber) {
        this.type = type;
        this.sugarNumber = sugarNumber;
    }

    @Override
    public double getPrice() {
        return this.type.getPrice();
    }

    @Override
    public String getInstruction() {
        return MessageFormat.format(
                "{0}:{1,choice,0#|0<{1,number,integer}}:{1,choice,0#|0<1}",
                this.type.getInstruction(),
                this.sugarNumber
        );
    }
}

package io.vieira.coffeemachine.instruction;

import io.vieira.coffeemachine.payment.Payable;

import java.text.MessageFormat;

public class Drink implements Instructable, Payable {

    public enum Type implements Instructable {
        TEA("T", 0.4),
        CHOCOLATE("H", 0.5),
        COFFEE("C", 0.6),
        ORANGE_JUICE("O", 0.6);

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

    private final boolean extraHot;

    public Drink(Type type) {
        this(type, 0, false);
    }

    public Drink(Type type, int sugarNumber) {
        this(type, sugarNumber, false);
    }

    public Drink(Type type, int sugarNumber, boolean extraHot) {
        this.type = type;
        this.extraHot = type != Type.ORANGE_JUICE && extraHot;
        this.sugarNumber = type != Type.ORANGE_JUICE ? sugarNumber : 0;
    }

    @Override
    public double getPrice() {
        return this.type.getPrice();
    }

    public Type getType() {
        return type;
    }

    @Override
    public String getInstruction() {
        return MessageFormat.format(
                "{0}{1,choice,0#|1#h}:{2,choice,0#|0<{2,number,integer}}:{2,choice,0#|0<1}",
                this.type.getInstruction(),
                // MessageFormat doesn't support boolean type. So, we need to convert it...
                this.extraHot ? 1 : 0,
                this.sugarNumber
        );
    }
}

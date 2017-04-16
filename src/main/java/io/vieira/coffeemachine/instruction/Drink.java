package io.vieira.coffeemachine.instruction;

import java.text.MessageFormat;

public class Drink implements Instructable {

    public enum Type implements Instructable {
        TEA("T"),
        CHOCOLATE("H"),
        COFFEE("C");

        private final String instruction;

        Type(String drinkInstruction) {
            this.instruction = drinkInstruction;
        }

        @Override
        public String getInstruction() {
            return instruction;
        }
    }

    private final Type type;

    private final int sugarNumber;

    public Drink(Type type, int sugarNumber) {
        this.type = type;
        this.sugarNumber = sugarNumber;
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

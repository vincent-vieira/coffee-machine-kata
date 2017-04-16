package io.vieira.coffeemachine.instruction;

public class UserMessage implements Instructable {

    private final String message;

    public UserMessage(String message) {
        this.message = message;
    }

    @Override
    public String getInstruction() {
        return String.format("M:%s", this.message);
    }

    public String getMessage() {
        return message;
    }
}

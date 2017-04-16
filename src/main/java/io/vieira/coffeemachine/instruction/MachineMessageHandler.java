package io.vieira.coffeemachine.instruction;

@FunctionalInterface
public interface MachineMessageHandler {
    MachineMessageHandler DEFAULT = message -> System.out.println(message.getInstruction());

    /**
     * Handles the supplied {@link Instructable}.
     *
     * @param message the instruction to handle
     */
    void handleMessage(Instructable message);
}

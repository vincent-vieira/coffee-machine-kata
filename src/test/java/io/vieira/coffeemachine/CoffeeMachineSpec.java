package io.vieira.coffeemachine;

import io.vieira.coffeemachine.instruction.MachineMessageHandler;
import io.vieira.coffeemachine.instruction.UserMessage;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Collections;

import static info.solidsoft.mockito.java8.AssertionMatcher.assertArg;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

public class CoffeeMachineSpec {

    private final MachineMessageHandler messageHandler = Mockito.mock(MachineMessageHandler.class);

    private final CoffeeMachine coffeeMachine = new CoffeeMachineImpl(messageHandler);

    @Test
    public void processingUserMessagesShouldInvokeTheHandler(){
        coffeeMachine.process(Collections.singletonList("M:Hey!"), 0);
        verify(messageHandler).handleMessage(assertArg(message -> assertThat(message).isInstanceOf(UserMessage.class)));
    }
}

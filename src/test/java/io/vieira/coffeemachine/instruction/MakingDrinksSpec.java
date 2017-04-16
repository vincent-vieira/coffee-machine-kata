package io.vieira.coffeemachine.instruction;

import org.junit.Test;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class MakingDrinksSpec {

    //With a stick = with sugar
    @Test
    public void drinkMakerShouldMakeAllKindOfDrinksWithAStick() {
        //Coffee with sugar... What a shame, actually.
        Drink coffeeWithSugar = new Drink(Drink.Type.COFFEE,  1);
        assertThat(coffeeWithSugar.getInstruction()).isEqualTo("C:1:1");

        Drink chocolateWithSugar = new Drink(Drink.Type.CHOCOLATE, 2);
        assertThat(chocolateWithSugar.getInstruction()).isEqualTo("H:2:1");

        Drink teaWithSugar = new Drink(Drink.Type.TEA, 1);
        assertThat(teaWithSugar.getInstruction()).isEqualTo("T:1:1");

        Drink orangeJuiceWithSugar = new Drink(Drink.Type.ORANGE_JUICE, 1);
        assertThat(orangeJuiceWithSugar.getInstruction()).isEqualTo("O::");
    }

    @Test
    public void drinkMakerShouldMakeAllKindOfDrinksWithoutAStick() {
        Drink coffee = new Drink(Drink.Type.COFFEE,  0);
        assertThat(coffee.getInstruction()).isEqualTo("C::");

        Drink chocolate = new Drink(Drink.Type.CHOCOLATE, 0);
        assertThat(chocolate.getInstruction()).isEqualTo("H::");

        Drink tea = new Drink(Drink.Type.TEA, 0);
        assertThat(tea.getInstruction()).isEqualTo("T::");

        Drink orangeJuice = new Drink(Drink.Type.ORANGE_JUICE, 0);
        assertThat(orangeJuice.getInstruction()).isEqualTo("O::");
    }

    @Test
    public void drinkMakerShouldForwardMessages() {
        UserMessage message = new UserMessage("Hello, world !");
        assertThat(message.getInstruction()).isEqualTo("M:Hello, world !");
    }
}

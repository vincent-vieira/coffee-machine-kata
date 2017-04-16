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

        //Even with sugarNumber set, an orange juice can not be "sugarred" !
        Drink orangeJuiceWithSugar = new Drink(Drink.Type.ORANGE_JUICE, 1);
        assertThat(orangeJuiceWithSugar.getInstruction()).isEqualTo("O::");
    }

    @Test
    public void drinkMakerShouldMakeAllKindOfDrinksWithoutAStick() {
        Drink coffee = new Drink(Drink.Type.COFFEE);
        assertThat(coffee.getInstruction()).isEqualTo("C::");

        Drink chocolate = new Drink(Drink.Type.CHOCOLATE);
        assertThat(chocolate.getInstruction()).isEqualTo("H::");

        Drink tea = new Drink(Drink.Type.TEA);
        assertThat(tea.getInstruction()).isEqualTo("T::");

        Drink orangeJuice = new Drink(Drink.Type.ORANGE_JUICE);
        assertThat(orangeJuice.getInstruction()).isEqualTo("O::");
    }

    @Test
    public void drinkMakerShouldMakeAllKindOfExtraHotDrinksExceptOrangeJuice() {
        Drink extraHotCoffee = new Drink(Drink.Type.COFFEE, 1, true);
        assertThat(extraHotCoffee.getInstruction()).isEqualTo("Ch:1:1");

        Drink extraHotChocolate = new Drink(Drink.Type.CHOCOLATE, 1, true);
        assertThat(extraHotChocolate.getInstruction()).isEqualTo("Hh:1:1");

        Drink extraHotTea = new Drink(Drink.Type.TEA, 1, true);
        assertThat(extraHotTea.getInstruction()).isEqualTo("Th:1:1");

        // An orange juice must be fresh to be great.
        Drink extraHotOrangeJuice = new Drink(Drink.Type.ORANGE_JUICE, 0, true);
        assertThat(extraHotOrangeJuice.getInstruction()).isEqualTo("O::");
    }

    @Test
    public void drinkMakerShouldForwardMessages() {
        UserMessage message = new UserMessage("Hello, world !");
        assertThat(message.getInstruction()).isEqualTo("M:Hello, world !");
    }
}

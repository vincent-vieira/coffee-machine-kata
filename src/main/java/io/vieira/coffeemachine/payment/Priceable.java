package io.vieira.coffeemachine.payment;

public interface Priceable {
    /**
     * Gets the price of the implementing resource.
     * @return the price
     */
    double getPrice();
}

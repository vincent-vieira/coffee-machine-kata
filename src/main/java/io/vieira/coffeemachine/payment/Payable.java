package io.vieira.coffeemachine.payment;

public interface Payable {
    /**
     * Gets the price of the implementing resource.
     * @return the price
     */
    double getPrice();
}

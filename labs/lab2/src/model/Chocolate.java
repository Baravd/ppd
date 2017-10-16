package model;

import java.math.BigDecimal;

public class Chocolate extends BaseProduct {
    @Override
    public String toString() {
        return "Chocolate{" +
                "quantity=" + quantity +
                ", price=" + price +
                '}';
    }

    public Chocolate(int quantity) {
        this.price = new BigDecimal("4.5");
        this.quantity = quantity;

    }

}

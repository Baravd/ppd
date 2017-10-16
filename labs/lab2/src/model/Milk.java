package model;

import java.math.BigDecimal;

public class Milk extends BaseProduct {
    public Milk(int quantity) {
        this.price = new BigDecimal("2.3");
        this.quantity = quantity;
    }


    @Override
    public String toString() {
        return "Milk{" +
                "quantity=" + quantity +
                ", price=" + price +
                '}';
    }
}

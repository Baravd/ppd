package model;

import java.math.BigDecimal;

public class Bread extends BaseProduct {
    @Override
    public String toString() {
        return "Bread{" +
                "quantity=" + quantity +
                ", price=" + price +
                '}';
    }

    public Bread(int quantity) {
        this.price = new BigDecimal("1.9");
        this.quantity = quantity;
    }
}

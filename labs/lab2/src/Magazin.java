import model.BaseProduct;
import model.Bread;
import model.Chocolate;
import model.Milk;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Magazin {
    List<BaseProduct> products;



    public Magazin() {
        products = new ArrayList<>();
        products.add(new Milk(1000));
        products.add(new Bread(1000));
        products.add(new Chocolate(1000));


    }

    public List<BaseProduct> getProducts() {
        return products;
    }

    public void setProducts(List<BaseProduct> products) {
        this.products = products;
    }

    @Override
    public String toString() {
        return "Magazin{" +
                "products=" + products +
                '}';
    }
}

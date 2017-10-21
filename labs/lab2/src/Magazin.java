import model.BaseProduct;
import model.Bread;
import model.Chocolate;
import model.Milk;

import java.util.ArrayList;
import java.util.List;

public class Magazin {
    List<BaseProduct> products;
    List<BaseProduct> registru;


    public Magazin() {
        products = new ArrayList<>();
        registru = new ArrayList<>();
        products.add(new Milk(1000));
        products.add(new Bread(1000));
        products.add(new Chocolate(1000));
        registru.add(new Milk(0));
        registru.add(new Bread(0));
        registru.add(new Chocolate(0));


    }

    public List<BaseProduct> getRegistru() {
        return registru;
    }

    public void setRegistru(List<BaseProduct> registru) {
        this.registru = registru;
    }

    public List<BaseProduct> getProducts() {
        return products;
    }

    public void setProducts(List<BaseProduct> products) {
        this.products = products;
    }

    public Integer getSize() {
        return 1000;
    }

    @Override
    public String toString() {
        return "Magazin{" +
                "products=" + products +
                ", registru=" + registru +
                '}';
    }
}

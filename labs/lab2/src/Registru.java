import model.BaseProduct;
import model.Bread;
import model.Chocolate;
import model.Milk;

import java.util.ArrayList;
import java.util.List;

public class Registru {
    List<BaseProduct> soldProducts;

    public Registru() {
        soldProducts = new ArrayList<>();
        soldProducts.add(new Milk(0));
        soldProducts.add(new Bread(0));
        soldProducts.add(new Chocolate(0));
    }
}

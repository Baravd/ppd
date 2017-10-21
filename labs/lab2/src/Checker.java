import model.BaseProduct;

import java.util.List;

public class Checker implements Runnable {
    Magazin magazin;
    int nr;
    boolean stopCondition;

    public  boolean  isStopCondition() {
        return stopCondition;
    }

    public synchronized void setStopCondition(boolean stopCondition) {
        this.stopCondition = stopCondition;
    }

    public Checker(Magazin magazin) {
        this.magazin = magazin;
        nr = 10;
        stopCondition =false;

    }

    @Override
    public void run() {
        while (!stopCondition) {
            System.out.println("CHecker Thread!");
            nr--;
            try {
                Thread.sleep(100);
                synchronized (magazin) {
                    List<BaseProduct> products = magazin.getProducts();
                    System.out.println("Produse=" + products);
                    List<BaseProduct> registru = magazin.getRegistru();
                    System.out.println("Registru=" + registru);
                    for (int i = 0; i < products.size(); i++) {
                        if (products.get(i).getQuantity() != magazin.getSize() - registru.get(i).getQuantity()) {
                            System.out.println("!!!!!SUma gresit!!!!!!");
                            throw new IllegalThreadStateException("something went wrong!");
                        }
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }
}

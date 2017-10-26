import model.BaseProduct;

import java.util.List;
import java.util.Random;

public class Bill implements Runnable {
    Magazin magazin;
    int threadID;

    public Bill(Magazin magazin, int threadID) {
        this.magazin = magazin;
        this.threadID = threadID;
    }


    @Override
    public void run() {
        int nr=10;
        Random random = new Random();
        //mutex lock


        try {
            while(nr>0) {
                nr--;
                Thread.sleep(1000);
                System.out.println("SLEEP");

                List<BaseProduct> products = magazin.getProducts();

                for (int i = 0; i < products.size(); i++) {

                    synchronized (magazin) {
                        List<BaseProduct> registru = magazin.getRegistru();
                        int anInt = random.nextInt(9) + 1;
                        System.out.println("Thread ID=" + threadID + "scadem=" + anInt);
                        if (products.get(i).getQuantity() > anInt) {
                            products.get(i).setQuantity(products.get(i).getQuantity() - anInt);
                            registru.get(i).setQuantity(registru.get(i).getQuantity() + anInt);
                        }
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        //mutex unlock


    }
}

import model.BaseProduct;

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
        Random random = new Random();
        //mutex lock

        for (BaseProduct product : magazin.getProducts()) {
            synchronized (product) {
                System.out.println("Thread ID="+threadID);

                product.setQuantity(product.getQuantity() - random.nextInt(9) + 1);
            }
        }


        //mutex unlock


    }
}

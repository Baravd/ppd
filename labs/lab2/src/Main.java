import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        Random random = new Random();
        int nrBill = random.nextInt(10)+1;
        Magazin magazin = new Magazin();
        List<Thread> threads = new ArrayList<>();
        for(int i=0;i<nrBill;i++) {
            Bill bill = new Bill(magazin, i);
            Thread thread = new Thread(bill);
            threads.add(thread);
            thread.start();
        }
        threads.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        System.out.println(magazin);

    }
}

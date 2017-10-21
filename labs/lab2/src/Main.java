import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Random random = new Random();
        int nrBill = random.nextInt(6) + 4;
        System.out.println("Nr threaduri=" + nrBill);
        Magazin magazin = new Magazin();
        List<Thread> threads = new ArrayList<>();
        Checker checker = new Checker(magazin);
        Thread thread1 = new Thread(checker);
        thread1.setPriority(10);
        thread1.start();
        for (int i = 0; i < nrBill; i++) {
            Bill bill = new Bill(magazin, i);
            Thread thread = new Thread(bill);
            threads.add(thread);
            thread.setPriority(5);
            thread.start();
        }
        for (Thread t : threads) {
            t.join();
        }
        thread1.join();
        System.out.println(magazin);

    }
}

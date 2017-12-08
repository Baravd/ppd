import org.apache.commons.lang.mutable.MutableBoolean;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Main {

    public static final int N_THREADS = 10;


    public static void main(String[] args) {
        List<Pereche> intervale = new ArrayList<>();
        List<Thread> producers = new ArrayList<>();
        List<Thread> consumers = new ArrayList<>();
        List<Lock> locks = new ArrayList<>();
        List<MutableBoolean> booleans = new ArrayList<>();

        int n = 100;
        int w = 4;
        int rest;
        int cat;
        rest = n % w;
        cat = n / w;
        int stanga = 0;
        int dreapta = stanga + cat - 1;
        System.out.println("Rest:" + rest);
        rest = rest - 1;
        for (int i = 0; i < w; i++) {
            if (rest > 0) {
                intervale.add(new Pereche(stanga, dreapta));
                stanga = dreapta + 1;
                dreapta = stanga + cat;
                rest = rest - 1;
            } else {
                intervale.add(new Pereche(stanga, dreapta));
                stanga = dreapta + 1;
                dreapta = stanga + cat - 1;

            }

        }

        int M = 100;
        int N = 100;
        int K = 100;


        Matrice matrice1 = new Matrice(M, K);
        matrice1.populateRandom();
        Matrice matrice2 = new Matrice(K, N);
        matrice2.populateRandom();
        Matrice matrice3 = new Matrice(N, M);
        matrice3.populateRandom();

        Matrice intermediar = new Matrice(M, N);
        Matrice rezultat = new Matrice(M, M);

        ProducerConsumerImpl producerConsumer = new ProducerConsumerImpl(matrice1, matrice2, intermediar, matrice3, rezultat);


        for (int i = 0; i < w; i++) {
            Lock lock = new ReentrantLock();
            locks.add(lock);

            MutableBoolean aBoolean = new MutableBoolean(false);
            booleans.add(aBoolean);

            Runnable runnable = new Producer(producerConsumer, intervale.get(i), locks.get(i), aBoolean);
            Thread thread = new Thread(runnable);

            thread.start();
            producers.add(thread);


        }
        for (int i = 0; i < w; i++) {
            Runnable runnable1 = new Consumer(producerConsumer, intervale.get(i), locks.get(i), booleans.get(i));
            Thread thread1 = new Thread(runnable1);

            thread1.start();
            consumers.add(thread1);

        }

        for (int i = 0; i < 2; i++) {
            try {
                producers.get(i).join();
                consumers.get(i).join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Matrice rezultat1 = producerConsumer.getRezultat();
        System.out.println(rezultat1);

    }
}

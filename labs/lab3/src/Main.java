import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {

    public static final int N_THREADS = 10;

    public static void main(String[] args) throws InterruptedException {
        List<Pereche> intervale = new ArrayList<>();
        int n = 1000;
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

        int M = 1000;
        int N = 1000;
        int K = 1000;


        Matrice matrice1 = new Matrice(M, K);
        matrice1.populateRandom();
        Matrice matrice2 = new Matrice(K, N);

        Matrice rezultat = new Matrice(M, N);
        matrice2.populateRandom();

        ExecutorService executorService = Executors.newFixedThreadPool(N_THREADS);
        List<Future<Matrice>> futureList = new ArrayList<>();
        Instant start = Instant.now();
        for (int i = 0; i < w; i++) {
            Callable<Matrice> worker = new MyThread(intervale.get(i), matrice1, matrice2, rezultat);
            Future<Matrice> future = executorService.submit(worker);
            futureList.add(future);
        }
        executorService.shutdown();
        System.out.println("aici");
        for (Future<Matrice> future : futureList) {
            try {
                Matrice matrice = future.get();
                //System.out.println(matrice);
            } catch (ExecutionException e) {
            }
        }
        Instant stop = Instant.now();
        System.out.println(stop.minusMillis(start.toEpochMilli()).toEpochMilli());
    }
}

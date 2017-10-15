import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Vali on 09.10.2017.
 */
public class Main {
    public static void main(String[] args) {
        int n=10000;
        int w;
        int rest;
        int cat;
        List<Pereche> intervale = new ArrayList<>();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Nr sarcini");


        //n = scanner.nextInt();
        System.out.println("Wokers");
        w = scanner.nextInt();

        rest = n % w;
        cat = n /w;
        int stanga = 0;
        int dreapta = stanga + cat - 1;
        System.out.println("Rest:"+rest);
        rest = rest - 1;
        for (int i = 0; i < w; i++) {
            if(rest>0) {
                intervale.add(new Pereche(stanga, dreapta));
                stanga = dreapta + 1;
                dreapta = stanga + cat ;
                rest = rest - 1;
            }
            else {
                intervale.add(new Pereche(stanga, dreapta));
                stanga = dreapta+1;
                dreapta = stanga + cat -1 ;

            }

        }
        System.out.println(intervale);

        int M = 10000;
        int N = 10000;
        int K=10000;
        Matrice matrice1 = new Matrice(M,K);
        matrice1.populateRandom();
        Matrice matrice2 = new Matrice(K,N);

        Matrice rezultat = new Matrice(M, N);
        matrice2.populateRandom();
        //System.out.println(matrice1);
        //System.out.println(matrice2);


        long start = System.currentTimeMillis();

        //rezultat.multiply(new Pereche(0,M-1),matrice1,matrice2,rezultat);
        List<Thread> threads = new ArrayList<>();
        for (int i=0;i<w;i++) {
            Runnable thread = new MyThread(intervale.get(i), matrice1, matrice2, rezultat);
            Thread thread1 = new Thread(thread);
            thread1.setPriority(10);
            threads.add(thread1);
            thread1.start();

        }
        //threads.stream().forEach(thread -> thread.start());
        threads.stream().forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        long stop =  System.currentTimeMillis();
        long diff = stop-start;
        System.out.println("time start:"+start+" end:"+stop+" diff(mili second)="+diff);


    }

}

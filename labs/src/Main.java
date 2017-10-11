import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Vali on 09.10.2017.
 */
public class Main {
    public static void main(String[] args) {
        int n;
        int w;
        int rest;
        int cat;
        List<Pereche> intervale = new ArrayList<>();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Nr sarcini");


        n = scanner.nextInt();
        System.out.println("Wokers");
        w = scanner.nextInt();

        rest = n % w;
        int stanga = 1;
        int dreapta = stanga + w + 1;
        rest = rest - 1;
        System.out.println("Rest:"+rest);
        for (int i = 0; i < w; i++) {
            if(rest>0) {
                intervale.add(new Pereche(stanga, dreapta));
                stanga = dreapta + 1;
                dreapta = stanga + w + 1;
                rest = rest - 1;
            }
            else {
                intervale.add(new Pereche(stanga, dreapta));
                stanga = dreapta+1;
                dreapta = stanga + w ;

            }

        }
        System.out.println(intervale);

        int M = 2;
        int N = 2;
        int K=3;
        Matrice matrice1 = new Matrice(M,K);
        matrice1.populateRandom();
        Matrice matrice2 = new Matrice(K,N);
        matrice2.populateRandom();
        System.out.println(matrice1);
        System.out.println(matrice2);

        Matrice rezultat = new Matrice(M,N);

        rezultat.multiply(matrice1,matrice2,rezultat);
        System.out.println(rezultat);


    }

}

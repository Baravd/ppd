import java.util.List;

/**
 * Created by Vali on 09.10.2017.
 */
public class MyThread implements Runnable {
    List<Pereche> intervale;
    Matrice m1;
    Matrice m2;

    public MyThread(List<Pereche> intervale, Matrice m1, Matrice m2) {
        this.intervale = intervale;
        this.m1 = m1;
        this.m2 = m2;
    }


    @Override
    public void run() {



    }
}

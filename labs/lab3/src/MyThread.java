import java.util.concurrent.Callable;

/**
 * Created by Vali on 09.10.2017.
 */
public class MyThread implements Callable<Matrice>{
    Pereche interval;
    Matrice m1;
    Matrice m2;
    Matrice rezultat;

    public MyThread(Pereche interval, Matrice m1, Matrice m2, Matrice rezultat) {
        this.interval = interval;
        this.m1 = m1;
        this.m2 = m2;
        this.rezultat = rezultat;
    }

    public MyThread() {

    }


    @Override
    public Matrice call() throws Exception {
        System.out.println("am pornit");
        return rezultat.multiply(interval, m1, m2, rezultat);
    }
}

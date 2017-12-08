import org.apache.commons.lang.mutable.MutableBoolean;

import javax.sound.midi.Soundbank;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ProducerConsumerImpl {
    Matrice m1;
    Matrice m2;
    Matrice intermediar;
    Matrice m3;
    Matrice rezultat;


    public ProducerConsumerImpl(Matrice m1, Matrice m2, Matrice intermediar, Matrice m3, Matrice rezultat) {
        this.m1 = m1;
        this.m2 = m2;
        this.intermediar = intermediar;
        this.m3 = m3;
        this.rezultat = rezultat;


    }

    public Matrice getRezultat() {
        return rezultat;
    }

    public void put(Pereche interval,Lock lock,Condition  condition, MutableBoolean computed) throws InterruptedException {
        lock.lock();
        try {

            if(computed.booleanValue()){
                System.out.println("producator waits");
                condition.await();

            }
            intermediar.multiply(interval, m1, m2, intermediar);
            computed.setValue( true);
            condition.signalAll();
            System.out.println("am inmultim pe intervalul="+interval);

        } finally {
            System.out.println("Producator="+Thread.currentThread()+" face unlock");
            lock.unlock();
        }

    }


    public void get(Pereche interval,Lock lock, Condition condition,MutableBoolean computed) throws InterruptedException {
        lock.lock();

        try {
            while (computed.isFalse()) {
                System.out.println("consumator waits");
                condition.await();
            }
            rezultat.multiply(interval, intermediar, m3, rezultat);
            computed.setValue(false);
            System.out.println(" COnsuman intervalul = " + interval);
            condition.signalAll();

        } finally {
            System.out.println("Consumator="+Thread.currentThread()+" face unlock");
            lock.unlock();
        }

    }
}

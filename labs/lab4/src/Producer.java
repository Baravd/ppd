import org.apache.commons.lang.mutable.MutableBoolean;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Producer implements Runnable {
    ProducerConsumerImpl pc;
    Pereche interval ;
    MutableBoolean computed;
    Lock lock =new ReentrantLock();
    Condition condition;

    public Producer(ProducerConsumerImpl pc, Pereche interval,Lock lock,MutableBoolean computed) {
        this.pc = pc;
        this.interval = interval;
        this.computed =computed;
        condition =lock.newCondition();

    }

    @Override
    public void run() {
        System.out.println("Producator="+Thread.currentThread().getName());
        try {
            pc.put(interval,lock, lock.newCondition(),computed);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}

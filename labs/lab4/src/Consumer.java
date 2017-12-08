import org.apache.commons.lang.mutable.MutableBoolean;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Consumer implements Runnable {

    ProducerConsumerImpl pc;
    Pereche interval;
    MutableBoolean computed;
    Lock lock =new ReentrantLock();
    Condition condition;

    public Consumer(ProducerConsumerImpl pc, Pereche interval,Lock lock,MutableBoolean computed) {
        this.pc = pc;
        this.interval = interval;
        this.computed =computed;
    }

    @Override
    public void run() {
        try {
            System.out.println("Consumer="+Thread.currentThread().getName());
            condition= lock.newCondition();
            pc.get(interval, lock,condition,computed);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}

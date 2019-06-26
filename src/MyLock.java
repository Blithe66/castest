import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;

/**
 * 手写锁实现
 *
 */
public class MyLock implements Lock {

    //定义线程类，同事只有一个线程访问这个类 ,保证操作原子性
    AtomicReference<Thread> owner = new AtomicReference<>();

    //阻塞的线程需要放在一个队列里（等待列表）
    LinkedBlockingQueue<Thread> linkedBlockingQueues = new LinkedBlockingQueue<>();

    //加锁
    @Override
    public void lock() {
        //抢到锁 比较和交换cas机制,返回是 true的时候lock锁成功了
        //owner.compareAndSet(null,Thread.currentThread());
        //返回false,当前线程抢锁失败，加入等待列表，将线程挂起
        while (!owner.compareAndSet(null,Thread.currentThread())){
            linkedBlockingQueues.add(Thread.currentThread());
            //线程等待，挂起
            LockSupport.park();
            //执行以下代码证明线程被唤醒
            linkedBlockingQueues.remove(Thread.currentThread());
        }

    }

    @Override
    public void unlock() {
        //刚才抢到锁的线程，来释放锁,将owner 改为空，说明不能操作线程类了
        //owner.compareAndSet(Thread.currentThread(),null);
        if(owner.compareAndSet(Thread.currentThread(),null)){
            //通知队列里等待的线程可以抢锁了，
            Object[] objects = linkedBlockingQueues.toArray();
            for (Object ob: objects) {
                Thread next = (Thread) ob;
                LockSupport.unpark(next);//唤醒所有线程
            }
            //把线程从等待列表中删除,不在这里执行，转到lock中执行，能执行到park后面说明线程已经被唤醒

        }
    }

    @Override
    public boolean tryLock() {
        return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }
    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public Condition newCondition() {
        return null;
    }
}

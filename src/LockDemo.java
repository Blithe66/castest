/**
 * 自己手写锁实现，加深理解
 */
public class LockDemo {
     int f = 0;
    MyLock myLock = new MyLock();
    public void incr() {
        myLock.lock();
        try {
            f++;
        }finally {
            myLock.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        LockDemo demo = new LockDemo();
        for (int j = 0 ; j<10;j++){
            new Thread(()->{
                Thread thread = Thread.currentThread();
                for (int k = 0 ;k<10;k++){
                    demo.incr();
                }
                System.out.println(thread);
            }).start();
        }
       Thread.currentThread().sleep(1000L);
        System.out.println(demo.f);
    }
}

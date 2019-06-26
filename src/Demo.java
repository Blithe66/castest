import java.util.concurrent.atomic.AtomicInteger;

/*
    多线程测试，i++计算
 */
public class Demo {

    AtomicInteger i = new AtomicInteger(0);
    volatile int f = 0;

    public synchronized void incr (){
        f++;
    }
    public synchronized void incr1 (){
        i.getAndIncrement();
    }

    public static void main(String[] args) throws InterruptedException {
        Demo demo = new Demo();
        Thread thread = Thread.currentThread();
        for (int j = 0 ; j<3;j++){
            new Thread(()->{
                for (int k = 0 ;k<10000;k++){
                    demo.incr();
                }
                System.out.println(thread);
            }).start();
        }
       Thread.currentThread().sleep(1000L);
        System.out.println(demo.f);
    }
}

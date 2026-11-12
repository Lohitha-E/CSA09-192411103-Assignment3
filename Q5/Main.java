import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;
class CinemaBooking {
    private final boolean[] booked=new boolean[10];
    private final CyclicBarrier[] gates=new CyclicBarrier[10];
    private final AtomicInteger successfulBookings=new AtomicInteger(0);
    CinemaBooking() {
        for(int i=0;i<10;i++) gates[i]=new CyclicBarrier(10);
    }
    void bookWithoutSync(int seat) {
        if(!booked[seat]) {
            try {
                gates[seat].await();
            } catch(Exception e) {
                Thread.currentThread().interrupt();
                return;
            }
            booked[seat]=true;
            successfulBookings.incrementAndGet();
        }
    }
    synchronized void bookWithSync(int seat) {
        if(!booked[seat]) {
            booked[seat]=true;
            successfulBookings.incrementAndGet();
        }
    }
    int getUniqueSeatsSold() {
        int count=0;
        for(boolean seat:booked) if(seat) count++;
        return count;
    }
    int getSuccessfulBookings() {
        return successfulBookings.get();
    }
}
public class Main {
    static void runExperiment(boolean useSync,int trial) throws InterruptedException {
        CinemaBooking cinema=new CinemaBooking();
        Thread[] customers=new Thread[100];
        CountDownLatch startSignal=new CountDownLatch(1);
        long start=System.nanoTime();
        for(int i=0;i<customers.length;i++) {
            final int seat=i%10;
            customers[i]=new Thread(()->{
                try {
                    startSignal.await();
                    if(useSync) cinema.bookWithSync(seat);
                    else cinema.bookWithoutSync(seat);
                } catch(InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
            customers[i].start();
        }
        startSignal.countDown();
        for(Thread customer:customers) customer.join();
        long elapsed=System.nanoTime()-start;
        System.out.println("Trial "+trial+": successful="+cinema.getSuccessfulBookings()+", unique="+cinema.getUniqueSeatsSold()+", duplicates="+(cinema.getSuccessfulBookings()-cinema.getUniqueSeatsSold())+", time="+elapsed+" ns");
    }
    public static void main(String[] args) throws InterruptedException {
        System.out.println("WITHOUT SYNCHRONIZATION");
        for(int i=1;i<=10;i++) runExperiment(false,i);
        System.out.println("WITH SYNCHRONIZATION");
        for(int i=1;i<=10;i++) runExperiment(true,i);
    }
}

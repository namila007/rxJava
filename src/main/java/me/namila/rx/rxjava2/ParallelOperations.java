package me.namila.rx.rxjava2;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * The Observable (within observable) Parallel operations.
 */
public class ParallelOperations {

  /**
   * The entry point of application.
   *
   * @param args the input arguments
   * @throws InterruptedException the interrupted exception
   */
  public static void main(String[] args) throws InterruptedException {
    runObs()
            .toList() //collecting to array so we can see the values in one place;
            .subscribe(x -> {
              System.out.println(
                      "Value: " + Arrays.toString(x.toArray()) + " SUBSCRIBE THREAD:" + Thread.currentThread().getName());
            });

    //waiting for all threads to complete. we can use use BlockingSubscribe to run main Obs on the main thread
    Thread.sleep(TimeUnit.SECONDS.toMillis(10));
  }

  public static Observable<Integer> runObs() {
    long start;
    AtomicLong end = new AtomicLong();
    start = System.currentTimeMillis(); //starting time
    return Observable.range(2, 3).doOnNext(x -> System.out.println("VALUE: " + x)) //printing emiting values
            .flatMap(v -> obs(v).subscribeOn(Schedulers.newThread())) //maping emmited value to new observable and subson
            .subscribeOn(Schedulers.newThread()) //main obs subs
            .doOnComplete(() -> { //when obs end
              end.set(System.currentTimeMillis());
              System.out.printf("%n Time elapsed: %d seconds %n", TimeUnit.MILLISECONDS.toSeconds(end.get() - start));
            });
  }

  /**
   * Obs observable.
   *
   * @param i the
   * @return the observable
   */
  public static Observable<Integer> obs(int i) {
    return Observable.create(x -> {
      System.out.printf("VALUE GOT Observer : %d, OBS THREAD: %s %n", i, Thread.currentThread().getName());
      for (int k = 1; k <= i; k++) {
        System.out.println();
        try {
          x.onNext(timeConsuming(k));
        } catch (Exception e) {
          x.onError(e);
        }
      }
      x.onComplete();
    });
  }

  /**
   * Time consuming int.
   *
   * @param i the
   * @return the int
   */
  public static int timeConsuming(int i) {
    try {
      System.out.printf("VALUE GOT for Time Consuming: %d, timeConsuming THREAD: %s %n", i,
              Thread.currentThread().getName());
      int randomint = new Random().nextInt(2000 - 200) + 200;
      threadSleeper(randomint);
      System.out.printf("Slept for (ms): %d, timeConsuming THREAD: %s %n", (randomint),
              Thread.currentThread().getName());
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return i;
  }

  public static void threadSleeper(int millis) throws InterruptedException {
    Thread.sleep(millis);
  }
}

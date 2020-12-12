package me.namila.rx;

import com.github.javafaker.Faker;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class Rx1 {

  public static void main(String[] args) throws InterruptedException {
    getThreads().subscribe(x -> {
      System.out.println(Thread.currentThread().getName());
      System.out.println(x);
    });
    getThreads().subscribeOn(Schedulers.computation()).subscribe(x -> {
      System.out.println(Thread.currentThread().getName());
      System.out.println(x);
    });

    getThreads().subscribeOn(Schedulers.newThread()).observeOn(Schedulers.newThread()).doOnNext(
        x -> System.out.println(x + " ----- " + Thread.currentThread().getName())).subscribe(x -> {
      System.out.println(Thread.currentThread().getName());
      System.out.println(x);
    });
    Thread.sleep(1000);
  }

  //  public static Flowable<Integer> getValue() {
  //    return Flowable.interval(1, 2, TimeUnit.SECONDS).map(x ->)
  //  }

  public static Observable<String> getThreads() {
    return Observable.create(x -> {
      //      x.onNext(new Random().nextInt()+"  "+ Thread.currentThread().getName());
      x.onNext(new Faker().animal().name() + "  " + Thread.currentThread().getName());
      x.onComplete();
    });
  }

  public static Observable<Integer> randomValue() {
    return Observable.create(x -> {
      x.onNext(2);
      x.onComplete();
    });
  }
}

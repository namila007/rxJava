package me.namila.rx;

import com.github.javafaker.Faker;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ObsCreate {
  @SuppressWarnings("unchecked")
  public static void main(String[] args) throws InterruptedException {

    Observable[] arr = new Observable[] { fromCallable(), justWithScheduler(), fromCallable(), justWithoutScheduler(),
        justWithScheduler() };

    Observable.range(0, 5).flatMap(i -> arr[i].subscribeOn(Schedulers.newThread())).subscribeOn(Schedulers.newThread())
        .subscribe(obsSub());

    System.out.println("----------------");

    Thread.sleep(2000); //wating for threads to complete

  }

  public static Observable<String> fromCallable() {
    return Observable.defer(() -> Observable.fromCallable(() -> {
      Thread.sleep(1000);
      return new Faker().harryPotter().character();
    }).subscribeOn(Schedulers.computation()).observeOn(Schedulers.newThread()).map(
        o -> o + " " + new Faker().harryPotter().character())
        .doOnNext(x -> System.out.println("fromCallable  " + Thread.currentThread().getName()))
        .doOnComplete(() -> System.out.println("fromCallable CALLABLE ------- " + Thread.currentThread().getName())));
  }

  public static Observable<Integer> justWithScheduler() {
    return Observable.just(1, 2, 3).subscribeOn(Schedulers.newThread()).observeOn(Schedulers.computation()).doOnNext(
        x -> System.out.println("w/ justWithScheduler " + Thread.currentThread().getName())).doOnComplete(() -> {
      System.out.println("justWithScheduler ------" + Thread.currentThread().getName());
    }).map(x -> x * 1000);
  }

  public static Observable<Integer> justWithoutScheduler() {
    return Observable.just(1, 2, 3).doOnNext(
        x -> System.out.println("w/ justWithoutScheduler " + Thread.currentThread().getName())).doOnComplete(() -> {
      System.out.println("Completed justWithoutScheduler ------" + Thread.currentThread().getName());
    });
  }

  public static <T> Observer<T> obsSub() {
    return new Observer<T>() {
      @Override
      public void onSubscribe(Disposable d) {
        System.out.println("onSubscribe");
      }

      @Override
      public void onNext(T strings) {
        System.out.println("onNext: " + strings.toString() + " THREAD: " + Thread.currentThread().getName());
      }

      @Override
      public void onError(Throwable e) {
        System.out.println("onError $e");
      }

      @Override
      public void onComplete() {
        System.out.println("onComplete");
      }
    };
  }
}

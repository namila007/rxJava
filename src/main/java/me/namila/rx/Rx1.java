package me.namila.rx;

import java.util.concurrent.TimeUnit;
import io.reactivex.Flowable;
import io.reactivex.Observable;

public class Rx1 {

  public static void main(String[] args) {

  }

  public static Flowable<Integer> getValue() {
    return Flowable.interval(1, 2, TimeUnit.SECONDS).map(x ->)
  }

  public static Observable<Integer> randomValue() {
    return Observable.create(x -> {
      x.onNext(2);
      x.onComplete();
    });
  }
}

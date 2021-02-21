package me.namila.rx.reactor.tute;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class Test2 {
  public static void main(String[] args) throws InterruptedException {
    Test2 test2 = new Test2();
    Mono<String> mono = test2.getValue("Hello World");
    Mono<String> mono2 = test2.getValue("MONOO");
    mono.doOnNext(s -> System.out.println(s + " " + Thread.currentThread().getName()))
            .log()
            .subscribeOn(Schedulers.single())
            .publishOn(Schedulers.boundedElastic())
            .doOnNext(s -> System.out.println(s + " " + Thread.currentThread().getName()))
            .subscribe(System.out::println);

    mono2.subscribe(System.out::println);
    Thread.sleep(100);
  }

  public Mono<String> getValue(String t) {
    return Mono.just(t)
            .doOnNext(s -> System.out.println(s + " " + Thread.currentThread().getName()));
  }
}

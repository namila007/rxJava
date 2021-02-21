package me.namila.rx.reactor.tute;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class Threading {
    public static void main(String[] args) throws InterruptedException {
//     mono with scheduler
//        Mono<String> monoSubs =
//            Mono.just("Hello")
//                .doOnNext(s -> System.out.println(s+" "+Thread.currentThread().getName()))
//                .map(x -> x + " World ")
//                .doOnNext(s -> System.out.println(s+" "+Thread.currentThread().getName()))
//                .subscribeOn(Schedulers.boundedElastic())
//                .map(x -> x + "!! ")
//                .doOnNext(s -> System.out.println(s+" "+Thread.currentThread().getName()))
//            .log();
//        monoSubs
//            .subscribe(x -> System.out.println(x +
//                Thread.currentThread().getName()));
//        // main thread is waiting
//        Thread.sleep(100);
//    //     prints Hello World !! boundedElastic-1


        Flux.just("Hello", "World")
                .doOnNext(s -> System.out.println(s + " " + Thread.currentThread().getName()))
                .map(x -> x + " 1 ") //on boundedElastic
                .doOnNext(s -> System.out.println(s + " " + Thread.currentThread().getName()))
                .publishOn(Schedulers.single()) //changing thread to single
                .subscribeOn(Schedulers.newParallel("PARALLEL")) //subs on bound elastic
                .map(x -> x + "!! ") //on single
//                .log()
                .doOnNext(s -> System.out.println(s + " " + Thread.currentThread().getName()))
                .subscribe(x -> System.out.println(x + Thread.currentThread().getName()));
        // main thread is waiting
//        Thread.sleep(100);
        // prints Hello World !! boundedElastic-1

        //
        // mono with scheduler
//    Mono<String> monoPubs =
//        Mono.just("Hello")
//            .map(x -> x + " World ")
//            .log()
//            .doOnNext(s -> System.out.println(s+" "+Thread.currentThread().getName()))
//            .publishOn(Schedulers.boundedElastic())
//            .map(x -> x + "!! ")
//            .doOnNext(s -> System.out.println(s+" "+Thread.currentThread().getName()));
//    monoPubs
//        .subscribe(x -> System.out.println(x +
//            Thread.currentThread().getName()));
//    // main thread is waiting
//    Thread.sleep(100);
////    // prints: Hello World !! boundedElastic-1

    }
}

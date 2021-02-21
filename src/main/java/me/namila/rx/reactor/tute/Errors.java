package me.namila.rx.reactor.tute;

import reactor.core.publisher.Flux;

import java.time.Duration;

public class Errors {
    public static void main(String[] args) throws InterruptedException {
        // ToDo tryCatch
//        try {
//          // throwing an error
//          Mono.error(new Exception("HELLO"))
//              .subscribe();
//        } catch (Exception e) {
//          System.out.println("On try catch " + e);
//        }

//    // throwing an error
//    Mono.error(new Exception("HELLO"))
//        // subscribe (consumer, error consumer)
//        .subscribe(System.out::println, System.out::println);
        // prints> java.lang.Exception: HELLO
//
//    Flux.just("HELLO","WORLD")
//        .doFinally(System.out::println)
//        .subscribe(System.out::println);
//
//
        //onErrorReturn
//    Mono.just("HELLO")
//        .map(x->{
//          throw new RuntimeException();
//        })
//        .onErrorReturn("HELLO WORLD")
//        .subscribe(System.out::println,System.err::println);
//
        //onErrorResume
//    Mono.just("Hello")
//        .map(x->{
//          throw new RuntimeException("ERROR!!!");
//        })
//        .onErrorResume(x->Mono.just("I RECOVERED with exception "+ x.getMessage()))
//        .subscribe(System.out::println,System.err::println);
//
//
//    //onErrorMap
//    Mono.just("HELLOW")
//        .map(x->{
//          throw new RuntimeException("THIS");
//        })
//        .onErrorMap(x-> new RuntimeException("RE THROWING "+ x.getMessage()))
//        .subscribe(System.out::println,System.err::println);
//
//    // new interval generator
        Flux.interval(Duration.ofMillis(10))
                //taking 5
                .take(5)
                .map(x -> {
                    if (x < 2) return "tick: " + x;
                    throw new RuntimeException("ERROR");
                })
                // retry once if exception occurred
                .retry(1)
                .subscribe(System.out::println, System.err::println);

        Thread.sleep(1000);
    }
}

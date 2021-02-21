package me.namila.rx.reactor.tute;

import reactor.core.publisher.Flux;

public class Hello {
  public static void main(String[] args) {
    // Mono emits 1 item and subscribe
//    Mono<String> helloMono = Mono.just("Hello World");
//    helloMono.subscribe(System.out::println);

    // Flux emit 2 items and subscribe
    Flux<String> helloFlux = Flux.just("Hello", "World");
    helloFlux.subscribe(System.out::println);
  }
}

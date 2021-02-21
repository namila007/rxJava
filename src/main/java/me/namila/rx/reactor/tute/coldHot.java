package me.namila.rx.reactor.tute;

import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;

import java.time.Duration;

public class coldHot {

  public static void main(String[] args) throws InterruptedException {
//    //cold
//    //generating
//    Flux<Integer> coldPublisher = Flux.just(1,2,3);
//
//    //mapping
//    Flux<Integer> coldFlux1 = coldPublisher.map(x->x*5);
//    Flux<Integer> coldFlux2 = coldPublisher.map(x->x*10);
//
//    //subscribing
//    coldFlux1.subscribe(x->System.out.println("coldFlux1: "+x));
//    coldFlux2.subscribe(x->System.out.println("coldFlux2: "+x));


    //Generating flux
    Flux<Long> hotPublisher = Flux.interval(Duration.ofMillis(200));
    //making it a hotflux and adding element delay time
    ConnectableFlux<Long> connectableFlux = hotPublisher.publish();
    //connecting to first sub
    connectableFlux.subscribe(x -> System.out.println("hotFlux1: " + x));
    connectableFlux.connect();
    //waiting for 500ms to skip some stream
    Thread.sleep(500);
    // subscribing to next subscriber
    connectableFlux.subscribe(x -> System.out.println("\thotFlux2: " + x));
    // blocking main thread
    Thread.sleep(600);


  }
}

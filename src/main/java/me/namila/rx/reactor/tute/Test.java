package me.namila.rx.reactor.tute;

import reactor.core.publisher.Flux;

public class Test {

    public static void main(String[] args) {
        Flux.range(0, 3) //5
                .map(x -> x * 10) //4
//         .flatMap(x-> Flux.just(Faker.instance().harryPotter().character())) //3
//         .map(x->x+"!") //2
                .subscribe(System.out::println, System.err::println); //1
    }
}

package me.namila.rx.reactor;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

public class SerialParallel {
    public static void main(String[] args) throws InterruptedException {

        AtomicReference<Long> start1 = new AtomicReference<>();
        AtomicReference<Long> start2 = new AtomicReference<>();
        AtomicReference<Long> start3 = new AtomicReference<>();
        AtomicReference<Long> start4 = new AtomicReference<>();
        Parallel.mainMethod(0, 5)
//            .log()
                .doFirst(() -> start1.set(System.currentTimeMillis()))
                .doOnComplete(() -> System.out.println("FIRST- " + (System.currentTimeMillis() - start1.get())))
                .collectList()
                .subscribe(x -> System.out.printf("RECEIVED P1 [%s]- value-[%s]\n", Thread.currentThread().getName(), x.toString()));

        Parallel.mainMethod(5, 5)
                .doFirst(() -> start2.set(System.currentTimeMillis()))
//        .log()
                .subscribeOn(Schedulers.boundedElastic())
                .doOnComplete(() -> System.out.println("SECOND- " + (System.currentTimeMillis() - start2.get())))
                .collectList()
                .subscribe(
                        x ->
                                System.out.printf(
                                        "RECEIVED P2 [%s]- value-[%s]\n", Thread.currentThread().getName(), x.toString()));

        Serial.mainMethod(0, 5)
                //            .log()
                .doFirst(() -> start3.set(System.currentTimeMillis()))
                .doOnComplete(() -> System.out.println("SERIAL1- " + (System.currentTimeMillis() - start3.get())))
                .collectList()
                .subscribe(x -> System.out.printf("RECEIVED SERIAL1 [%s]- value-[%s]\n", Thread.currentThread().getName(), x.toString()));

        Serial.mainMethod(5, 5)
                //            .log()
                .doFirst(() -> start4.set(System.currentTimeMillis()))
                .subscribeOn(Schedulers.immediate())
                .doOnComplete(() -> System.out.println("SERIAL2- " + (System.currentTimeMillis() - start4.get())))
                .collectList()
                .subscribe(x -> System.out.printf("RECEIVED SERIAL2 [%s]- value-[%s]\n", Thread.currentThread().getName(), x.toString()));
        Thread.sleep(10000);
    }

    static class Parallel {


        static Flux<Integer> mainMethod(int s, int i) {
            return Flux.range(s, i)
//          .doOnNext(x->System.out.printf("HIGHER 1- [%s]- value-[%d]\n",Thread.currentThread().getName(),x))
                    .parallel(3)
                    .runOn(Schedulers.parallel())
                    .flatMap(x -> lowerMethod(x))
//          .doOnNext(x->System.out.printf("HIGHER 2- [%s]- value-[%d]\n",Thread.currentThread().getName(),x))
                    //          .log();
//          .doOnComplete(()->System.out.printf("HIGHER COMPLETED- [%s]\n",Thread.currentThread().getName()))
                    .sequential();


        }

        static Flux<Integer> lowerMethod(int i) {
            return Flux.just(i)
                    .subscribeOn(Schedulers.immediate())
//          .log()
//          .doOnNext(x->System.out.printf("LOWER- [%s]- value-[%d]\n",Thread.currentThread().getName(),x))
//          .delayElements(Duration.ofMillis(new Random().nextInt(1000)))
//          .doOnComplete(()->System.out.printf("LOWER COMPLETED- [%s]\n",Thread.currentThread().getName()))
                    .flatMap(x -> {
                        try {
                            sleep();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return Flux.just(x);
                    })
                    ;
        }

        static void sleep() throws InterruptedException {
            Thread.sleep(new Random().nextInt(1000));
        }
    }


    static class Serial {


        static Flux<Integer> mainMethod(int s, int i) {
            return Flux.range(s, i)
                    .concatMap(x -> lowerMethod(x));

        }

        static Flux<Integer> lowerMethod(int i) {
            return Flux.just(i)
//          .delayElements(Duration.ofMillis(new Random().nextInt(1000)))
                    .map(x -> {
                        try {
                            sleep();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return i;
                    })
                    .onErrorStop();
        }

        static void sleep() throws InterruptedException {
            Thread.sleep(new Random().nextInt(1000));
        }
    }
}

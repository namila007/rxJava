package me.namila.rx.reactor.tute;

import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;

import java.time.Duration;

public class BackPressure {

    public static void main(String[] args) throws InterruptedException {

        Flux.interval(Duration.ofMillis(1000))
                .log()
                .subscribe(
                        new BaseSubscriber<Long>() {
                            @Override
                            protected void hookOnSubscribe(Subscription subscription) {
                                subscription.request(5);
                                System.out.println(subscription);
                            }

                            @Override
                            protected void hookOnNext(Long value) {
                                request(5);
                                System.out.println(value);
                            }

                            @Override
                            protected void hookOnComplete() {
                                System.out.println("COMPLETE");
                            }
                        });

        Thread.sleep(5000);
    }
}

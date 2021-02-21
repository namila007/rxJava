package me.namila.rx.reactor.tute;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.test.publisher.TestPublisher;

import java.time.Duration;
import java.util.function.Function;

public class TestTest {

  @Test
  public void Test1() {
    Flux<String> test =
            Flux.just("Hello", "World")
                    .map(x -> x + "!")
                    .concatWith(Flux.error(new RuntimeException("Test")));

    StepVerifier.create(test)
            .expectNext("Hello!")
            .expectNext("World!")
            .expectError(RuntimeException.class)
            .log()
            .verify();
  }

  @Test
  public void testVirtualTime() {
    StepVerifier.withVirtualTime(
            () -> Flux.just("Hello", "World").delayElements(Duration.ofDays(1)).map(x -> x + "!"))
            .expectSubscription()
            .expectNoEvent(Duration.ofDays(1))
            .expectNext("Hello!")
            .expectNoEvent(Duration.ofDays(1))
            .expectNext("World!")
            .expectComplete()
            .verify(Duration.ofMillis(300));
  }

  @Test
  public void testPublisher() {
    // functional lambda func to map string to uppercase
    Function<Mono<String>, Mono<String>> capitalize = a -> a.map(String::toUpperCase);
    // creating test publisher
    final TestPublisher<String> testPublisher = TestPublisher.create();
    // init stepverifier with the function and change publisher to mono
    StepVerifier.create(capitalize.apply(testPublisher.mono()))
            // then emits values on test publisher
            .then(() -> testPublisher.emit("hello world"))
            .expectNext("HELLO WORLD")
            .expectComplete()
            .log()
            .verify();

  }
}

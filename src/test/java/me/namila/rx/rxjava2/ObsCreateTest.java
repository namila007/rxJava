package me.namila.rx.rxjava2;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.TestScheduler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * The type Obs create test.
 */
public class ObsCreateTest {

  /**
   * The Test scheduler.
   */
  TestScheduler testScheduler;


  /**
   * Sets up.
   *
   * @throws Exception the exception
   */
  @Before
  public void setUp() throws Exception {
    testScheduler = new TestScheduler();

    // hooks for schedulers
    RxJavaPlugins.setComputationSchedulerHandler(x -> testScheduler);
    RxJavaPlugins.setNewThreadSchedulerHandler(x -> testScheduler);
  }

  /**
   * After method.
   *
   * @throws Exception the exception
   */
  @After
  public void afterMethod() throws Exception {
    RxJavaPlugins.reset();
  }

  /**
   * Test just without scheduler.
   */
  @Test
  public void testJustWithoutScheduler() {
    // testing subscription
    TestObserver<Integer> testObserver = ObsCreate.justWithoutScheduler().test();

    testObserver.assertComplete();
    testObserver.assertNoErrors();
    testObserver.assertValueCount(3);
    testObserver.assertValues(1, 2, 3);
    testObserver.dispose(); // always dispose observers
  }

  /**
   * Test just with scheduler.
   */
  @Test
  public void testJustWithScheduler() {
    TestObserver<Integer> testObserver = ObsCreate.justWithScheduler().test();
    testScheduler.triggerActions(); // triggering scheduler
    testObserver.assertComplete();
    testObserver.assertNoErrors();
    testObserver.assertValueCount(3);
    testObserver.assertValues(1000, 2000, 3000);
    testObserver.dispose();
  }

  @Test
  public void testfromCallable() {
    TestObserver<String> testObserver = ObsCreate.fromCallable().test();

    testObserver.assertNoValues(); // before scheduler triggering there should no value

    testScheduler.triggerActions(); // triggerting scheduler
    testScheduler.advanceTimeBy(2, TimeUnit.SECONDS); // forwarding time
    testObserver.assertComplete();
    testObserver.assertNoErrors();
    testObserver.assertValueCount(1);
    testObserver.dispose();
  }

  @Test
  public void testJustWithoutSchedulerException() {
    TestObserver<Integer> testObserver = ObsCreate.justWithoutScheduler()
            .concatWith(Observable.error(new RuntimeException("Error Occurred"))).test();

    testObserver.assertError(RuntimeException.class);
    testObserver.assertErrorMessage("Error Occurred");
    testObserver.assertNotComplete();
  }
}

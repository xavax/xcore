package com.xavax.concurrent;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;

import static org.testng.Assert.*;
import static com.xavax.concurrent.DelayedWriter.delayedWrite;

/**
 * Test cases for the BasicPromise class.
 */
@SuppressWarnings({ "PMD.DoNotUseThreads", "PMD.SignatureDeclareThrowsException" })
public class BasicPromiseTest {
  private final static long TIMEOUT = 50000;
  private final static long SLEEP_TIME = 20;
  private final static String EXPECTED = "name: \"Test1\", ready: false, result: <null>, sentinel: ";
  private final static String NAME1 = "Test1";
  private final static String NAME2 = "Test12";
  private final static String RESULT = "Test Results";

  private BasicPromise<String> promise1;
  private BasicPromise<String> promise2;

  /**
   * @throws java.lang.Exception
   */
  @BeforeMethod
  public void setUp() throws Exception {
    promise1 = new BasicPromise<>(NAME1);
    promise2 = new BasicPromise<>(NAME2, TIMEOUT);
  }

  /**
   * Test method for
   * {@link com.xavax.concurrent.BasicPromise#isReady()}.
   */
  @Test
  public void testIsReady() {
    assertFalse(promise1.isReady());
    promise1.set(RESULT);
    assertTrue(promise1.isReady());
  }

  /**
   * Test method for
   * {@link com.xavax.concurrent.BasicPromise#getName()}.
   */
  @Test
  public void testGetName() {
    assertEquals(promise1.getName(), NAME1);
    assertEquals(promise2.getName(), NAME2);
  }

  /**
   * Test method for
   * {@link com.xavax.concurrent.BasicPromise#toString()}.
   */
  @Test
  public void testToString() {
    final String value = promise1.toString();
    assertTrue(value.contains(EXPECTED));
  }

  /**
   * Test method for
   * {@link com.xavax.concurrent.BasicPromise#get()}.
   */
  @Test
  public void testGet() {
    delayedWrite(SLEEP_TIME, promise1, RESULT);
    delayedWrite(SLEEP_TIME, promise2, RESULT);
    assertEquals(promise1.get(), RESULT);
    assertEquals(promise2.get(), RESULT);
  }

  /**
   * Test promise with timeout.
   */
  @Test
  public void testGetWithTimeout() {
    assertNull(promise2.get());
  }

  /**
   * Test interrupted promise.
   */
  @Test
  @SuppressWarnings("PMD.AccessorMethodGeneration")
  public void testInterruptedPromise() throws Exception {
    final Thread thread = new Thread() {
      /**
       * Attempt to get promised value.
       */
      public void run() {
	promise1.get();
      }
    };
    thread.start();
    Thread.sleep(SLEEP_TIME);
    thread.interrupt();
    Thread.sleep(SLEEP_TIME);
    assertTrue(promise1.wasInterrupted());
  }
}

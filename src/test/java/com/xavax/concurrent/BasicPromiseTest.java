package com.xavax.concurrent;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.equalTo;

import static com.xavax.concurrent.DelayedWriter.delayedWrite;

/**
 * Test cases for the BasicPromise class.
 */

public class BasicPromiseTest {
  private final static long TIMEOUT = 50000;
  private final static long SLEEP_TIME = 20;
  private final static String EXPECTED = "name: Test1, ready: false, result: <null>, sentinel: ";
  private final static String NAME1 = "Test1";
  private final static String NAME2 = "Test12";
  private final static String RESULT = "Test Results";

  private BasicPromise<String> promise1;
  private BasicPromise<String> promise2;

  /**
   * @throws java.lang.Exception
   */
  @Before
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
    assertThat(promise1.isReady(), is(false));
    promise1.set(RESULT);
    assertThat(promise1.isReady(), is(true));
  }

  /**
   * Test method for
   * {@link com.xavax.concurrent.BasicPromise#getName()}.
   */
  @Test
  public void testGetName() {
    assertThat(promise1.getName(), is(equalTo(NAME1)));
    assertThat(promise2.getName(), is(equalTo(NAME2)));
  }

  /**
   * Test method for
   * {@link com.xavax.concurrent.BasicPromise#toString()}.
   */
  @Test
  public void testToString() {
    final String value = promise1.toString();
    assertThat(value.contains(EXPECTED), is(true));
  }

  /**
   * Test method for
   * {@link com.xavax.concurrent.BasicPromise#get()}.
   */
  @Test
  public void testGet() {
    delayedWrite(SLEEP_TIME, promise1, RESULT);
    delayedWrite(SLEEP_TIME, promise2, RESULT);
    assertThat(promise1.get(), is(equalTo(RESULT)));
    assertThat(promise2.get(), is(equalTo(RESULT)));
  }

  /**
   * Test promise with timeout.
   */
  @Test
  public void testGetWithTimeout() {
    assertThat(promise2.get(), is(equalTo(null)));
  }

  /**
   * Test interrupted promise.
   */
  @Test
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
    assertThat(promise1.wasInterrupted(), is(true));
  }
}

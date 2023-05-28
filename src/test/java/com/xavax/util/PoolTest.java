package com.xavax.util;

import java.util.Arrays;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * PoolTest is the test case for the Pool class.
 */
public class PoolTest {
  private final static String AUTO = "AUTO";
  private final static String TEST_POOL = "test pool";
  private final static String[] NAMES = new String[] {
      "ELELMENT0", "ELELMENT1", "ELELMENT2", "ELELMENT3"
  };
  private final static String[] EXTRA_NAMES = new String[] {
      "ELELMENT4", "ELELMENT5", "ELELMENT6", "ELELMENT7"
  };

  private final static Widget[] widgets = getWidgets(NAMES);
  private final static Widget[] extra = getWidgets(EXTRA_NAMES);

  /**
   * Test the constructors.
   */
  @Test
  public void testConstructors() {
    Pool<Widget> pool = new Pool<>(TEST_POOL, Widget.class, widgets);
    assertEquals(widgets.length, pool.getCapacity());
    assertSame(pool.getType(), Widget.class);
    Object object = pool.allocate();
    assertNotNull(object);
    assertTrue(object instanceof Widget);
    assertEquals(widgets[0].getName(), ((Widget) object).getName());
    pool = new Pool<>(TEST_POOL, Widget.class, Arrays.asList(widgets));
    assertEquals(widgets.length, pool.getCapacity());
    object = pool.allocate();
    check(object, widgets[0]);
  }

  /**
   * Test adding to the pool.
   */
  @Test
  public void testAdd() {
    Pool<Widget> pool = new Pool<>(TEST_POOL, Widget.class);
    pool.add(4);
    assertEquals(4, pool.getCapacity());
    Object object = pool.allocate();
    assertNotNull(object);
    assertTrue(object instanceof Widget);
    pool = new Pool<>(TEST_POOL, Widget.class);
    pool.add(widgets);
    assertEquals(widgets.length, pool.getCapacity());
    object = pool.allocate();
    check(object, widgets[0]);
    pool = new Pool<>(TEST_POOL, Widget.class);
    pool.add(Arrays.asList(widgets));
    object = pool.allocate();
    check(object, widgets[0]);
    pool.add(extra);
    assertEquals(widgets.length + extra.length, pool.getCapacity());
    object = advance(pool, widgets.length);
    check(object, extra[0]);
  }

  /**
   * Test allocating and deallocating..
   */
  @Test
  public void testAllocate() {
    final Pool<Widget> pool = new Pool<>(TEST_POOL, Widget.class, widgets);
    Object object0 = pool.allocate();
    check(object0, widgets[0]);
    Object object1 = pool.allocate();
    check(object1, widgets[1]);
    pool.deallocate((Widget) object0);
    pool.deallocate((Widget) object1);
    advance(pool, 2);
    object0 = pool.allocate();
    check(object0, widgets[0]);
    object1 = pool.allocate();
    check(object1, widgets[1]);
  }

  /**
   * Test autoGrow and builder.
   */
  @Test
  public void testAutoGrow() {
    Pool<Widget> pool = new Pool<>(TEST_POOL, Widget.class, widgets);
    advance(pool, widgets.length);
    assertNull(pool.allocate());
    pool = new Pool<>(TEST_POOL, Widget.class, widgets);
    Widget.reset();
    pool.setAutoGrow(true);
    advance(pool, widgets.length);
    Object object = pool.allocate();
    assertNotNull(object);
    assertTrue(object instanceof Widget);
    assertEquals(AUTO + 0, ((Widget) object).getName());
    pool = new Pool<>(TEST_POOL, Widget.class, widgets);
    pool.withAutoGrow(true).withBuilder(thePool -> { return new Widget(); });
    Widget.reset();
    advance(pool, widgets.length);
    object = pool.allocate();
    assertNotNull(object);
    assertTrue(object instanceof Widget);
    assertEquals(AUTO + 0, ((Widget) object).getName());

  }

  @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
  static Widget[] getWidgets(final String... names) {
    final int size = names.length;
    Widget[] results = new Widget[size];
    for ( int i = 0; i < size; ++i ) {
      results[i] = new Widget(names[i]);
    }
    return results;
  }

  static void check(final Object object, final Widget expected) {
    assertNotNull(object);
    assertTrue(object instanceof Widget);
    assertEquals(expected.getName(), ((Widget) object).getName());
  }

  static Widget advance(final Pool<Widget> pool, final int count) {
    Widget result = null;
    for ( int i = 0; i < count; ++i ) {
      result = pool.allocate();
    }
    return result;
  }

  public static class Widget {
    private static int counter;

    private final String name;

    public Widget() {
      this.name = auto();
    }

    public Widget(final String name) {
      this.name = name;
    }

    private String auto() {
      return AUTO + counter++;
    }

    public String getName() {
      return this.name;
    }

    public String toString() {
      return this.name;
    }

    public static void reset() {
      counter = 0;
    }
  }
}

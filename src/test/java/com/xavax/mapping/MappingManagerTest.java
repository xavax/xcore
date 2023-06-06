package com.xavax.mapping;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.xavax.util.Pool;

/**
 * MappingManagerTest is the test case for MappingManager.
 */
public class MappingManagerTest {
  private final static int POOL_SIZE = 32;
  private final static String NAME1 = "channel1";
  private final static String NAME2 = "channel2";
  private final static String NAME3 = "channel3";
  private final static String NAME4 = "channel4";
  private final static MappingManager manager = MappingManager.getInstance();

  /**
   * Test the MappingManager.add method.
   */
  @Test
  public void testAdd() {
    final Channel channel1 = manager.add(new Channel(NAME1));
    final Channel channel2 = manager.add(new Channel(NAME2));
    final Channel channel3 = manager.add(new Channel(NAME3));
    final Channel channel4 = manager.add(new Channel(NAME4));
    List<Channel> channels = manager.getChannels();
    assertEquals(4, channels.size());
    assertEquals(channel1, channels.get(0));
    assertEquals(channel4, channels.get(3));
    Map<String, Channel> channelMap = manager.getChannelMap();
    assertEquals(channel2, channelMap.get(NAME2));
    assertEquals(channel3, channelMap.get(NAME3));
    assertEquals(channel4, manager.findChannel(NAME4));
    manager.reset();
    assertEquals(0, channels.size());
    assertEquals(0, channelMap.size());
  }

  /**
   * Test the MappingManager context pool.
   */
  @Test
  public void testPool() {
    MappingContext context1 = manager.beginTransaction();
    assertNotNull(context1);
    long id1 = context1.id();
    MappingContext context2 = manager.beginTransaction();
    long id2 = context2.id();
    assertNotNull(context2);
    assertNotEquals(context1, context2);
    assertNotEquals(id1, id2);
    manager.endTransaction(context1);
    manager.endTransaction(context2);
    long ids[] = new long[50];
    for ( int i = 0; i < 50; ++i ) {
      ids[i] = 0;
    }
    for ( int i = 0; i < 50; ++i ) {
      MappingContext context = manager.beginTransaction();
      assertNotNull(context);
      long id = context.id();
      check(ids, i, id);
      if ( id == id1 ) {
	assertEquals(POOL_SIZE - 2, i);
	System.out.println("reused after " + i + " transactions");
      }
    }
    Pool<MappingContext> pool = manager.getPool();
    int i = pool.getCapacity();
    assertTrue(i > POOL_SIZE);
  }

  void check(final long[] ids, final int max, final long id) {
    for ( int i = 0; i < max; ++i) {
      if ( id == ids[i] ) {
	System.out.println("duplicate found at " + i);
	break;
      }
    }
    ids[max] = id;
  }

}

package com.xavax.event;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;

import com.xavax.event.BasicEvent;
import com.xavax.event.BroadcastHelper;
import com.xavax.event.Broadcaster;
import com.xavax.event.EventQueue;
import com.xavax.event.TimeEvent;

import static org.testng.Assert.*;

public class TestEventHandling  {

  @BeforeMethod
  public void setUp()
  {}

  @Test
  public void testEvents()
  {
    broadcaster = new BroadcastHelper();

    event = new BasicEvent[5];
    observer = new SampleObserver[5];
    for ( int i= 0; i < 5; ++i ) {
      observer[i] = new SampleObserver(broadcaster, i+1);
      event[i] = new BasicEvent(i+1);
    }
    int maxCount = 10;
    for ( int i = 0; i < maxCount; ++i ) {
      for ( int j = 0; j < 5; ++j ) {
	broadcaster.broadcast(event[j]);
      }
    }
    for ( int i = 0; i < 5; ++i ) {
      int count = observer[i].count();
      assertEquals(maxCount, count);
    }
  }

  @Test
  public void testEventQueue()
  {
    int maxCount = 10;
    TimeEvent e = null;
    EventQueue eq = new EventQueue();
    for ( int i = 0; i < maxCount; ++i ) {
      Integer info = new Integer(i);
      for ( int j = 0; j < 5; ++j ) {
	e = new TimeEvent(j, info);
	eq.enqueue(e);
      }
    }
    for ( int j = 0; j < 5; ++j ) {
      int i = 0;
      do {
	e = (TimeEvent) eq.dequeue(j);
	if ( e != null ) {
	  int type = e.type();
	  assertEquals(type, j);
	  ++i;
	}
      } while ( e != null );
      assertEquals(maxCount, i);
    }
  }

  private BasicEvent[] event;
  private Broadcaster broadcaster;
  private SampleObserver[] observer;
}

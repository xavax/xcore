package com.xavax.event;

import com.xavax.event.Broadcaster;
import com.xavax.event.Event;
import com.xavax.event.Observer;

public class SampleObserver implements Observer {
  /**
   * Construct a SampleObserver.
   *
   * @param type  the type of event to observe.
   * @param source  the broadcaster.
   */
  public SampleObserver(Broadcaster source, int type)
  {
    this.type = type;
    count = 0;
    source.attach(type, this);
  }

  /**
   * Called by a Broadcaster to notify this observer of an event.
   *
   * @param event  the event being broadcast.
   */
  public void notify(Event event)
  {
    int type = event.type();
    if ( type == this.type ) {
      ++count;
    }
  }

  /**
   * Returns the number of events received since the last reset.
   *
   * @return the number of events received since the last reset.
   */
  public int count()
  {
    return this.count;
  }

  /**
   * Reset the counter.
   */
  public void reset()
  {
    count = 0;
  }

  private int type;
  private int count;
}

package com.xavax.util;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Function;

/**
 * Pool manages a pool of objects of type T. Idle objects
 * are kept in a queue until they are allocated and returned to
 * the queue when deallocated. The pool can grow either by adding
 * elements or calling a builder function. The default builder
 * uses the Class.newInstance method.
 *
 * @param <T> the type of elements in the pool.
 */
public class Pool<T> extends AbstractJoinableObject {
  private final static String ANONYMOUS = "<anonymous>";
  private final static String AUTOGROW = "autoGrow";
  private final static String CAPACITY = "capacity";
  private final static String NAME = "name";
  private final static String QUEUE = "queue";
  private final static String TYPE = "type";

  private final String name;
  private final Class<? extends T> type;
  private final Queue<T> queue;

  private boolean autoGrow;
  private int capacity;
  private Function<Pool<T>, T> builder;

  private final Function<Pool<T>, T> DEFAULT_BUILDER = (pool) -> {
	T object = null;
	try {
	  object = pool.type.cast(pool.type.getDeclaredConstructor().newInstance());
	}
	catch (IllegalAccessException | IllegalArgumentException |
	       InstantiationException | InvocationTargetException |
	       NoSuchMethodException | SecurityException e) {
	  System.out.println("Unexpected exception: " + e.getClass().getSimpleName());
	  e.printStackTrace();
	}
	return object;
  };

  /**
   * Construct a Pool for the specified type of objects. The pool is
   * empty and must be grown by adding elements.
   *
   * @param name  the name of this pool (for debugging).
   * @param type  the type of T.
   */
  public Pool(final String name, final Class<? extends T> type) {
    super();
    assert type != null;
    this.name = name == null ? ANONYMOUS : name;
    this.type = type;
    this.capacity = 0;
    this.autoGrow = false;
    queue = new ConcurrentLinkedQueue<T>();
    builder = DEFAULT_BUILDER;
  }

  /**
   * Construct a Pool for the specified type of objects with the
   * specified capacity. The elements of the pool will be created
   * automatically by calling Class.newInstance.
   *
   * @param name      the name of this pool (for debugging).
   * @param type      the type of T.
   * @param capacity  the initial size of the pool.
   */
  public Pool(final String name, final Class<? extends T> type,
	      final int capacity) {
    this(name, type);
    add(capacity);
  }

  /**
   * Pool manages a pool of objects of type T. An initial list of
   * elements is added to the pool.
   *
   * @param name      the name of this pool (for debugging).
   * @param type      the type of T.
   * @param elements  a list of elements to add to the pool.t
   */
  public Pool(final String name, final Class<? extends T> type,
	      final T[] elements) {
    this(name, type);
    add(elements);
  }

  /**
   * Pool manages a pool of objects of type T. An initial list of
   * elements is added to the pool.
   *
   * @param name      the name of this pool (for debugging).
   * @param type      the type of T.
   * @param elements  a list of elements to add to the pool.t
   */
  public Pool(final String name, final Class<? extends T> type,
	      final List<T> elements) {
    this(name, type);
    add(elements);
  }

  /**
   * Grow the pool by adding new elements to the queue.
   *
   * @param count  the number of elements to add.
   */
  public final void add(final int count) {
    if ( count > 0 ) {
      capacity += count;
      for ( int i = 0; i < count; ++i ) {
	final T object = builder.apply(this);
	queue.add(object);
      }
    }
  }

  /**
   * Grow the pool by adding an element.
   *
   * @param element  the element to add.
   */
  public final void add(final T element) {
    capacity++;
    queue.add(element);
  }

  /**
   * Grow the pool by adding elements to the queue.
   *
   * @param elements  an array of elements to add.
   */
  public final void add(final T[] elements) {
    if ( elements != null ) {
      final int size = elements.length;
      if ( size > 0 ) {
	capacity += size;
	for ( int i = 0; i < size; ++i ) {
	  queue.add(elements[i]);
	}
      }
    }
  }

  /**
   * Grow the pool by adding elements to the queue.
   *
   * @param elements  the list of elements to add.
   */
  public final void add(final List<T> elements) {
    if ( elements != null ) {
      final int size = elements.size();
      if ( size > 0 ) {
	capacity += size;
	queue.addAll(elements);
      }
    }
  }

  /**
   * Allocate an element from the pool. If the pool is empty and autoGrow is
   * false, return null. If the pool is empty and autoGrow is true, grow the
   * pool by one element and return the new element.
   *
   * @return an element from the queue or null if the queue is empty.
   */
  public T allocate() {
    T result = queue.poll();
    if ( result == null && autoGrow ) {
      add(1);
      while ( result == null ) {
	result = queue.poll();
	if ( result == null ) {
	  try {
	    queue.wait(1000);
	  }
	  catch (InterruptedException e) {
	    System.out.println("retry!");
	  }
	}
	else {
	  break;
	}
      }
    }
    return result;
  }

  /**
   * Return an element to the pool.
   *
   * @param element  the element to be returned to the pool.
   */
  public void deallocate(final T element) {
    if ( element != null ) {
      queue.add(element);
    }
  }

  /**
   * Returns the autoGrow flag.
   *
   * @return the autoGrow flag.
   */
  public boolean hasAutoGrow() {
    return this.autoGrow;
  }

  /**
   * Sets the autoGrow flag. If true, new elements will be created when an
   * element is requested and the pool is empty.
   *
   * @param flag  the new value of the autoGrow flag.
   */
  public void setAutoGrow(final boolean flag) {
    this.autoGrow = flag;
  }

  /**
   * Sets the autoGrow flag.
   *
   * @param flag  the new value of the autoGrow flag.
   * @return this Pool.
   */
  public Pool<T> withAutoGrow(final boolean flag) {
    this.autoGrow = flag;
    return this;
  }

  /**
   * Returns the current capacity of the pool.
   *
   * @return the current capacity of the pool.
   */
  public int getCapacity() {
    return this.capacity;
  }

  /**
   * Returns the builder used to create new pool elements.
   *
   * @return the builder used to create new pool elements.
   */
  public Function<Pool<T>,T> getBuilder() {
    return builder;
  }

  /**
   * Sets the builder function to use to create new pool elements.
   * The builder function should accept the pool as a parameter
   * and return a new element. 
   *
   * @param builder  the builder to use to create new pool elements.
   */
  public void setBuilder(final Function<Pool<T>,T> builder) {
    this.builder = builder == null ? DEFAULT_BUILDER : builder;
  }

  /**
   * Sets the builder function to use to create new pool elements.
   * The builder function should accept the pool as a parameter
   * and return a new element. 
   *
   * @param builder  the builder to use to create new pool elements.
   */
  public Pool<T> withBuilder(final Function<Pool<T>,T> builder) {
    this.builder = builder == null ? DEFAULT_BUILDER : builder;
    return this;
  }

  /**
   * Returns the name of this queue.
   *
   * @return the name of this queue.
   */
  public String getName() {
    return this.name;
  }

  /**
   * Returns the type of the elements in this pool.
   *
   * @return the type of the elements in this pool.
   */
  public Class<? extends T> getType() {
    return this.type;
  }

  /**
   * Output this object to the specified joiner.
   *
   * @param joiner the joiner to use for output.
   * @return this joiner.
   */
  @Override
  protected Joiner doJoin(final Joiner joiner) {
    joiner.appendField(NAME, name)
	  .appendField(TYPE, type.getSimpleName())
	  .appendField(CAPACITY, capacity)
	  .appendField(AUTOGROW, autoGrow)
	  .appendField(QUEUE, queue);
    return joiner;
  }

}

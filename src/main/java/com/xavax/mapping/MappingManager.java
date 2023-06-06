package com.xavax.mapping;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xavax.annotations.AnnotationsManager;
import com.xavax.util.Joiner;
import com.xavax.util.Pool;

import static com.xavax.logger.XLogger.*;
import static com.xavax.util.CollectionFactory.*;
import static com.xavax.util.Constants.*;

/**
 * MappingManager manages mapping metadata and facilities.
 */
public class MappingManager extends AnnotationsManager {
  private final static String ADD = "add";
  private final static String CHANNELS = "channels";
  private final static String CHANNEL_MAP = "channelMap";
  private final static String CLASSNAME = MappingManager.class.getSimpleName();
  private final static String CONTEXT_POOL = "contextPool";
  
  private final static Logger logger = LoggerFactory.getLogger(MappingManager.class);
  private final static MappingManager manager = new MappingManager();

  private final List<Channel> channels;
  private final List<Channel> channelShadowList;
  private final Map<String, Channel> channelMap;
  private final Map<String, Channel> channelShadowMap;
  private final Pool<MappingContext> contextPool;
  
  /**
   * Construct the MappingManager.
   */
  private MappingManager() {
    super();
    channels = arrayList();
    channelShadowList = Collections.unmodifiableList(channels);
    channelMap = hashMap();
    channelShadowMap = Collections.unmodifiableMap(channelMap);
    contextPool = new Pool<MappingContext>(CONTEXT_POOL, MappingContext.class, 32);
    contextPool.withAutoGrow(true);
  }

  /**
   * Returns the MappingManager singleton.
   *
   * @return the MappingManager singleton.
   */
  public static MappingManager getInstance() {
    return manager;
  }

  /**
   * Add a channel.
   *
   * @param channel  the channel to add.
   * @return the channel added.
   */
  public Channel add(final Channel channel) {
    logger.trace(TRACE_ENTER, CLASSNAME, ADD);
    if ( channel != null ) {
      final String name = channel.getName();
      if ( name != null && !name.equals(EMPTY_STRING) ) {
	channels.add(channel);
	channelMap.put(name, channel);
      }
    }
    logger.trace(TRACE_RETURN, CLASSNAME, ADD, channel);
    return channel;
  }

  /**
   * Begin a transaction by allocating a mapping context from the
   * context pool.
   *
   * @return a mapping context
   */
  public MappingContext beginTransaction() {
    MappingContext context = contextPool.allocate();
    return context;
  }

  /**
   * End a transaction. Deallocate the context.
   *
   * @param context  the mapping context for this transaction.
   */
  public void endTransaction(final MappingContext context) {
    contextPool.deallocate(context);
  }

  /**
   * Returns the channel with the specified name, or null if
   * name is null or the channel is not found.
   *
   * @param name  the channel name.
   * @return the channel with the specified name.
   */
  public Channel findChannel(final String name) {
    return name == null ? null : channelMap.get(name);
  }

  /**
   * Returns the list of channels as an unmodifiable list
   * 
   * @return the list of channels.
   */
  public List<Channel> getChannels() {
    return channelShadowList;
  }

  /**
   * Returns the map of channels as an unmodifiable map.
   *
   * @return the map of channels.
   */
  public Map<String, Channel> getChannelMap() {
    return channelShadowMap;
  }

  /**
   * Returns the context pool.
   * FOR TRSTING
   *
   * @return the context pool.
   */
  Pool<MappingContext> getPool() {
    return contextPool;
  }

  /**
   * Reset the mapping manager.
   */
  void reset() {
    channels.clear();
    channelMap.clear();
  }

  /**
   * Output this object to the specified joiner.
   *
   * @param joiner  the joiner to use for output.
   * @return this joiner.
   */
  @Override
  public Joiner doJoin(final Joiner joiner) {
    super.doJoin(joiner);
    joiner.append(CHANNELS, channels)
    	  .append(CHANNEL_MAP, channelMap);
    return joiner;
  }
}

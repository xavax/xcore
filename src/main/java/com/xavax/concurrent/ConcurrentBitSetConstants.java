package com.xavax.concurrent;

public class ConcurrentBitSetConstants {
  final static int LOG2_BITS_PER_BYTE = 3;
  final static int LOG2_BITS_PER_INT  = 5;
  final static int LOG2_BITS_PER_LONG = 6;
  final static int LOG2_BITS_PER_PAGE = 9;
  final static int BITS_PER_INT = 1 << LOG2_BITS_PER_INT;
  final static int BITS_PER_LONG = 1 << LOG2_BITS_PER_LONG;
  final static int BITS_PER_PAGE = 1 << LOG2_BITS_PER_PAGE;
  final static int BITSET_BUFFER_SIZE = 32768;

  final static int LOG2_DEFAULT_SEGMENT_SIZE = 16;
  final static int LOG2_MAX_SEGMENT_SIZE = BITS_PER_INT + LOG2_BITS_PER_PAGE;
  final static long MAX_SEGMENT_SIZE = 1 << LOG2_MAX_SEGMENT_SIZE;
  final static long DEFAULT_INITIAL_SIZE = 1 << 24;
}

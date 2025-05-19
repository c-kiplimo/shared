package com.collicode.shared.util;

/**
 * Distributed unique ID generator using Twitter's Snowflake algorithm.
 * Thread-safe implementation that generates 64-bit unique IDs with components:
 * - 41 bits for timestamp (milliseconds since epoch)
 * - 5 bits for datacenter ID
 * - 5 bits for machine ID
 * - 3 bits for sequence number
 */
public class SnowflakeIdGenerator {
  private static final long EPOCH_BITS = 41L;
  private static final long DATACENTER_ID_BITS = 5L;
  private static final long MACHINE_ID_BITS = 5L;
  private static final long SEQUENCE_BITS = 3L;

  private static final long MAX_DATACENTER_ID = ~(-1L << DATACENTER_ID_BITS);
  private static final long MAX_MACHINE_ID = ~(-1L << MACHINE_ID_BITS);
  private static final long  MAX_SEQUENCE = ~(-1L << SEQUENCE_BITS);

  private static final long DATACENTER_ID_SHIFT = SEQUENCE_BITS;
  private static final long MACHINE_ID_SHIFT = SEQUENCE_BITS + DATACENTER_ID_BITS;
  private static final long TIMESTAMP_SHIFT = SEQUENCE_BITS + DATACENTER_ID_BITS + MACHINE_ID_BITS;

  private static final long MAX_CLOCK_BACKWARDS_MS = 5L;
  private static final long MAX_TIMESTAMP_BITS = ~(-1L << EPOCH_BITS);

  private  long epoch =1672531200000L;
  private final long datacenterId;
  private final long machineId;

  private volatile long lastTimestamp = -1L;
  private volatile long sequence = 0L;

  private final Object lock = new Object();

  /**
   * Initialize Snowflake ID generator
   *
   * @param epoch Custom epoch in milliseconds
   * @param datacenterId ID of datacenter (0-31)
   * @param machineId ID of machine (0-31)
   * @throws IllegalArgumentException if any parameters are invalid
   */
  public SnowflakeIdGenerator(long epoch, long datacenterId, long machineId) {
    validateParameters(epoch, datacenterId, machineId);

    this.epoch = epoch;
    this.datacenterId = datacenterId;
    this.machineId = machineId;
  }
  public SnowflakeIdGenerator(long datacenterId, long machineId) {
    validateParameters(epoch, datacenterId, machineId);

    this.datacenterId = datacenterId;
    this.machineId = machineId;
  }

  private void validateParameters(long epoch, long datacenterId, long machineId) {
    if (datacenterId > MAX_DATACENTER_ID || datacenterId < 0) {
      throw new IllegalArgumentException(
          String.format("Datacenter ID must be between 0 and %d", MAX_DATACENTER_ID));
    }
    if (machineId > MAX_MACHINE_ID || machineId < 0) {
      throw new IllegalArgumentException(
          String.format("Machine ID must be between 0 and %d", MAX_MACHINE_ID));
    }
    if (epoch > System.currentTimeMillis()) {
      throw new IllegalArgumentException("Epoch cannot be in the future");
    }
    if (epoch < 0) {
      throw new IllegalArgumentException("Epoch cannot be negative");
    }
  }

  /**
   * Generate unique ID
   *
   * @return Unique 64-bit ID
   * @throws RuntimeException if clock moves backwards beyond tolerance or thread interrupted
   */
  public long generateId() {
    long currentTimestamp = getCurrentTimestamp();

    synchronized (lock) {
      if (currentTimestamp < lastTimestamp) {
        handleClockBackwards(currentTimestamp);
        currentTimestamp = getCurrentTimestamp();
      }

      if (currentTimestamp == lastTimestamp) {
        sequence = (sequence + 1) & MAX_SEQUENCE;
        if (sequence == 0) {
          currentTimestamp = waitForNextMillisecond();
        }
      } else {
        sequence = 0L;
      }


      lastTimestamp = currentTimestamp;

      long timestampDiff = currentTimestamp - epoch;
      if (timestampDiff > MAX_TIMESTAMP_BITS) {
        throw new RuntimeException("Timestamp bits overflow - refusing to generate ID");
      }

      return constructId(currentTimestamp);
    }
  }

  private long constructId(long timestamp) {
    return ((timestamp - epoch) << TIMESTAMP_SHIFT)
        | (datacenterId << DATACENTER_ID_SHIFT)
        | (machineId << MACHINE_ID_SHIFT)
        | sequence;
  }

  private void handleClockBackwards(long timestamp) {
    long offset = lastTimestamp - timestamp;
    if (offset > MAX_CLOCK_BACKWARDS_MS) {
      throw new RuntimeException(
          String.format("Clock moved backwards by %d ms - refusing to generate ID", offset));
    }
    try {
      Thread.sleep(offset);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new RuntimeException("ID generation interrupted", e);
    }
  }

  private long getCurrentTimestamp() {
    return System.currentTimeMillis();
  }

  private long waitForNextMillisecond() {
    long timestamp;
    do {
      timestamp = getCurrentTimestamp();
    } while (timestamp <= lastTimestamp);
    return timestamp;
  }



  public static void main(String[] args) {
    SnowflakeIdGenerator generator = new SnowflakeIdGenerator(1, 1); // Datacenter ID and Machine ID
    for (int i = 0; i < 10; i++) {
      System.out.println(generator.generateId());
    }
  }
}

package com.jiayee.lilo.broker;

// https://dzone.com/articles/kafka-producer-and-consumer-example
public interface KafkaConstants {
  // The Kafka broker's address. If Kafka is running in a cluster then you can provide
  // comma-separated addresses. For example: "localhost:9091, localhost:9092"
  public static String BOOTSTRAP_SERVERS_CONFIG = "localhost:9092";

  // Id of the producer so that the broker can determine the source of the request.
  public static String CLIENT_ID = "lilo";

  // The consumer group id used to identify to which group this consumer belongs
  public static String GROUP_ID_CONFIG = "es";

  public static String OFFSET_RESET_EARLIEST = "earliest";

  public static String OFFSET_RESET_LATEST = "latest";

  public static Integer MAX_NO_MESSAGE_FOUND_COUNT = 100;

  // The max count of records that the consumer will fetch in one iteration.
  public static Integer MAX_POLL_RECORDS_CONFIG = 1;

  public static Integer MESSAGE_COUNT = 1000;

  public static String TOPIC_NAME = "data";
}

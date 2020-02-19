package com.jiayee.lilo.broker;

import com.google.common.collect.ImmutableList;
import java.time.Duration;
import java.util.Properties;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

// https://dzone.com/articles/kafka-producer-and-consumer-example
public class LiloConsumer {
  private final Consumer<Long, String> kafkaConsumer;

  public LiloConsumer() {
    kafkaConsumer = createKafkaConsumer();
  }

  public void runKafkaConsumerOnce() {
    final ConsumerRecords<Long, String> consumerRecords = kafkaConsumer
        .poll(Duration.ofSeconds(30));
    consumerRecords.forEach(record -> {
      // TODO: Complete this
      // Parse value
      // Index to ES if applicable
    });
    kafkaConsumer.commitAsync();
    kafkaConsumer.close();
  }

  private static Consumer<Long, String> createKafkaConsumer() {
    final Properties props = new Properties();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConstants.BOOTSTRAP_SERVERS_CONFIG);
    props.put(ConsumerConfig.GROUP_ID_CONFIG, KafkaConstants.GROUP_ID_CONFIG);
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class.getName());
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, KafkaConstants.OFFSET_RESET_EARLIEST);
    props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
    props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, KafkaConstants.MAX_POLL_RECORDS_CONFIG);
    final Consumer<Long, String> consumer = new KafkaConsumer<>(props);
    consumer.subscribe(ImmutableList.of(KafkaConstants.TOPIC_NAME));
    return consumer;
  }
}

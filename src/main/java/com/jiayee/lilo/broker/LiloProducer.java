package com.jiayee.lilo.broker;

import com.google.common.collect.ImmutableList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// https://dzone.com/articles/kafka-producer-and-consumer-example
public class LiloProducer {
  private static final Logger LOG = LoggerFactory.getLogger(LiloProducer.class.getSimpleName());

  private final Producer<Long, String> kafkaProducer;

  public LiloProducer() {
    kafkaProducer = createKafkaProducer();
  }

  public void runKafkaProducerOnce() {
    // TODO: Derive the messages for records by querying updated_at columns for all tables
    final List<String> updates = ImmutableList.of("Some transaction"); // TODO: Replace message
    final List<ProducerRecord<Long, String>> records = updates.stream()
        .map(update -> new ProducerRecord<Long, String>(KafkaConstants.TOPIC_NAME, update))
        .collect(Collectors.toList());
    records.forEach(record -> {
      try {
        final RecordMetadata metadata = kafkaProducer.send(record).get();
        // TODO: Log metadata
      } catch (InterruptedException | ExecutionException e) {
        LOG.error(e.getMessage(), e);
      }
    });
  }

  private static Producer<Long, String> createKafkaProducer() {
    final Properties props = new Properties();
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConstants.BOOTSTRAP_SERVERS_CONFIG);
    props.put(ProducerConfig.CLIENT_ID_CONFIG, KafkaConstants.CLIENT_ID);
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class.getName());
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
    // May want to fill up ProducerConfig.PARTITIONER_CLASS_CONFIG if continuing with project
    return new KafkaProducer<>(props);
  }
}

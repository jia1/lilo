package com.jiayee.lilo.broker;

import com.google.common.collect.ImmutableList;
import com.jiayee.lilo.models.ImmutableJob;
import com.jiayee.lilo.models.ImmutableKafkaMessage;
import com.jiayee.lilo.models.JobStatus;
import com.jiayee.lilo.models.KafkaMessage;
import com.jiayee.lilo.models.KafkaMessageSerializer;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// https://dzone.com/articles/kafka-producer-and-consumer-example
public class LiloProducer {
  private static final Logger LOG = LoggerFactory.getLogger(LiloProducer.class.getSimpleName());

  // Not needed anymore because retention.ms config is set already
  // private final AdminClient kafkaAdminClient;

  private final Producer<Long, KafkaMessage> kafkaProducer;

  public LiloProducer() {
    // kafkaAdminClient = createKafkaAdminClient();
    kafkaProducer = createKafkaProducer();
  }

  public void runKafkaProducerOnce() {
    // TODO: Derive the messages for records by querying updated_at columns for all tables
    final List<KafkaMessage> updates = ImmutableList.of(
        ImmutableKafkaMessage.builder()
            .clazz(ImmutableJob.class)
            .modelAsString(ImmutableJob.builder()
                .jobID(5)
                .location("DE")
                .status(JobStatus.INACTIVE)
                .employerID(3)
                .build()
                .serialize())
            .build()
    );
    final List<ProducerRecord<Long, KafkaMessage>> records = updates.stream()
        .peek(update -> LOG.info(update.serialize()))
        .map(update -> new ProducerRecord<Long, KafkaMessage>(KafkaConstants.TOPIC_NAME, update))
        .collect(Collectors.toList());
    records.forEach(record -> {
      try {
        final RecordMetadata metadata = kafkaProducer.send(record).get();
        LOG.info(String.format("Publishing to %s topic", metadata.topic()));
      } catch (InterruptedException | ExecutionException e) {
        LOG.error(e.getMessage(), e);
      }
    });
  }

  /*
  private static AdminClient createKafkaAdminClient() {
    final Properties props = new Properties();
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConstants.BOOTSTRAP_SERVERS_CONFIG);
    props.put(ProducerConfig.CLIENT_ID_CONFIG, KafkaConstants.CLIENT_ID);
    return AdminClient.create(props);
  }
  */

  private static Producer<Long, KafkaMessage> createKafkaProducer() {
    final Properties props = new Properties();
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConstants.BOOTSTRAP_SERVERS_CONFIG);
    props.put(ProducerConfig.CLIENT_ID_CONFIG, KafkaConstants.CLIENT_ID);
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class.getName());
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaMessageSerializer.class.getName());
    // May want to fill up ProducerConfig.PARTITIONER_CLASS_CONFIG if continuing with project
    return new KafkaProducer<>(props);
  }
}

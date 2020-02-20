package com.jiayee.lilo.broker;

import com.jiayee.lilo.models.Employer;
import com.jiayee.lilo.models.Job;
import com.jiayee.lilo.models.KafkaMessage;
import com.jiayee.lilo.models.KafkaMessageSerializer;
import com.jiayee.lilo.repositories.EmployerRepository;
import com.jiayee.lilo.repositories.JobRepository;
import java.sql.SQLException;
import java.sql.Timestamp;
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
import org.springframework.stereotype.Component;

// https://dzone.com/articles/kafka-producer-and-consumer-example
@Component
public class LiloProducer {
  private static final Logger LOG = LoggerFactory.getLogger(LiloProducer.class.getSimpleName());

  // Not needed anymore because retention.ms config is set already
  // private final AdminClient kafkaAdminClient;

  private final Producer<Long, KafkaMessage> kafkaProducer;

  private final JobRepository jobRepository;

  private final EmployerRepository employerRepository;

  public LiloProducer(
      final JobRepository jobRepository,
      final EmployerRepository employerRepository
  ) {
    // kafkaAdminClient = createKafkaAdminClient();
    kafkaProducer = createKafkaProducer();
    this.jobRepository = jobRepository;
    this.employerRepository = employerRepository;
  }

  public void runKafkaProducerOnce(final Timestamp timestamp) throws SQLException {
    final List<Job> updatedJobs = jobRepository.getUpdatedJobs(timestamp);
    LOG.info(String.format("Number of updated jobs: %d", updatedJobs.size()));
    final List<Employer> updatedEmployers = employerRepository.getUpdatedEmployers();
    LOG.info(String.format("Number of updated employers: %d", updatedEmployers.size()));
    final List<KafkaMessage> messages = updatedJobs.stream()
        .map(Job::toKafkaMessage)
        .collect(Collectors.toList());
    messages.addAll(updatedEmployers.stream()
        .map(Employer::toKafkaMessage)
        .collect(Collectors.toList()));
    final List<ProducerRecord<Long, KafkaMessage>> records = messages.stream()
        .peek(record -> LOG.info(record.serialize()))
        .map(record -> new ProducerRecord<Long, KafkaMessage>(KafkaConstants.TOPIC_NAME, record))
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

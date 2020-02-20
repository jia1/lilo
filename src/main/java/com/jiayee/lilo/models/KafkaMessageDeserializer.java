package com.jiayee.lilo.models;

import java.nio.charset.StandardCharsets;
import org.apache.kafka.common.serialization.Deserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KafkaMessageDeserializer implements Deserializer<KafkaMessage> {
  private static final Logger LOG = LoggerFactory.getLogger(
      KafkaMessageDeserializer.class.getSimpleName());

  @Override
  public KafkaMessage deserialize(final String topic, final byte[] message) {
    final String messageString = new String(message, StandardCharsets.UTF_8);
    LOG.info(messageString);
    return KafkaMessage.deserialize(messageString);
  }
}

package com.jiayee.lilo.models;

import org.apache.kafka.common.serialization.Serializer;

public class KafkaMessageSerializer implements Serializer<KafkaMessage> {
  @Override
  public byte[] serialize(final String topic, final KafkaMessage message) {
    return message.serialize().getBytes();
  }
}

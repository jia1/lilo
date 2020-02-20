package com.jiayee.lilo.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.io.Serializable;
import org.immutables.value.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Value.Immutable
@JsonSerialize(as = ImmutableKafkaMessage.class)
@JsonDeserialize(as = ImmutableKafkaMessage.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public interface KafkaMessage extends Serializable {
  Logger LOG = LoggerFactory.getLogger(Employer.class.getSimpleName());

  ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  Class<? extends KafkaModel> getClazz();

  String getModelAsString();

  @JsonIgnore
  default String serialize() {
    try {
      return OBJECT_MAPPER.writeValueAsString(this);
    } catch (JsonProcessingException e) {
      LOG.error(e.getMessage(), e);
      return "";
    }
  }

  static KafkaMessage deserialize(final String message) {
    try {
      return OBJECT_MAPPER.readValue(message, KafkaMessage.class);
    } catch (final JsonProcessingException e) {
      LOG.error(e.getMessage(), e);
      return null;
    }
  }
}

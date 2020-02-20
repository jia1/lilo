package com.jiayee.lilo.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.Serializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

interface KafkaModel extends Serializable {
  Logger LOG = LoggerFactory.getLogger(KafkaModel.class.getSimpleName());

  ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  default String serialize() {
    try {
      return OBJECT_MAPPER.writeValueAsString(this);
    } catch (JsonProcessingException e) {
      LOG.error(e.getMessage(), e);
      return "";
    }
  }
}

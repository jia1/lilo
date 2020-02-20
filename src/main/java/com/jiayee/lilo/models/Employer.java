package com.jiayee.lilo.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.Optional;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableEmployer.class)
@JsonDeserialize(as = ImmutableEmployer.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public interface Employer extends ElasticsearchModel, KafkaModel {
  default String getElasticsearchID() {
    return String.valueOf(getEmployerID());
  }

  default KafkaMessage toKafkaMessage() {
    return ImmutableKafkaMessage.builder()
        .clazz(ImmutableEmployer.class)
        .elasticsearchID(getElasticsearchID())
        .modelAsString(this.serialize())
        .build();
  }

  @JsonProperty("employer_id")
  int getEmployerID();

  // FIXME: Field is present but value is empty in ES
  Optional<String> getIndustry();
}

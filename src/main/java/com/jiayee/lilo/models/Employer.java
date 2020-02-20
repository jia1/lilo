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
  default KafkaMessage toKafkaMessage() {
    return ImmutableKafkaMessage.builder()
        .clazz(ImmutableEmployer.class)
        .modelAsString(this.serialize())
        .build();
  }

  @JsonProperty("employer_id")
  int getEmployerID();

  Optional<String> getIndustry();
}

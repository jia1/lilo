package com.jiayee.lilo.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableJob.class)
@JsonDeserialize(as = ImmutableJob.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public interface Job extends ElasticsearchModel, KafkaModel {
  @JsonProperty("job_id")
  int getJobID();

  String getLocation();

  JobStatus getStatus();

  @JsonProperty("employer_id")
  int getEmployerID();
}

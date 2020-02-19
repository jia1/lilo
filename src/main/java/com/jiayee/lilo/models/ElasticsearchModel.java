package com.jiayee.lilo.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties("doc_id")
public interface ElasticsearchModel {
  @JsonProperty("doc_id")
  String getDocID();
}

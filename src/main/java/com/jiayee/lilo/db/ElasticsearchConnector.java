package com.jiayee.lilo.db;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jiayee.lilo.models.ElasticsearchModel;
import java.io.IOException;
import java.util.List;
import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ElasticsearchConnector {
  private static final Logger LOG = LoggerFactory.getLogger(
      ElasticsearchConnector.class.getSimpleName());

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  private final RestHighLevelClient client;

  public ElasticsearchConnector(
      @Value("${es.host:localhost}") final String host,
      @Value("${es.port:9200}") final int port,
      @Value("${es.http:http}") final String http
  ) {
    client = new RestHighLevelClient(RestClient.builder(new HttpHost(host, port, http)));
  }

  // https://dzone.com/articles/fake-data-generation-and-bulk-insert-with-elastics
  public boolean bulkInsert(final List<? extends ElasticsearchModel> models) {
    final BulkRequest bulkRequest = new BulkRequest();
    models.forEach(model -> {
      final Class<? extends ElasticsearchModel> clazz = model.getClass();
      final IndexRequest indexRequest;
      try {
        // https://stackoverflow.com/questions/39187097/elastic-search-number-of-object-passed-must-be-even
        indexRequest = new IndexRequest().index(clazz.getSimpleName().toLowerCase())
            .source(OBJECT_MAPPER.writeValueAsString(model), XContentType.JSON);
        bulkRequest.add(indexRequest);
      } catch (JsonProcessingException e) {
        e.printStackTrace();
      }
    });
    try {
      final BulkResponse response = client.bulk(bulkRequest, RequestOptions.DEFAULT); // TODO: Async
      if (response.hasFailures()) {
        LOG.warn(String.format("HTTP status code: %s", response.status().getStatus()));
        LOG.error(response.buildFailureMessage());
      }
      return !response.hasFailures();
    } catch (final IOException e) {
      LOG.error(e.getMessage(), e);
      return false;
    }
  }
}
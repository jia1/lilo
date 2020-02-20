package com.jiayee.lilo.threads;

import com.jiayee.lilo.broker.LiloProducer;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ProducerThread extends Thread {
  private static final Logger LOG = LoggerFactory.getLogger(ProducerThread.class.getSimpleName());

  private final LiloProducer producer;

  public ProducerThread(final LiloProducer producer) {
    this.producer = producer;
  }

  public void run() {
    while (true) {
      try {
        producer.runKafkaProducerOnce();
      } catch (final SQLException e) {
        LOG.error(e.getMessage(), e);
      }
    }
  }
}

package com.jiayee.lilo.threads;

import com.jiayee.lilo.broker.LiloConsumer;
import org.springframework.stereotype.Component;

@Component
public class ConsumerThread extends Thread {
  private final LiloConsumer consumer;

  public ConsumerThread(final LiloConsumer consumer) {
    this.consumer = consumer;
  }

  public void run() {
    while (true) {
      consumer.runKafkaConsumerOnce();
    }
  }
}

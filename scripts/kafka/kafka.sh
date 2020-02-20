#!/bin/bash

# https://medium.com/@Ankitthakur/apache-kafka-installation-on-mac-using-homebrew-a367cdefd273
zookeeper-server-start /usr/local/etc/kafka/zookeeper.properties
kafka-server-start /usr/local/etc/kafka/server.properties

# https://kafka.apache.org/quickstart
kafka-topics --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic test
kafka-topics --list --bootstrap-server localhost:9092

kafka-console-producer --broker-list localhost:9092 --topic test
kafka-console-consumer --bootstrap-server localhost:9092 --topic test --from-beginning

kafka-configs --zookeeper localhost:2181 --alter --entity-name test --entity-type topics --add-config retention.ms=30000
kafka-topics --zookeeper localhost:2181 --describe --topics-with-overrides

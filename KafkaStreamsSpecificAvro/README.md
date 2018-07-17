# KafkaStreamsSpecificAvro Kafka streams with Kafka Schema Registry

Shows how to use your own specific format Avro objects with Kafka Streams

Assuming you have changed where your

- Kafka logs
- Zookeeper logs

Store their data, and have updated the **RunThePipeline.ps1** PowerShell script, running things should be as simple as

- \RunThePipeline.ps1
- Open SBT, and do SBT compile
- Run **\KafkaStreamsSpecificAvro\producer\src\main\scala\com\barber\avro\ProducerApp** inside IntelliJ IDEA
- Run **\KafkaStreamsSpecificAvro\streams\src\main\scala\com\barber\avro\StreamsApp** inside IntelliJ IDEA
- Run **\KafkaStreamsSpecificAvro\subscriber\src\main\scala\com\barber\avro\SubscriberApp** inside IntelliJ IDEA

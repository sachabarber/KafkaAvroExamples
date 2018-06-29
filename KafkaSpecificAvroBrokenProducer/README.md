# KafkaSpecificAvro with Kafka Schema Registry 

Shows working/broken Avro objects against Kafka Schema Registry, from the point of view of a Kafka producer

Assuming you have changed where your

- Kafka logs
- Zookeeper logs

Store their data, and have updated the **RunThePipeline.ps1** PowerShell script, running things should be as simple as

- \RunThePipeline.ps1
- Open SBT, and do SBT compile
- Run **KafkaSpecificAvroBrokenProducer\producer\src\main\scala\com\barber\avro\ProducerApp** inside IntelliJ IDEA


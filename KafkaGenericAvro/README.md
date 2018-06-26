# KafkaGenericAvro

Assuming you have changed where your

- Kafka logs
- Zookeeper logs

Store their data, and have updated the **RunThePipeline.ps1** PowerShell script, running things should be as simple as

- \RunThePipeline.ps1
- Open SBT, and do SBT compile
- Run **ProducerApp** inside IntelliJ IDEA
- Run **ConsumerApp** inside IntelliJ IDEA

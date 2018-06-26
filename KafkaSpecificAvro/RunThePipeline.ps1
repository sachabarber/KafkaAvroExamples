$global:kafkaWindowsBatFolder = "C:\Apache\confluent-3.3.0\bin\windows\"
$global:kafkaAndZooLoggingFolder = "C:\temp\"


$global:kafkaTopics = "avro-specific-demo-topic"
	
$global:ProcessesToKill = @()



function RunPipeLine() 
{
	WriteHeader "STOPPING PREVIOUS SERVICES"
	StopZookeeper
	StopKafka
	
	$path = $kafkaAndZooLoggingFolder + "kafka-logs"
	Remove-Item -Recurse -Force $path
	
	$path = $kafkaAndZooLoggingFolder + "zookeeper"
	Remove-Item -Recurse -Force $path
	
	Start-Sleep -s 10
	
	WriteHeader "STARTING NEW SERVICE INSTANCES"
	StartZookeeper
	
	Start-Sleep -s 10
	StartKafka
	
	Start-Sleep -s 10
	StartSchemaRegistry
	
	Start-Sleep -s 10
	DeleteKafkaTopics
	
	Start-Sleep -s 10
	CreateKafkaTopics

	

	
	WaitForKeyPress

	WriteHeader "KILLING PROCESSES CREATED BY SCRIPT"
	KillProcesses
}

function WriteHeader($text) 
{
	Write-Host "========================================`r`n"
	Write-Host "$text`r`n"
	Write-Host "========================================`r`n"
}


function StopZookeeper() {
	$zookeeperCommandLine = $global:kafkaWindowsBatFolder + "zookeeper-server-stop.bat"
	Write-Host "> Zookeeper Command Line : $zookeeperCommandLine`r`n"
    $global:ProcessesToKill += start-process $zookeeperCommandLine -WindowStyle Normal -PassThru
}

function StopKafka() {
	$kafkaServerCommandLine = $global:kafkaWindowsBatFolder + "kafka-server-stop.bat" 
	Write-Host "> Kafka Server Command Line : $kafkaServerCommandLine`r`n"
    $global:ProcessesToKill += start-process $kafkaServerCommandLine  -WindowStyle Normal -PassThru
}

function StartZookeeper() {
	$zookeeperCommandLine = $global:kafkaWindowsBatFolder + "zookeeper-server-start.bat"
	$arguments = $global:kafkaWindowsBatFolder + "..\..\etc\kafka\zookeeper.properties"
	Write-Host "> Zookeeper Command Line : $zookeeperCommandLine args: $arguments `r`n"
    $global:ProcessesToKill += start-process $zookeeperCommandLine $arguments -WindowStyle Normal -PassThru
}

function StartKafka() {
	$kafkaServerCommandLine = $global:kafkaWindowsBatFolder + "kafka-server-start.bat" 
	$arguments = $global:kafkaWindowsBatFolder + "..\..\etc\kafka\server.properties"
	Write-Host "> Kafka Server Command Line : $kafkaServerCommandLine args: $arguments `r`n"
    $global:ProcessesToKill += start-process $kafkaServerCommandLine $arguments -WindowStyle Normal -PassThru
}

function StartSchemaRegistry() {
	$kafkaServerCommandLine = $global:kafkaWindowsBatFolder + "schema-registry-start.bat" 
	$arguments = $global:kafkaWindowsBatFolder + "..\..\etc\schema-registry\schema-registry.properties"
	Write-Host "> Kafka Server Command Line : $kafkaServerCommandLine args: $arguments `r`n"
    $global:ProcessesToKill += start-process $kafkaServerCommandLine $arguments -WindowStyle Normal -PassThru
}


function CreateKafkaTopics() 
{
	Foreach ($topic in $global:kafkaTopics )
	{
		$kafkaCommandLine = $global:kafkaWindowsBatFolder + "kafka-topics.bat"
		$arguments = "--zookeeper localhost:2181 --create  --replication-factor 1 --partitions 1 --topic $topic"
		Write-Host "> Create Kafka Topic Command Line : $kafkaCommandLine args: $arguments `r`n"
		$global:ProcessesToKill += start-process $kafkaCommandLine $arguments -WindowStyle Normal -PassThru
	}
	
	Write-Host "> Finished creating Kafka Topics `r`n"
}


function DeleteKafkaTopics() 
{
	Foreach ($topic in $global:kafkaTopics )
	{
		$kafkaCommandLine = $global:kafkaWindowsBatFolder + "kafka-topics.bat"
		$arguments = "--zookeeper localhost:2181 --delete  --topic $topic"
		Write-Host "> Delete Kafka Topic Command Line : $kafkaCommandLine args: $arguments `r`n"
		$global:ProcessesToKill += start-process $kafkaCommandLine $arguments -WindowStyle Normal -PassThru
	}
	
	Write-Host "> Finished creating Kafka Topics `r`n"
}


function WaitForKeyPress
{
	Write-Host -NoNewLine "Press any key to continue....`r`n"
	[Console]::ReadKey()
}


function KillProcesses() 
{
	Foreach ($processToKill in $global:ProcessesToKill )
	{
		$name = $processToKill | Get-ChildItem -Name
		Write-Host "Killing Process : $name `r`n" 
		$processToKill | Stop-Process -Force
	}
}


# Kick of the entire pipeline
RunPipeLine


 

# kafka-java-basics

Levelling up with Kafka

1. Download Kafka zip
2. Download and install java
3. Kafka/bin can be added to env variables, otherwise can be used from the source folder as well.
4. Steps for knowing env variables in Mac : cat ~/.zprofile
Lets start zookeeper :  cd kafka_2.13-2.8.0 bin/zookeeper-server-start.sh config/zookeeper.properties
1. Cat config/zookeeper.properties -> cat to view the file we can see one line saying dataDir=/tmp/zookeeper not very good practice so I’m gonna make mkdir data inside Kafka folder farahuzma@Farahs-MacBook-Air kafka_2.13-2.8.0 % mkdir data mkdir data/zookeper
2. Now I change this path in zookeeper.properties to “/Users/farahuzma/kafka_2.13-2.8.0/data/zookeeper”
3. Upon restarting zookeeper, now in the above path we can find version-2 folder
4. In data directory, I’m gonna do a mkdir kafka.. now in Cong/server.properties this path needs to be given
Lets start kafka..  farahuzma@Farahs-MacBook-Air kafka_2.13-2.8.0 % kafka-server-start config/server.properties

In summary, for Mac OS X
1. Install brew if needed: /usr/bin/ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)" 
2. Download and Setup Java 11 JDK:
    * 		brew tap caskroom/versions
    * 		brew cask install java11
3.   
4. Download & Extract the Kafka binaries from https://kafka.apache.org/downloads
5. Install Kafka commands using brew: brew install kafka
6. Try Kafka commands using kafka-topics (for example)
7. Edit Zookeeper & Kafka configs using a text editor
    *     zookeeper.properties: dataDir=/your/path/to/data/zookeeper
    *     server.properties: log.dirs=/your/path/to/data/kafka 
8. Start Zookeeper in one terminal window: 
    *     zookeeper-server-start config/zookeeper.properties
9. Start Kafka in another terminal window: 
    *     kafka-server-start config/server.properties


KAFKA CLI

1. Creating topics : under kafka folder execute Kafka-topics
2. It gives the entire list of documentation. Ok
Now do : Kafka-topics —zookeeper 127.0.0.1:2181 for creating a kafka topic we need to reference zoo keeper. Zookeeper is running on that local ip kafka-topics --zookeeper 127.0.0.1:2181 --topic first_topic --create --partitions 3 —replication-factor 1
3. I have created topic with replication factor 1 because, currently I have only 1 broker and it is not possible to have replication greater than 1 when there is only 1 broker.
4. Check topics now 
*     kafka-topics --zookeeper 127.0.0.1:2181 --list
5. Below command will give a lot of information about the topic kafka-topics --zookeeper 127.0.0.1:2181 —topic first-topic —describe  Result : Topic: first_topic	TopicId: eqPXwmXkQeSnOU4IuY7WlQ	PartitionCount: 3	ReplicationFactor: 1	Configs: 
	Topic: first_topic	Partition: 0	Leader: 0	Replicas: 0	Isr: 0
	Topic: first_topic	Partition: 1	Leader: 0	Replicas: 0	Isr: 0
	Topic: first_topic	Partition: 2	Leader: 0	Replicas: 0	Isr: 0 in the above result, it shows 3 partitions, Leader : 0 indicates that Broker with id 0 is the leader.
6. Creating a second topic with 6 partitions
*     kafka-topics --zookeeper 127.0.0.1:2181 --topic first_topic --create --partitions 3 —replication-factor 1
Run list again to see if it is added
*     kafka_2.13-2.8.0 % kafka-topics --zookeeper 127.0.0.1:2181 --list                                                            
first_topic
second_topic
to delete the second_topic:
	*     kafka-topics --zookeeper 127.0.0.1:2181 --topic second_topic —delete

KAFKA CONSOLE PRODUCER CLI

1. Commands related to kafka-console-producer command : 
*     kafka-console-producer this will list out all options
2. Why is it used? As the name suggests, messages are produced from here. We need to connect to a bootstrap server and a topic and publish messages as producer to Kafka.. :)
3. Let us now launch a producer and connect to our broker and first topic and publish messages.
*     kafka-console-producer --broker-list 127.0.0.1:9092 --topic 
running it will let us type message. broker-list needs to connect to Kafka, so the ip there is of Kafka
4. We can set our producer to have properties. Right now its using set of default properties. To provide properties follow below: If you want to set property acks
*     kafka-console-producer --broker-list 127.0.0.0:9092 --topic first_topic —producer-property acks=all upon running you will be able to send messages
What happens if you publish a message to a topic that does not exist? 
1. Kafka will first throw a warning saying, leader for the topic mentioned in the command does not exist. It gets elected. Hence producing to a topic that does not exist result in Kafka creating that topic with default setup i.e. 1 Partition, 1 Replication ( 1 copy ), 1 existing Broker and that elected as leader as well. Commands :
*     kafka-console-producer --broker-list 127.0.0.1:9092 --topic new_topic  kafka-console-producer --broker-list 127.0.0.1:9092 --topic new_topic  
	>hi farah
[2021-09-05 14:02:58,925] WARN [Producer clientId=console-producer] Error while fetching metadata 	with correlation id 3 : {new_topic=LEADER_NOT_AVAILABLE} (org.apache.kafka.clients.NetworkClient)
	>wassup 
In the above terminal commands and output, it was threw warning but later sent msg with no issues..
Now upon doing command : 
*     kafka-topics —zookeeper 127.0.0.1:2181 —list
we will get both first_topic and new_topic  Upon running 
*     kafka-topics --zookeeper 127.0.0.1:2181 --topic new_topic --describe
Topic: new_topic	TopicId: prwe50TRTMm9oEJ7eNmdbQ	PartitionCount: 1	ReplicationFactor: 1	Configs: 
Topic: new_topic	Partition: 0	Leader: 0	Replicas: 0	Isr: 0
2. Hence before producing to topic, create it otherwise it will be created with default properties.
3. You can edit your config file to tell your Kafka what should be the default properties of a topic that will be created if msg is published to unknown topic : for that : nano config/server.properties  — Log Basics -> no of partitions change from 1 to 3 Save and Exit

KAFKA CONSOLE CONSUMER

1. Command : Kafka-console-consumer will give you the description kafka-console-consumer --bootstrap-server 127.0.0.1:9092 --topic first_topic now open producer and send messages. You will receive them in consumer.
2. What if I want to read all the messages to this topic from beginning including the newly arriving messages
*     kafka-console-consumer --bootstrap-server 127.0.0.1:9092 --topic first_topic --from-beginning
KAFKA CONSUMER GROUP :
3. To consume messages in group mode Command : kafka-console-consumer --bootstrap-server 127.0.0.1:9092 --topic first_topic --group my-app  Suppose I have 2 terminals running this same command and are consuming messages in the group my-app  Now when we start producing messages, messages randomly get distributed to any one of the terminal, because both consumers are listening to different partitions. Remember that at one time one consumer in a consumer group listens exclusively from a partition. Since our topic has 3 partitions, when message got produced it can land on any partition and therefore be consumed by any of the consumers in the consumer group depending on what partition it is listening from.
4. In such a case suppose we stop any consumer from consumer group -> Partitions will be reassigned amongst existing consumers.

A consumer group with 3 consumers listening from a topic with 3 partitions: 


￼Now when any of the terminal is closed and consumer is removed, Partitions are reassigned amongst active consumers
￼If only one consumer is remaining, all partitions will publish to one.
5. Another Imp thing: Suppose we are now listening to the first-topic from  kafka-console-consumer --bootstrap-server 127.0.0.1:9092 --topic first_topic --group my-second-app --from-beginning  this will display all the messages that were produced to this topic from the beginning. All good all expected.  But now if you stop this command and run same one again, you will not see all the messages from beginning. Because the offset of messages read by my-second-app is committed. So until the messages that were last read won’t be read now.  
￼

KAFKA CONSUMER GROUPS

Command : Kafka-consumer-groups for description kafka-consumer-groups --bootstrap-server 127.0.0.1:9092 --list
Helps you view the consumer group list, delete and manage kafka-consumer-groups --bootstrap-server 127.0.0.1:9092 --group my-app --describe  Consumer group 'my-app' has no active members. // this means none is active right now
RESET OFFSET OF CONSUMER GROUPS:

Upon running Kafka-consumer-groups, you see a option —reset-offsets,   kafka-consumer-groups --bootstrap-server localhost:9092 --group my-second-app --reset-offsets --to-earliest --execute --topic first_topic

Now upon listening to messages from beginning for my-second-app, all messages will be replayed
*     kafka-console-consumer --bootstrap-server localhost:9092 --group my-second-app --topic first_topic --from-beginning
 hi farah
best of luck 
hi farah
hey
hi
second attempt
you are doing good
wassup
wassup

*     kafka-consumer-groups --bootstrap-server localhost:9092 --group my-second-app --reset-offsets --shift-by -2 execute --topic first_topic 
— this means we have shifted backwards by 2. Hence minus 2  It will shift by 2 on each partition


KAFKA has client Bi-Directional Compatibility i.e. older client can talk to a newer broker newer client can talk to an older broker :) 

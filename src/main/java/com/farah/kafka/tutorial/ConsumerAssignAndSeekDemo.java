package com.farah.kafka.tutorial;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class ConsumerAssignAndSeekDemo {
    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(ConsumerDemo.class.getName());
//        create consumer properties - check kafka documentation - dont put group id
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"127.0.0.1:9092");
        //producer sends a msg to kafka, it serializes our string and sends it to kafka. Consumer upon receiving needs do desrialize it again
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        /*
            Upon setting another consumer in addition to my-third-app,
            there are two consumers now, so this will join the group.
            It will cause re-balancing and partition assigning happens.
         */
        /*
        AUTO OFFSET RESET CONFIG
        earliest - you want to read from very beginning
        latest - you want to read only new ones
        none - will throw error
         */
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest");

        //create consumer
        KafkaConsumer<String,String> consumer = new KafkaConsumer<String, String>(properties);

        //assign and seek are used to fetch and replay data or fetch a specific message
        TopicPartition partitionToReadFrom = new TopicPartition("first_topic",0);
        long offsetToReadFrom = 15L;
        consumer.assign(Arrays.asList(partitionToReadFrom));

        consumer.seek(partitionToReadFrom,offsetToReadFrom);

        int noOfMessagesToRead = 5;
        int noOfMessagesReadSoFar = 0;
        boolean keepReading = true;

        //poll for new data - consumer does not get data unless it asks for data
        while(keepReading)
        {   //consumer polls for 100 Milli's
            ConsumerRecords<String,String> records = consumer.poll(Duration.ofMillis(100));
            for(ConsumerRecord record : records)
            {
                noOfMessagesReadSoFar++;
                logger.info("Key :"+ record.key());
                logger.info("Value :"+ record.value());
                logger.info("Partition :"+ record.partition());
                if(noOfMessagesToRead<noOfMessagesReadSoFar)
                {
                    keepReading = false;
                    break;
                }

            }
        }



    }
}

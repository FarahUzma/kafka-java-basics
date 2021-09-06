package com.farah.kafka.tutorial;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class ConsumerGroupsDemo {
    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(ConsumerDemo.class.getName());
//        create consumer properties - check kafka documentation
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"127.0.0.1:9092");
        //producer sends a msg to kafka, it serializes our string and sends it to kafka. Consumer upon receiving needs do desrialize it again
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG,"my-third-app");
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

        //subscribe to topic ( can be single or multiple)
        consumer.subscribe(Arrays.asList("first_topic"));

        //poll for new data - consumer does not get data unless it asks for data
        while(true)
        {   //consumer polls for 100 Milli's
            ConsumerRecords<String,String> records = consumer.poll(Duration.ofMillis(100));
            for(ConsumerRecord record : records)
            {
                logger.info("Key :"+ record.key());
                logger.info("Value :"+ record.value());
                logger.info("Partition :"+ record.partition());
            }
        }



    }
}

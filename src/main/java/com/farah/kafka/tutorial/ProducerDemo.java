package com.farah.kafka.tutorial;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class ProducerDemo {
    public static void main(String[] args) {
//        create producer properties - check Kafka Documentation
        Properties properties = new Properties();
//        properties.setProperty("bootstrap.servers","127.0.0.1:9092");
//        properties.setProperty("key.serializer", StringSerializer.class.getName());
//        properties.setProperty("value.serializer",StringSerializer.class.getName());
//        better way below
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"127.0.0.1:9092");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,StringSerializer.class.getName());

//        here we are telling kafka what values we will be sending and what serializer it needs to convert it into 0's and 1's
//        create producer - both key and values are string
        KafkaProducer<String,String> producer = new KafkaProducer<String, String>(properties);

//        send message
        ProducerRecord<String,String> record = new ProducerRecord<>("first_topic","Hello World");
        producer.send(record);
        //flush data - send is asynchronus.. without flush it wont work
        producer.flush();
        producer.close();
    }
}

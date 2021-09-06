package com.farah.kafka.tutorial;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class ProducerWithKey {
    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(ProducerDemoWithCallBack.class);
//        create producer properties
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
//        we can set key in the below as second argument, which will always make it go to same partition
       for(int i=0;i<10;i++)
       {
           // if you run this more than once, msg with an ID will always go to same partition
           ProducerRecord<String,String> record = new ProducerRecord<>("first_topic","id-"+i,"Hello World");
           producer.send(record, new Callback() {
               @Override
               public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                   //post sending a msg, this method is invoked
                   //its tells if there was exception in the process or if it was successfully sent
                   if(e==null)
                   {
                       logger.info("Msg Sent to \n"+"Topic : "+recordMetadata.topic()+"\n"+
                               "Time : "+recordMetadata.timestamp()
                               +"\n"+
                               "Partition : "+recordMetadata.partition());
                   }
                   else
                   {
                       logger.info("Msg Sending Failed!!! Sorry");
                   }
               }
           });
       }

                //flush data - send is asynchronus.. without flush it wont work
                producer.flush();
        producer.close();
    }


}
